


public class NewClientEvent extends Event
{
	private static final long serialVersionUID = 1L;
	private int mID;
	private String mName;
	
	NewClientEvent(){
		super(Event.Type.NEW_CLIENT);
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String mName) {
		this.mName = mName;
	}
	
	public int getID() {
		return mID;
	}
	
	public void setID(int iD) {
		mID = iD;
	}		
}