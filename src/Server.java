import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException {
		new Server(12345).Run();
	}
	
	private int mIP;
	private int mDoor;
	private int mClientCounter;
	
	private Hashtable<Integer, ObjectOutputStream> mOutputStreamTable;
	private Hashtable<Integer, Socket> mClientTable;
	private Hashtable<String, Integer> mNameMap;
	private Semaphore mSemaphore;
	
	public Server(int door){
		mIP = 0xEFFFFF01;
		mDoor = door;
		mClientCounter = 0;
		mOutputStreamTable = new Hashtable<>();
		mClientTable = new Hashtable<>();
		mNameMap = new Hashtable<>();
		mSemaphore = new Semaphore(1);
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
			mOutputStreamTable.put(mClientCounter, new ObjectOutputStream(client.getOutputStream()));
			
			mSemaphore.release();
						
			new Thread(new ClientHandler(mClientCounter, this)).start();
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
			
			while(scanner.hasNextLine()){
				SendMessageCommand command = new SendMessageCommand().setMessage(scanner.nextLine());
				command.setDestination(command.BROADCAST_ADDRESS);
				command.setSource(mIP);
				
				try {
					mServer.mSemaphore.acquire();
					for(ObjectOutputStream stream : mOutputStreamTable.values()){
						stream.writeObject(command);
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
	
	public static class ClientHandler implements Runnable {
		private Integer mID;
		private Server mServer;
		
		public ClientHandler(Integer ID, Server server) throws IOException{
			mServer = server;
			mID = ID;
		}
		
		@Override
		public void run() {
			try {
				mServer.mSemaphore.acquire();
				InputStream inputStream = mServer.mClientTable.get(mID).getInputStream();
				mServer.mSemaphore.release();
				ObjectInputStream stream = new ObjectInputStream(inputStream);
				Command command;
				
				while ((command = (Command)stream.readObject()) != null) {
					handleCommand(command);
				}
				
				stream.close();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void handleCommand(Command command) throws InterruptedException{
			switch(command.mType){
			case SET_CLIENT_NAME:
				handleSetClientNameCommand((SetClientNameCommand)command);
				break;
			case SEND_MESSAGE:
				handleSendMessageCommand((SendMessageCommand)command);
				break;
			default:
				break;
			}
		}
		
		private void handleSetClientNameCommand(SetClientNameCommand command) throws InterruptedException{
			mServer.mSemaphore.acquire();
			
			mServer.mNameMap.put(command.getName(), mID);
			
			mServer.mSemaphore.release();
		}
		
		private void handleSendMessageCommand(SendMessageCommand command){
			System.out.println(command.getMessage());
		}
	}
}