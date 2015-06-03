import java.io.IOException;

public class Main {
	private static String OS = null;
	private static String SHELL = "gnome-terminal";

	public static void main(String[] args) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		if(isWindows()){
			SHELL = "C:\\cygwin\\bin\\cygstart";
		}
		
		runtime.exec(SHELL + " java -classpath bin/ Server");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		runtime.exec(SHELL + " java -classpath bin/ Client miles");
		runtime.exec(SHELL + " java -classpath bin/ Client miles");
	}

	public static String getOsName() {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}

	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}
}
