
public class PacketBuilder {

	public PacketBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public static SendMessageCommand buildSendMessageCommand(String message, int source, int destination){
		return (SendMessageCommand) buildPacket(new SendMessageCommand(message), source, destination);
	}
	
	public static ConnectionLostNotify buildConnectionLostNotify(int ID, int source, int destination){
		return (ConnectionLostNotify) buildEvent(new ConnectionLostNotify(ID), source, destination);
	}
	
	public static Event buildEvent(Event event, int source, int destination){
		return (Event) buildPacket(event, source, destination);
	}
	
	public static Packet buildPacket(Packet packet, int source, int destination){
		packet.setSource(source);
		packet.setDestination(destination);
		
		return packet;
	}
	
	public static CommandErrorNotification buildCommandErrorNotification(CommandErrorNotification notification, int source, int destination){
		return (CommandErrorNotification) buildEvent(notification, source, destination);
	}
	
	public static RegisterClientError buildRegisterClientError(String name, int source, int destination){
		return (RegisterClientError) buildCommandErrorNotification(new RegisterClientError(name), source, destination);
	}
}
