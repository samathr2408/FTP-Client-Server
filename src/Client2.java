import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client2 {
	
	private static Scanner keyboard;
    
	public static void main(String[] args) throws IOException,
			InterruptedException {
		int id=2;
		if(authenticate()) {
			InetAddress addr = InetAddress.getByName(Constants.HOST);
			while (true) {
				if (ClientControl.threadCount() < Constants.MAX_THREADS)
					new ClientControl(addr, Constants.PORT,id);
				Thread.currentThread().sleep(10000);
			}
		}
	}

	//Authenticate User Clients
	private static boolean authenticate() {
		keyboard = new Scanner(System.in);
		Boolean flag = false;
		String userName;
		String password;
		int InvalidAttemps = 0;
		while (!flag && InvalidAttemps < 3) {
			if(InvalidAttemps > 0)
				System.out.println("Attempts Remaining : " + (3-InvalidAttemps));
			System.out.print("Enter user name\n");
			userName = keyboard.next();
			System.out.print("Enter password\n");
			password = keyboard.next();

			if(Constants.credentials.containsKey(userName) && Constants.credentials.get(userName).equalsIgnoreCase(password)){
				System.out.println("Login Successful");
				flag = true;
				return flag;
			}
			else {
				InvalidAttemps++;
				System.err.println("Invalid Login Attempt");
			}
		}
	return flag;
	}
}