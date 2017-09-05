import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.locks.*;
import java.net.InetAddress;
import java.util.Scanner;
import java.net.*;

public class ClientControl extends Thread {
	private InetAddress addr;
	
	private int port;

	public int id ;
	private static int threadcount = 0;
	private File[] listOfFiles = null;
	ReadWriteLock rwLock=new ReentrantReadWriteLock();
	
	public static int threadCount() {
		return threadcount;
	}

	public ClientControl(InetAddress addr, int PORT,int id) {
		this.id=id;
		System.out.println("Making client " + id);
		System.out.println("In Client " + id + " Control Constructor");
		threadcount++;
		this.addr = addr;
		this.port = PORT;
		//selectMenu();
		try {
			start();
		} catch (Exception e) {
		}
	}
	
	public void run() {
	    try {
	    	selectMenu();
	    } catch(Exception e) {
	      System.err.println("IO Exception");
	    } finally {
	    }
	  }

	private void selectMenu() throws IOException {
		int sel = 0, filetoDownload, filetoUplaod,ch;
		@SuppressWarnings("resource")
		Scanner choice = new Scanner(System.in);
		while (sel == 0) {
			System.out.println("");
			System.out.println("Please select from the below menu for client "
					+ id);
			System.out.println("1 - PWD(Print Server Directory");
			System.out.println("2 - Download Files");
			System.out.println("3 - Upload Files");
			System.out.println("4 - Abort");
			System.out.println("5 - Delete");
			System.out.println("6 - Status");
			System.out.println("7 - NOOP");
			System.out.println();
			System.out.println("Enter your choice");
			sel = choice.nextInt();

			switch (sel) {
			case 1:
				displayFiles(Constants.SERVERLOCATION);
				sel=0;
				break;
			case 2:
				displayFiles(Constants.SERVERLOCATION);
				System.out.println("");
				System.out.println("Enter the file of your choice from the above list or -1 to Download All");
				filetoDownload=choice.nextInt();
				rwLock.readLock().lock();
				new ClientReqToDownload(addr, port, id,listOfFiles,filetoDownload);
				rwLock.readLock().unlock();
				sel = 0;
				break;
			case 3:
				displayFiles(Constants.CLIENTLOCATION+""+id+"/");
				System.out.println("");
				System.out.println("Select file to upload from client "+id+" or -1 to Upload All");
				filetoUplaod = choice.nextInt();
				rwLock.writeLock().lock();
				new ClientReqToUpload(addr, port,id, listOfFiles, filetoUplaod);
				rwLock.writeLock().lock();
				sel = 0;
				break;
			case 4:
				
				break;
			case 5:
				delete();
				//ch=1;
				//Client_operation(addr,13267,ch);
				sel=0;
				break;
			case 6:
				System.out.println("-------Connection Alive---------");
				System.out.println("Total Bytes sent- "+Constants.upbytes);
				System.out.println("Total Bytes recieved- "+Constants.downbytes);
				System.out.println("Total files sent- "+Constants.upfiles);
				System.out.println("Total files recieved- "+Constants.downfiles);
				sel=0;
				break;
			case 7:
				InetAddress addr = InetAddress.getByName(Constants.HOST);
				PrintWriter out;
				Socket sock=null;
				try
				{
				 sock = new Socket(addr,port) ;
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
				 System.out.println("The server is connected!");
				 out.println(Constants.END);
				 out.close();
				 sock.close();
				}
				
				catch(Exception e)
				{
					System.out.println("The server is not connected!");
					
					
				}
				
				sel=0;
				break;
			case 8:
				break;
			case 9:
				break;
			default: // return null;
				System.out.println("You have choosen the wrong menu option");
			}
		}
		
		System.out.println("---------------CLOSING CLIENT CONNECTION-------------------------");
		
	}

	void delete()
	{
		
		
		   int co,i=0;
		   Scanner ch = new Scanner(System.in);
		   String fdel=null;
		
	
		   displayFiles(Constants.CLIENTLOCATION+""+id+"/");
		   System.out.println("Enter the file to delete");
		   co=ch.nextInt();
		   File folder = new File(Constants.CLIENTLOCATION+""+id+"/");
		   listOfFiles = folder.listFiles();
		   
		   for (File file : listOfFiles)
		   {
				 ++i;
				
			if(co==i)
				fdel=file.getName();
			
		    }	
		   
		
        		
		try{

    		File file1 = new File(Constants.CLIENTLOCATION+""+id+"/"+fdel);

    		if(file1.delete()){
    			System.out.println(file1.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}

    	}catch(Exception e){

    		e.printStackTrace();

    	}
		
	}
	
	
	/*private void Client_operation(InetAddress addr, int PORT, int m)
	{
		Socket s=null;
		String Result;
	
		try
		{
			 s=new Socket(addr,port);
			 out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						s.getOutputStream())), true);
			 inputBuff=new BufferedReader(new InputStreamReader(s.getInputStream()));
			 
		} catch (IOException e)
		{
			
		}
		
		switch (m)
		{
		
		case 1:
			out.println("1");
			out.flush();
		try 
			{
				Result=inputBuff.readLine();
				System.out.print(Result);
				
			}
			 catch (IOException e1)
			 {
				
				e1.printStackTrace();
			  }
			
			break;
		case 2:
			break;
			
		default:
			break;
				
		
			
			
		
		}
		try
		{
			s.close();
			out.close();
		} catch (IOException e)
		{
			
			e.printStackTrace();
		}
		
		
	}*/
	private void displayFiles(String loc) 
	{
		File folder = new File(loc);
		listOfFiles = folder.listFiles();
		int i = 0;
			for (File file : listOfFiles)
			{
				System.out.println(++i+" : "+file.getName());
			}	
	}

}
