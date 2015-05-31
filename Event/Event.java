public class Event extends Packet {
	private static final long serialVersionUID = 1L;
	
	public enum Type{
		NEW_CLIENT,
		ID_NOTIFY,
		CLIENT_TABLE_NOTIFY,
		CONNECTION_LOST_NOTIFY
	}
	
	private Type mEventType;

	public Event(Type type) {
		super(Packet.Type.EVENT);
		mEventType = type;
	}

	public Type getEventType() {
		return mEventType;
	}

	public void setEventType(Type type) {
		mEventType = type;
	}
}
