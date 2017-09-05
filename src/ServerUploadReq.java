import java.io.*;
import java.net.*;
import java.util.ArrayList;

class ServerUploadReq extends Thread {
	private Socket soc;
	//private PrintWriter out;
	InputStream is;
	private String fileName;
	public ServerUploadReq(Socket s, String fileName, InputStream is) throws IOException {
		soc = s;
		this.is = is;
		this.fileName = fileName;
		//out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
		start(); // Calls run()
	}

	public void run()
	
	{
		try 
		{
			uploadtoServer();
			
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

	private void uploadtoServer() throws IOException {
		if(fileName.equals(Constants.UPLOAD_ALL)) {
			uploadMultipleFiles();
		}
		else
			uploadSingleFile();
	}

	private void uploadSingleFile() throws IOException 
	{
		System.out.println("Recieveing "+fileName+" from client\n\n\n ");
		
		String path = Constants.SERVERLOCATION+""+fileName;
		File file = new File(path);
		OutputStream os = null;
		
		DataInputStream clientData  = new DataInputStream(is);
		os = new FileOutputStream(file);
	    byte[] bytes = new byte[1024];
	    int count;
	    while((count = clientData.read(bytes))>0)
	    {
	    	os.write(bytes, 0, count);
	    }
	    
	    System.out.println("Upload Complete");
	    
	    os.close();
	    is.close();
		
	}

	private void uploadMultipleFiles() throws IOException 
	{
		int len;
		int smblen;
		boolean flag = true;
		OutputStream os;
		DataOutputStream dos;
		BufferedOutputStream bos;
		
		while(flag==true)
		{  
			DataInputStream clientData = new DataInputStream(is); // use

			System.out.println("Starting...");

			int fileSize = clientData.read();

			 ArrayList<File>files=new ArrayList<File>(fileSize); //store list of filename from client directory
             ArrayList<Integer>sizes = new ArrayList<Integer>(fileSize); //store file size from client
			// Start to accept those filename from server
			for (int count = 0; count < fileSize; count++) {
				File ff = new File(clientData.readUTF());
				files.add(ff);
			}

			for (int count = 0; count < fileSize; count++) {

				sizes.add(clientData.readInt());
			}

			for (int count = 0; count < fileSize; count++) {

				if (fileSize - count == 1) {
					flag = false;
				}

				len = sizes.get(count);

				System.out.println("File Size =" + len);

				os = new FileOutputStream(Constants.SERVERLOCATION+""+files.get(count));
				dos = new DataOutputStream(os);
				bos = new BufferedOutputStream(os);

				byte[] buffer = new byte[len];

				bos.write(buffer, 0, buffer.length); // This line is important

				while (len > 0 && (smblen = clientData.read(buffer)) > 0) {
					dos.write(buffer, 0, smblen);
					len = len - smblen;
					dos.flush();
				}
				
				dos.close(); // It should close to avoid continue deploy by
								// resource under view
			}
		}
		
	}
}