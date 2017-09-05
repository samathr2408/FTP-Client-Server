import java.io.*;
import java.net.*;
import java.util.*;

class ClientReqToDownload {
	private Socket socket;
	// private BufferedReader in;
	private PrintWriter out;
	public static int downbytes=0;
	private int clientID;
	private String fileName;

	public ClientReqToDownload(InetAddress addr, int PORT, int id,File[] listOfFiles, int fileNo) 
	{
		try
		{
			socket = new Socket(addr, PORT);
			this.clientID = id;
			if (fileNo != -1)
				fileName = listOfFiles[fileNo - 1].getName();
			else
				fileName = Constants.DOWNLOAD_ALL;
		} 
		catch (IOException e) 
		{
			System.err.println("Socket failed");
		}
		
		try {
			// in = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			out.println(Constants.RETRIEVE);
			out.println(fileName);
			createFile();
		    } 
		
   catch (IOException e) {
			   
	          try {
				socket.close();
			      } 
	          
			    catch (IOException e2)
	          {
				System.err.println("Socket not closed");
			  }
		}
	}

	private void createFile() throws IOException {
		if (fileName.equals(Constants.DOWNLOAD_ALL)) {
			createMultipleFiles();
		} else {
			createSingleFile();
		}
	}
	
	int getupbytes()
	{
		return downbytes;
	}

	private void createMultipleFiles() throws IOException {
		int len;
		int smblen;
		boolean flag = true;
		OutputStream os;
		DataOutputStream dos;
		BufferedOutputStream bos;
		
		while(flag==true)
		{  
			InputStream is = socket.getInputStream(); // used
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

				os = new FileOutputStream(Constants.CLIENTLOCATION + clientID
						+ "/" + files.get(count));
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

	private void createSingleFile() throws IOException 
	{
		File file = new File(Constants.CLIENTLOCATION + clientID + "/"
				+ fileName);
		OutputStream os = null;
		Constants.downfiles++;

		InputStream is = socket.getInputStream();
		DataInputStream clientData = new DataInputStream(is);
		os = new FileOutputStream(file);
		byte[] bytes = new byte[1024];
		int count;
		
		System.out.println("------------DOWNLOADING-----------\n\n\n\n");
		
		
		while ((count = clientData.read(bytes)) > 0)
		{
			Constants.downbytes=Constants.downbytes+count;
			os.write(bytes, 0, count);
		}
		
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
		
			e.printStackTrace();
		}
		
		System.out.println(fileName+" downloaded complete\n\n\n\n");

		os.close();
		is.close();
		
	}
}