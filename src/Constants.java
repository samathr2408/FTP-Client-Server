import java.util.HashMap;
import java.util.Map;


public class Constants {
	public static final String SERVERLOCATION = "ServerFiles/";
	public static final String CLIENTLOCATION = "ClientFiles_";
	public static final String RETRIEVE = "RETR";
	public static final String UPLOAD = "UP";
	public static final String END = "END";
	public static final String DOWNLOAD_ALL  = "DownloadAll";
	public static final String UPLOAD_ALL  = "UploadAll";
	public static final int MAX_THREADS = 1;
	public static  int upbytes = 0;
	public static int downbytes=0;
	public static  int upfiles = 0;
	public static int downfiles=0;
	public static final String HOST = "localhost";
	public static final int PORT = 13268;
	public static final Map<String, String> credentials = createMap();
	private static Map<String, String> createMap()
    {
        Map<String,String> credentials = new HashMap<String,String>();
        credentials.put("tagore", "tagore");
        credentials.put("samath", "samath");
        credentials.put("charith", "charith");
        return credentials;
    }
}
