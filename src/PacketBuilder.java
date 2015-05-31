
public class PacketBuilder {

	public PacketBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public static SendMessageCommand buildSendMessageCommand(String message, int source, int destination){
		return (SendMessageCommand) buildPacket(new SendMessageCommand(message), source, destination);
	}
	
	public static Packet buildPacket(Packet packet, int source, int destination){
		packet.setSource(source);
		packet.setDestination(destination);
		
		return packet;
	}

}
