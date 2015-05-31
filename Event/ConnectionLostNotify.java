
public class ConnectionLostNotify extends Event {
	private static final long serialVersionUID = 1L;
	private int mID;	
	
	public ConnectionLostNotify() {
		this(-1);
	}
	
	public ConnectionLostNotify(int ID){
		super(Event.Type.CONNECTION_LOST_NOTIFY);
		mID = ID;
	}

	public int getID() {
		return mID;
	}

	public void setID(int iD) {
		mID = iD;
	}

}
