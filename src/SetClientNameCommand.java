
public class SetClientNameCommand extends Command {
	private static final long serialVersionUID = 1L;
	private String mName;
	private Integer mID;
	
	public SetClientNameCommand() {
		super(Type.SET_CLIENT_NAME);
		mID = 0x00;
		mName = "unknow";
	}
	
	public SetClientNameCommand setName(String name){
		mName = name;
		return this;
	}

	public String getName() {
		return mName;
	}
	
	public SetClientNameCommand setID(Integer ID){
		mID = ID;
		return this;
	}
	
	public Integer getID(){
		return mID;
	}
}
