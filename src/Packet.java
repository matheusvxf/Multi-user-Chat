
public class Packet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer mSource;
	private Integer mDestination;
	
	public final static Integer BROADCAST_ADDRESS = 0xffffffff;

	public enum Type {
		COMMAND,
		EVENT
	}
	
	private Type mType;
	
	public Packet(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}

	public void setType(Type type) {
		mType = type;
	}
	
	public Packet setSource(Integer source){
		mSource = source;
		return this;
	}
	
	public Integer getSource(){
		return mSource;
	}
	
	public Packet setDestination(Integer destination){
		mDestination = destination;
		return this;
	}
	
	public Integer getDestination(){
		return mDestination;
	}
}
