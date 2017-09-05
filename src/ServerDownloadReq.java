import java.io.*;
import java.net.*;

class ServerDownloadReq extends Thread {
	private Socket soc;
	//private PrintWriter out;
	private String path;
	public ServerDownloadReq(Socket s, String path) throws IOException {
		this.path = path;
		soc = s;
		//out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
		start(); // Calls run()
	}

	public void run() 
	{
		try
		{
			send(path);
			
		} 
		catch (IOException e) 
		{
			System.err.println("IO Exception");
		} 
		finally {
			try 
			{
				soc.close();
			} 
			catch (IOException e)
			{
				System.err.println("Socket not closed");
			}
		}
	}

	private void send(String path) throws IOException
	{
			if(path.equals(Constants.DOWNLOAD_ALL)) 
			{
				sendMultipleFiles();
			}
			else 
			{
				sendSingleFile();
			}
	}

	private void sendMultipleFiles() throws IOException
	{
		File myFile = new File(Constants.SERVERLOCATION);
        File[] Files = myFile.listFiles();
          
        OutputStream os = soc.getOutputStream();  
        DataOutputStream dos = new DataOutputStream(os); 
        
        dos.writeInt(Files.length);
         
        for (int count=0;count<Files.length;count ++){
              dos.writeUTF(Files[count].getName());
               
        }
        for (int count=0;count<Files.length;count ++){
               
              int filesize = (int) Files[count].length();
              dos.writeInt(filesize);
        }
         
        for (int count=0;count<Files.length;count ++){
         
        int filesize = (int) Files[count].length();
        byte [] buffer = new byte [filesize];
             
        //FileInputStream fis = new FileInputStream(myFile);  
        FileInputStream fis = new FileInputStream(Files[count].toString());  
        BufferedInputStream bis = new BufferedInputStream(fis);  
     
        //Sending file name and file size to the server  
        bis.read(buffer, 0, buffer.length); //This line is important
         
        dos.write(buffer, 0, buffer.length);   
        dos.flush(); 
        //dos.close();
        }  
		
	}

	private void sendSingleFile() throws IOException
	{
		System.out.println("Recieved file request----\n\n\n\n");
		
		
		
		File file = new File(Constants.SERVERLOCATION+""+path);
		int filesize = (int) file.length();
		OutputStream os = soc.getOutputStream();  
		System.out.println(filesize);
		int count;
		InputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024 * 16];
		
		while ((count = in.read(buffer)) > 0)
		{
			os.write(buffer,0,count);
		}
		
		System.out.println("File sent\n\n\n\n");
		os.flush();
	    os.close();
	    in.close();
	    
		
	}
	
}