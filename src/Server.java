import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException {
		new Server(12345).Run();
	}
	private static Random mRandom = new Random();
	private static String TAG = "Server";
	
	public static int SERVER_ID = 1;
	
	private int mIP;
	private int mDoor;
	private int mID;
	private int mClientCounter;
	private String mName;
	
	private Hashtable<Integer, PacketSender> mPacketSender;
	private Hashtable<Integer, Socket> mClientTable;
	private Hashtable<String, Integer> mNameTable;
	private Hashtable<Integer, String> mIDTable;
	
	private Semaphore mSemaphore;
	
	public Server(int door){
		mIP = 0xEFFFFF01; // 127.0.0.1
		mDoor = door;
		mID = SERVER_ID;
		mName = "server";
		mClientCounter = 1;
		mPacketSender = new Hashtable<>();
		mClientTable = new Hashtable<>();
		mNameTable = new Hashtable<>();
		mIDTable = new Hashtable<>();
		mSemaphore = new Semaphore(1);
		
		mNameTable.put(mName, mID);
		mIDTable.put(mID, mName);
	}

	@SuppressWarnings("resource")
	private void Run() throws IOException, InterruptedException {
		ServerSocket server = new ServerSocket(mDoor);
		System.out.println("Door " + mDoor + " open!");
		
		new Thread(new ServerCommandLine(System.in, this)).start();

		while (true) {
			Socket client = server.accept();

			System.out.println("New connection with the client "
					+ client.getInetAddress().getHostAddress());

			mSemaphore.acquire();
			
			mClientCounter++;
			mClientTable.put(mClientCounter, client);
			mPacketSender.put(mClientCounter, new PacketSender(client.getOutputStream()));
			
			mSemaphore.release();
						
			new Thread(new ClientHandler(this, client, mClientCounter)).start();
		}
	}

	private class ServerCommandLine implements Runnable{
		private InputStream mInputStream;
		private Server mServer;
		
		public ServerCommandLine(InputStream inputStream, Server server){
			mInputStream = inputStream;
			mServer = server;
		}

		@Override
		public void run(){
			Scanner scanner = new Scanner(mInputStream);
			
			// Server broadcast its command line input
			while(scanner.hasNextLine()){				
				try {
					String line = scanner.nextLine();
					SendMessageCommand command = PacketBuilder.buildSendMessageCommand(line, mID, Packet.BROADCAST_ADDRESS);
					
					mServer.mSemaphore.acquire();
					for(PacketSender sender : mPacketSender.values()){
						sender.sendCommand(command);
					}
					mServer.mSemaphore.release();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			scanner.close();
		}
	}
	
	private void removeClient(int ID){
		mNameTable.remove(mIDTable.get(ID));
		mClientTable.remove(ID);
		mPacketSender.remove(ID);
		mIDTable.remove(ID);
	}
	
	private class ClientHandler implements Runnable {
		private int mID;
		private Server mServer;
		private Socket mSocket;
		
		public ClientHandler(Server server, Socket socket, Integer ID) throws IOException{
			mServer = server;
			mSocket = socket;
			mID = ID;
		}
		
		@Override
		public void run() {
			try {
				mSemaphore.acquire();
				InputStream inputStream = mClientTable.get(mID).getInputStream();
				mPacketSender.get(mID).sendClientTableNotifyEvent(mNameTable);
				mSemaphore.release();
				
				ObjectInputStream stream = new ObjectInputStream(inputStream);
				Command command;
				
				while ((command = (Command)stream.readObject()) != null) {
					handleCommand(command);
				}
				
				stream.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e)	{
				System.out.println("Lost connection with " + mIDTable.get(mID) + 
						" (" + mSocket.getInetAddress().getHostAddress() + ")");
				mServer.removeClient(mID);
				try {
					connectionLostNotify();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void connectionLostNotify() throws InterruptedException, IOException{
			ConnectionLostNotify event = PacketBuilder.buildConnectionLostNotify(mID, mServer.mID, Packet.BROADCAST_ADDRESS);
			
			for(PacketSender sender : mPacketSender.values()){
				sender.sendEvent(event);
			}
		}	
		
		private void handleCommand(Command command) throws InterruptedException, IOException{
			switch(command.mType){
			case REGISTER_CLIENT:
				handleRegisterClientCommand((RegisterClientCommand) command);
				break;
			case SEND_MESSAGE:
				handleSendMessageCommand((SendMessageCommand) command);
				break;
			default:
				break;
			}
		}
		
		private void handleRegisterClientCommand(RegisterClientCommand command) throws InterruptedException, IOException{
			mSemaphore.acquire();
			
			if(mNameTable.containsKey(command.getName())){ // Invalid Name
				command.setName("unknown" + Integer.toString(mRandom.nextInt() % 1000));
			}
				
			mNameTable.put(command.getName(), mID);
			mIDTable.put(mID, command.getName());
			mPacketSender.get(mID).sendIDNotifyEvent(mID);
			
			mSemaphore.release();
			
			brodcastClientName(command.getName());
		}
		
		private void handleSendMessageCommand(SendMessageCommand command) throws IOException, InterruptedException{;
			System.out.println("Forwarding from " +
					mIDTable.get(command.getSource()) + " to " +
					mIDTable.get(command.getDestination()));
			mSemaphore.acquire();
			
			if(command.getDestination() == mServer.mID){
				System.out.println(mIDTable.get(command.getSource()) + " said: " + command.getMessage());
			} else if(mPacketSender.containsKey(command.getDestination())){
				mPacketSender.get(command.getDestination()).sendPacket(command);
			}
			
			mSemaphore.release();
		}
		
		private void brodcastClientName(String name) throws InterruptedException, IOException
		{
			NewClientEvent event = new NewClientEvent();
			event.setID(mID);
			event.setName(name);
			
			mSemaphore.acquire();
			
			for(PacketSender sender : mPacketSender.values()){
				sender.sendEvent(event);
			}
			
			mSemaphore.release();
		}
	}
}