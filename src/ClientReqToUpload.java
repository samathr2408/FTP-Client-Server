
import java.io.*;
import java.net.*;

class ClientReqToUpload{
	private Socket socket;
	//private BufferedReader in;
	private PrintWriter out;
	public static int upbytes=0;
	private String fileName;
	private int clientID;
	public ClientReqToUpload(InetAddress addr, int PORT, int id, File[] listOfFiles, int fileNo) {
		try {
			socket = new Socket(addr, PORT);
			if (fileNo != -1)
				fileName = listOfFiles[fileNo - 1].getName();
			else
				fileName = Constants.UPLOAD_ALL;
			this.clientID = id;
		} catch (IOException e) {
			System.err.println("Socket failed");
		}
		try {
			//in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(Constants.UPLOAD);
			out.println(fileName);
			out.println(Constants.END);
			sendFileToServer(); 
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e2) {
				System.err.println("Socket not closed");
			}
		}
	}
	private void sendFileToServer() throws IOException {
		if(fileName.equals(Constants.UPLOAD_ALL)) {
			sendMultipleFilesToServer();
		}
		else {
			sendSingleFileToServer();
		}
		
	}
	private void sendSingleFileToServer() throws IOException {
		String path = Constants.CLIENTLOCATION+""+clientID+"/"+fileName;
		File file = new File(path);
		Constants.upfiles++;
		//int filesize = (int) file.length();
		OutputStream os = socket.getOutputStream();  
		//System.out.println(filesize);
		int count;
		InputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024 * 16];
		while ((count = in.read(buffer)) > 0)
		{
			os.write(buffer,0,count);
			Constants.upbytes=Constants.upbytes+count;
		}
		
		
		System.out.println("------------UPLOADING-----------\n\n\n\n");
			
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
		
			e.printStackTrace();
		}
		os.flush();
	    os.close();
	    in.close();
	    System.out.println(fileName+" uploaded to the server\n\n\n\n");
		
	}
	
	int getupbytes()
	{
		return upbytes;
	}
	private void sendMultipleFilesToServer() throws IOException {
		File myFile = new File(Constants.CLIENTLOCATION+""+clientID+"/");
        File[] Files = myFile.listFiles();
          
        OutputStream os = socket.getOutputStream();  
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
	
}