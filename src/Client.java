import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client {
	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {
		if(args.length > 0)
			new Client(args[0]).Run();
		else
			new Client().Run();
	}

	private static Random mRandom = new Random();
	private String mName;	
	private int mID;
	private Socket mSocket;
	private Hashtable<String, Integer> mNameTable;
	private Hashtable<Integer, String> mIDTable;
	private PacketSender mSender;
	
	private Semaphore mSemaphore;
	
	public Client(){
		this("unknown" + Integer.toString(mRandom.nextInt() % 1000));
	}
	
	public Client(String name){
		mName = name;
		mID = -1;
		mNameTable = new Hashtable<>();
		mIDTable = new Hashtable<>();
		mSemaphore = new Semaphore(1);
	}
	
	private void Run() throws UnknownHostException, IOException, InterruptedException {
		mSocket = new Socket("127.0.0.1", 12345);
		mSender = new PacketSender(mSocket.getOutputStream());
		Scanner keyboard = new Scanner(System.in);

		System.out.println("The client connected to the server!");
		mSender.sendRegisterClientCommand(mName);

		new Thread(new ServerHandler()).start();
		
		while (keyboard.hasNextLine()) {
			Scanner scanner = new Scanner(keyboard.nextLine());
			scanner.useDelimiter(" ");
			buildCommand(scanner);
		}

		keyboard.close();
		mSender.close();
		mSocket.close();
	}
	
	private void buildCommand(Scanner scanner) throws InterruptedException, IOException{
		try{
			String command = scanner.next();
			
			if(command.equals("send")){
				buildSendMessageCommand(scanner);					
			} else{
				throw new NoSuchElementException("Invalid command.");
			}
		} catch(NoSuchElementException e){
			String message = e.getLocalizedMessage();
			
			if(message == null)
				message = "Invalid command.";
			
			System.out.println(message);
		}
	}
	
	private void buildSendMessageCommand(Scanner scanner) throws IOException, InterruptedException{
		String token = scanner.next();
		
		mSemaphore.acquire();
		try{
			if(mNameTable.containsKey(token)){
				String message = scanner.nextLine().trim();
				
				if(!message.isEmpty())
					mSender.sendMessageCommand(message, mNameTable.get(token));
			} else{
				throw new NoSuchElementException("Invalid destination.");
			}
		} catch(NoSuchElementException e){
			String message = e.getLocalizedMessage();			
			System.out.println(message);
		}
		mSemaphore.release();
	}

	private class ServerHandler implements Runnable {
		
		@Override
		public void run() {
			try{
				InputStream inputStream = mSocket.getInputStream();
				ObjectInputStream stream = new ObjectInputStream(inputStream);
				Packet packet;
				
				while ((packet = (Packet)stream.readObject()) != null) {
					handlePacket(packet);
				}
				
				stream.close();
			} catch(ClassNotFoundException | IOException e){
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void handlePacket(Packet packet) throws InterruptedException{
			switch(packet.getType()){
			case COMMAND:
				handleCommand((Command) packet);
				break;
			case EVENT:
				handleEvent((Event) packet);
				break;
			default:
				break;
			}
		}
		
		private void handleCommand(Command command){
			switch(command.mType){
			case SEND_MESSAGE:
				handleSendMessageCommand((SendMessageCommand)command);
				break;
			case REGISTER_CLIENT:
				break;
			default:
				break;
			}
		}
		
		private void handleSendMessageCommand(SendMessageCommand command){
			System.out.println(mIDTable.get(command.getSource()) + " said: " + command.getMessage());
		}
		
		private void handleEvent(Event event) throws InterruptedException{
			switch(event.getEventType()){
			case NEW_CLIENT:
				handleNewClientEvent((NewClientEvent) event);
				break;
			case ID_NOTIFY:
				handleIDNotifyEvent((IDNotifyEvent) event);
				break;
			case CLIENT_TABLE_NOTIFY:
				handleClientTableNotifyEvent((ClientTableNotifyEvent) event);
				break;
			case CONNECTION_LOST_NOTIFY:
				handleConnectionLostNotify((ConnectionLostNotify) event);
				break;
			case COMMAND_ERROR:
				handleCommandError((CommandErrorNotification) event);
			default:
				break;
			}
		}
		
		private void handleNewClientEvent(NewClientEvent event) throws InterruptedException{
			Logger.clientOnline(event.getName());
			
			mSemaphore.acquire();
			mNameTable.put(event.getName(), event.getID());
			mIDTable.put(event.getID(), event.getName());
			mSemaphore.release();
		}
		
		private void handleIDNotifyEvent(IDNotifyEvent event){
			mID = event.getID();
			mSender.setSenderID(mID);
		}
		
		private void handleClientTableNotifyEvent(ClientTableNotifyEvent event) throws InterruptedException{
			Iterator<Entry<String, Integer>> iterator = event.getNameTable().entrySet().iterator();
			
			mSemaphore.acquire();
			while(iterator.hasNext()){
				Entry<String, Integer> entry = iterator.next();
				mNameTable.put(entry.getKey(), entry.getValue());
				mIDTable.put(entry.getValue(), entry.getKey());
			}
			mSemaphore.release();
			
			for(Entry<Integer, String> entry : mIDTable.entrySet()){
				if(entry.getKey() != Server.SERVER_ID && entry.getKey() != mID)
					Logger.clientOnline(entry.getValue());
			}
		}
		
		private void handleConnectionLostNotify(ConnectionLostNotify event){
			int ID = event.getID();
			String name = mIDTable.get(ID);
			
			Logger.clientLeaved(name);
			
			mNameTable.remove(name);
			mIDTable.remove(ID);
		}
		
		private void handleCommandError(CommandErrorNotification error){
			switch(error.getErrorType()){
			case REGISTER_CLIENT:
				handleRegisterClientError((RegisterClientError) error);
				break;
			}
		}
		
		private void handleRegisterClientError(RegisterClientError error){
			
		}
	}
}