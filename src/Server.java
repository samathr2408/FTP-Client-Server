import java.io.*;
import java.net.*;

public class Server {
    public static void main (String[] args) throws IOException {
    ServerSocket s = new ServerSocket(Constants.PORT);
    System.out.println("Server Started at port "+Constants.PORT);
    BufferedReader in;
    try {
    	
      while (true)
      {  

        Socket  socket  =  s.accept();
        try
        {
        	
        	System.out.println("Waiting for incoming connection...\n\n\n\n");
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	
        	
        	while (true)
        	{
				String str = in.readLine();
				if (str.equals(Constants.END))
					break;
				if (str.equals(Constants.RETRIEVE))
					 new  ServerDownloadReq (socket,in.readLine());
				if (str.equals(Constants.UPLOAD))
					new  ServerUploadReq (socket,in.readLine(),socket.getInputStream());
				
				
					
			}
         
        }
        
        catch (IOException e)
        {
          socket.close ();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            socket.close ();
         }
      }
    } finally {
      s.close ();
    }
  }
}