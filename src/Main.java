import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Runtime r = Runtime.getRuntime();
		r.exec("C:\\cygwin\\bin\\cygstart /bin/bash -a=\"java C:\\Users\\Matheus\\Documents\\workspace\\Chat\\bin\\Server\"");
		r.exec("java Client");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
