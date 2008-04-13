package net.eiroca.j2me.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.Pair;
import net.eiroca.j2me.observable.Observable;
import net.eiroca.j2me.observable.Observer;
import net.eiroca.j2me.observable.ObserverManager;

public class HTTPClient implements Observable, Runnable {

  public static final String BOUNDARY = "---eiroca---123XYZ123---eiroca---";
  //  public static final String NL = "\r\n";
  public static final String NL = "\n";

  public String userAgent = "eIrOcA MIDlet";
  public String acceptLanguage = "en-US";
  public boolean usePost = false;
  public boolean useKeepAlive = false;
  public boolean useMultipart = false;

  public String result = null;
  public int status = -1;

  private String url;
  private String host;
  private final Vector params = new Vector();
  private final Vector attach = new Vector();
  private final ObserverManager manager = new ObserverManager();

  public HTTPClient() {
  }

  public void clear() {
    params.removeAllElements();
    attach.removeAllElements();
  }

  public void addParameter(final String parameter, final String value) {
    final Pair p = new Pair();
    p.name = parameter;
    p.value = BaseApp.URLEncode(value);
    params.addElement(p);
  }

  public void addAttach(final HTTPAttach data) {
    attach.addElement(data);
    useMultipart = true;
  }

  public String getPostData() {
    final StringBuffer postData = new StringBuffer(100);
    if (params.size() > 0) {
      boolean first = true;
      for (int i = 0; i < params.size(); i++) {
        final Pair p = (Pair) params.elementAt(i);
        if (!first) {
          postData.append('&');
        }
        postData.append(BaseApp.URLEncode(String.valueOf(p.name)));
        if (p.value != null) {
          postData.append('=').append(p.value);
        }
        first = false;
      }
    }
    return postData.toString();
  }

  private HttpConnection getConnection() throws IOException {
    HttpConnection connection = null;
    String uri;
    if (!usePost) {
      final String postData = getPostData();
      if (url.indexOf('?') > 0) {
        uri = url + '&' + postData;
      }
      else {
        uri = url + '?' + postData;
      }
    }
    else {
      uri = url;
    }
    connection = (HttpConnection) Connector.open(uri, Connector.READ_WRITE);
    if (usePost) {
      connection.setRequestMethod(HttpConnection.POST);
    }
    else {
      connection.setRequestMethod(HttpConnection.GET);
    }
    connection.setRequestProperty("User-Agent", userAgent);
    connection.setRequestProperty("Accept-Language", acceptLanguage);
    connection.setRequestProperty("Host", host);
    if (useKeepAlive) {
      connection.setRequestProperty("Connection", "keep-alive");
      connection.setRequestProperty("Keep-Alive", "300");
    }
    if (useMultipart) {
      connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + HTTPClient.BOUNDARY);
    }
    else {
      connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
    }
    return connection;
  }

  private void sendPostData(final HttpConnection connection) throws IOException {
    final String postData = getPostData();
    final OutputStream dos = connection.openOutputStream();
    dos.write(postData.getBytes());
    dos.close();
  }

  public void writePostData(final ByteArrayOutputStream byteOut) throws IOException {
    if (params.size() > 0) {
      StringBuffer buf = new StringBuffer(200);
      for (int i = 0; i < params.size(); i++) {
        final Pair p = (Pair) params.elementAt(i);
        buf = new StringBuffer(200);
        buf.append(HTTPClient.BOUNDARY).append(HTTPClient.NL);
        buf.append("Content-Disposition: form-data; name=").append('"').append(p.name).append('"').append(HTTPClient.NL);
        buf.append(HTTPClient.NL).append((p.value != null ? p.value : "")).append(HTTPClient.NL);
        byteOut.write(buf.toString().getBytes());
      }
    }
  }

  public void sendData(final HttpConnection connection) throws IOException {
    final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    writePostData(byteOut);
    StringBuffer buf;
    for (int i = 0; i < attach.size(); i++) {
      final HTTPAttach sendable = (HTTPAttach) attach.elementAt(i);
      final String mimeType = sendable.getMimeType();
      final byte[] data = sendable.getData();
      buf = new StringBuffer(200);
      buf.append(HTTPClient.BOUNDARY).append(HTTPClient.NL);
      buf.append("Content-Disposition: form-data;");
      buf.append(" name=\"file_").append(i).append("\"").append(HTTPClient.NL);
      buf.append("Content-Type: ").append(mimeType).append(HTTPClient.NL).append(HTTPClient.NL);
      byteOut.write(buf.toString().getBytes());
      byteOut.write(data);
      byteOut.write(HTTPClient.NL.getBytes());
    }
    final OutputStream dos = connection.openOutputStream();
    final byte[] b = byteOut.toByteArray();
    System.out.println(new String(b));
    dos.write(b);
    dos.close();
  }

  private String readResult(final HttpConnection connection) throws IOException {
    InputStream dis;
    final StringBuffer buf = new StringBuffer(1024);
    dis = connection.openDataInputStream();
    int chr;
    while ((chr = dis.read()) != -1) {
      buf.append((char) chr);
    }
    dis.close();
    return buf.toString();
  }

  public void run() {
    result = null;
    setStatus(0);
    try {
      final HttpConnection httpConn = getConnection();
      if (useMultipart) {
        sendData(httpConn);
      }
      else {
        if (usePost) {
          sendPostData(httpConn);
        }
      }
      final int responseCode = httpConn.getResponseCode();
      result = readResult(httpConn);
      setStatus(responseCode);
      System.out.println(result);
    }
    catch (final IOException e) {
      result = e.getMessage();
      setStatus(999);
    }
  }

  public void submit(final String url) {
    this.url = url;
    final int first = url.indexOf('/');
    host = url.substring(first + 2, url.indexOf('/', first + 2));
    new Thread(this).start();
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(final int status) {
    this.status = status;
    manager.notifyObservers(this);
  }

  public ObserverManager getObserverManager() {
    return manager;
  }

  public void addObserver(final Observer observer) {
    manager.addObserver(observer);
  }

  public void removeObserver(final Observer observer) {
    manager.removeObserver(observer);
  }

}
