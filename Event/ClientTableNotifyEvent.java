import java.util.Hashtable;


public class ClientTableNotifyEvent extends Event{
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Integer> mNameTable;

	public ClientTableNotifyEvent() {
		super(Event.Type.CLIENT_TABLE_NOTIFY);
	}

	public Hashtable<String, Integer> getNameTable() {
		return mNameTable;
	}

	public void setNameTable(Hashtable<String, Integer> nameTable) {
		mNameTable = nameTable;
	}
}
