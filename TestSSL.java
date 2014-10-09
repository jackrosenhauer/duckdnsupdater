import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestSSL {

    public static void main(String[] args) throws Exception {
    
	//Default Domain
	String domain = "your duckdns subdomain here";
	//Default Token
	String token = "get your token from duckdns";
	if (args.length <= 1){
	    token = args[0];
	    System.out.println("Token set to: " + token + " and status: ");
	}
	 if (args.length <= 2){
	    token = args[0];
	    domain = args[1];
	    System.out.print("Domain set to: " + domain + ", token set to: " + token + "  and status: ");
	}else{
	   System.out.println("Usage: ./<name> <token> <domain>");
	}
        
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //	System.out.println("checkClientTrusted certs: " + certs.toString() + ", authType: " + authType + ".\n");
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //	System.out.println("checkServerTrusted certs: " + certs.toString() + ", authType: " + authType + ".\n");
            }
        } };
        
        
        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
            //	System.out.println("HostnameVerified:verify:: hostname: " + hostname + ", SSLSession: " + session +".");
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        URL url = new URL("https://www.duckdns.org/update?domains=" + domain + "&token=" + token + "&ip=");
        URLConnection con = url.openConnection();
        final Reader reader = new InputStreamReader(con.getInputStream());
        final BufferedReader br = new BufferedReader(reader);        
        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
	    if (!line.equals("OK")){
	       System.out.println("thats not okay.. shame shame");
	    }
        }        
        br.close();
    } // End of main 
} // End of the class //
