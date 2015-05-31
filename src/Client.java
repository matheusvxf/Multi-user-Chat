import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		if(args[0] != null)
			new Client(args[0]).Run();
		else
			new Client().Run();
	}

	private static Random mRandom = new Random();
	private String mName;	
	private Socket mSocket;
	
	public Client(){
		this("Unkwown" + Integer.toString(mRandom.nextInt()));
	}
	
	public Client(String name){
		mName = name;
	}
	
	private void Run() throws UnknownHostException, IOException {
		mSocket = new Socket("127.0.0.1", 12345);

		System.out.println("The client connected to the server!");

		new Thread(new ServerHandler(this)).start();
		Scanner keyboard = new Scanner(System.in);
		ObjectOutputStream output = new ObjectOutputStream(mSocket.getOutputStream());
		Command command = new SetClientNameCommand().setName(mName);
		
		output.writeObject(command);
		while (keyboard.hasNextLine()) {
			command = new SendMessageCommand().setMessage(keyboard.nextLine());
			output.writeObject(command);
		}

		output.close();
		keyboard.close();
		mSocket.close();
	}

	public static class ServerHandler implements Runnable {
		private Client mClient;
		
		public ServerHandler(Client client){
			mClient = client;
		}
		
		@Override
		public void run() {
			try{
				InputStream inputStream = mClient.mSocket.getInputStream();
				ObjectInputStream stream = new ObjectInputStream(inputStream);
				Command command;
				
				while ((command = (Command)stream.readObject()) != null) {
					handleCommand(command);
				}
				
				stream.close();
			} catch(ClassNotFoundException | IOException e){
				
			}
		}
		
		private void handleCommand(Command command){
			switch(command.mType){
			case SEND_MESSAGE:
				handleSendMessageCommand((SendMessageCommand)command);
				break;
			case SET_CLIENT_NAME:
				break;
			default:
				break;
			}
		}
		
		private void handleSendMessageCommand(SendMessageCommand command){
			System.out.println(command.getMessage());
		}
	}
}