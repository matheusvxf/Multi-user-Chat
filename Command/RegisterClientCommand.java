
public class RegisterClientCommand extends Command {
	private static final long serialVersionUID = 1L;
	private String mName;
	private Integer mID;
	
	public RegisterClientCommand() {
		super(Type.REGISTER_CLIENT);
		mID = 0x00;
		mName = "unknow";
	}
	
	public RegisterClientCommand setName(String name){
		mName = name;
		return this;
	}

	public String getName() {
		return mName;
	}
	
	public RegisterClientCommand setID(Integer ID){
		mID = ID;
		return this;
	}
	
	public Integer getID(){
		return mID;
	}
}
