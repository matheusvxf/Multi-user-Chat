import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;


public class PacketSender {
	private ObjectOutputStream mObjectOutputStream;
	private int mSenderID;
	
	public PacketSender(OutputStream outputStream) throws IOException {
		this(outputStream, -1);
	}
	
	public PacketSender(OutputStream outputStream, int senderID) throws IOException{
		mObjectOutputStream = new ObjectOutputStream(outputStream);
		mSenderID = senderID;		
	}
	
	public void sendMessageCommand(String message, int destination) throws IOException{
		sendMessageCommand(message, mSenderID, destination);
	}
	
	public void sendMessageCommand(String message, int source, int destination) throws IOException{
		SendMessageCommand command = new SendMessageCommand();
		command.setMessage(message);
		
		sendCommand(command, source, destination);
	}
	
	public void sendRegisterClientCommand(String name) throws IOException{
		RegisterClientCommand command = new RegisterClientCommand();
		command.setName(name);
		
		sendCommand(command, Server.SERVER_ID);
	}
	
	public void sendCommand(Command command) throws IOException{
		sendPacket(command);
	}
	
	public void sendCommand(Command command, int destination) throws IOException{
		sendCommand(command, mSenderID, destination);
	}
	
	public void sendCommand(Command command, int source, int destination) throws IOException{
		sendPacket(command, source, destination);
	}
	
	public void sendClientTableNotifyEvent(Hashtable<String,Integer> nameTable) throws InterruptedException, IOException{
		ClientTableNotifyEvent event = new ClientTableNotifyEvent();
		event.setNameTable(nameTable);
		
		sendEvent(event);
	}
	
	public void sendIDNotifyEvent(int ID) throws InterruptedException, IOException{
		IDNotifyEvent event = new IDNotifyEvent();
		event.setID(ID);
		
		sendEvent(event);
	}
	
	public void sendEvent(Event event) throws InterruptedException, IOException{
		sendPacket(event);
	}
	
	public void sendPacket(Packet packet, int source, int destination) throws IOException{
		packet.setDestination(destination);
		packet.setSource(source);
		
		sendPacket(packet);
	}
	
	public void sendPacket(Packet packet) throws IOException{
		mObjectOutputStream.writeObject(packet);
	}
	
	public int getSenderID() {
		return mSenderID;
	}

	public void setSenderID(int senderID) {
		mSenderID = senderID;
	}
	
	public void close() throws IOException{
		mObjectOutputStream.close();
	}
}
