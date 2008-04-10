package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class DataSender extends Thread {

  Suite suite;
  String url;
  String version;
  String result = null;

  public DataSender(Suite suite, String url, String version) {
    this.suite = suite;
    this.url = url;
    this.version = version;
  }

  public void run() {
    System.out.println("RUN");
    try {
      postData();
    }
    catch (IOException e) {
      result = "error " + e.getMessage();
    }
    System.out.println(result);
  }

  public void postData() throws IOException {
    HttpConnection httpConn = null;
    InputStream is = null;
    OutputStream os = null;
    try {
      // Open an HTTP Connection object
      httpConn = (HttpConnection) Connector.open(url);
      // Setup HTTP Request to POST
      httpConn.setRequestMethod(HttpConnection.POST);
      httpConn.setRequestProperty("User-Agent", "eIrOcA TestSuite");
      httpConn.setRequestProperty("Accept_Language", "en-US");
      //Content-Type is must to pass parameters in POST Request
      httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      // This function retrieves the information of this connection
      getConnectionInformation(httpConn);
      os = httpConn.openOutputStream();
      suite.export(os, version);
      os.write('\n');
      // Read Response from the Server
      StringBuffer sb = new StringBuffer();
      is = httpConn.openDataInputStream();
      int chr;
      while ((chr = is.read()) != -1) {
        sb.append((char) chr);
      }
      result = sb.toString();
      // Web Server just returns the birthday in mm/dd/yy format.
    }
    finally {
      if (is != null) is.close();
      if (os != null) os.close();
      if (httpConn != null) httpConn.close();
    }
  }

  void getConnectionInformation(HttpConnection hc) {
    System.out.println("Request Method for this connection is " + hc.getRequestMethod());
    System.out.println("URL in this connection is " + hc.getURL());
    System.out.println("Protocol for this connection is " + hc.getProtocol()); // It better be HTTP:)
    System.out.println("This object is connected to " + hc.getHost() + " host");
    System.out.println("HTTP Port in use is " + hc.getPort());
    System.out.println("Query parameter in this request are  " + hc.getQuery());
  }

}
