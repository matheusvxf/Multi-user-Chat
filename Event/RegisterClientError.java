
public class RegisterClientError extends CommandErrorNotification {
	private static final long serialVersionUID = 1L;
	private String mDefaultName;
	
	public RegisterClientError(String name) {
		super(CommandErrorNotification.Type.REGISTER_CLIENT);
		mDefaultName = name;
	}

	public String getDefaultName() {
		return mDefaultName;
	}

	public void setDefaultName(String defaultName) {
		mDefaultName = defaultName;
	}
}
