
public class IDNotifyEvent extends Event {
	private static final long serialVersionUID = 1L;
	private int mID;
	
	public IDNotifyEvent() {
		super(Event.Type.ID_NOTIFY);
		// TODO Auto-generated constructor stub
	}

	public int getID() {
		return mID;
	}

	public void setID(int iD) {
		mID = iD;
	}

}
