import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Runtime r = Runtime.getRuntime();
		r.exec("C:\\cygwin\\bin\\cygstart java -classpath bin/ Server");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		r.exec("C:\\cygwin\\bin\\cygstart java -classpath bin/ Client matheus");
		r.exec("C:\\cygwin\\bin\\cygstart java -classpath bin/ Client miles");		
	}
}
