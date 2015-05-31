

public class Command implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public enum Type {
		SET_CLIENT_NAME,
		SEND_MESSAGE
	}
	
	Type mType;
	
	public Command(Type type){
		mType = type;
	}
	
	public static String toString(Type type){
		switch(type){
		case SET_CLIENT_NAME:
			return "SET_CLIENT_MESSAGE";
		case SEND_MESSAGE:
			return "SEND_MESSAGE";
		default:
			return "unknow";
		}
	}
}

