

public class Command extends Packet {
	private static final long serialVersionUID = 1L;

	public enum Type {
		REGISTER_CLIENT,
		SEND_MESSAGE
	}
	
	Type mType;
	
	public Command(Type type){
		super(Packet.Type.COMMAND);
		mType = type;
	}
	
	public static String toString(Type type){
		switch(type){
		case REGISTER_CLIENT:
			return "SET_CLIENT_MESSAGE";
		case SEND_MESSAGE:
			return "SEND_MESSAGE";
		default:
			return "unknow";
		}
	}
}

