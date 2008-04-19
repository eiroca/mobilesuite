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

  private static final String BOUNDARY = "eiroca123XYZ123";
  private static final String BOUNDARY_PRE = "--";
  private static final String CONTENT_TYPE = "Content-type";

  private static final String SEP = "" + BaseApp.CR;

  public static final int MODE_GET = 0;
  public static final int MODE_POST = 1;
  public static final int MODE_MULTIPART = 2;

  public String userAgent = "eIrOcA MIDlet";
  public String acceptLanguage = "en-US";
  public boolean useKeepAlive = false;
  public int mode = HTTPClient.MODE_GET;

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
    switch (mode) {
      case MODE_GET:
        final String postData = getPostData();
        if (url.indexOf('?') > 0) {
          uri = url + '&' + postData;
        }
        else {
          uri = url + '?' + postData;
        }
        break;
      default:
        uri = url;
        break;
    }
    connection = (HttpConnection) Connector.open(uri, Connector.READ_WRITE);
    switch (mode) {
      case MODE_GET:
        connection.setRequestMethod(HttpConnection.GET);
        break;
      default:
        connection.setRequestMethod(HttpConnection.POST);
        break;
    }
    connection.setRequestProperty("User-Agent", userAgent);
    connection.setRequestProperty("Accept-Language", acceptLanguage);
    connection.setRequestProperty("Host", host);
    if (useKeepAlive) {
      connection.setRequestProperty("Connection", "keep-alive");
      connection.setRequestProperty("Keep-Alive", "300");
    }
    switch (mode) {
      case MODE_MULTIPART:
        connection.setRequestProperty(CONTENT_TYPE, "multipart/form-data; boundary=" + HTTPClient.BOUNDARY);
        break;
      case MODE_POST:
        connection.setRequestProperty(CONTENT_TYPE, "application/x-www-form-urlencoded");
        break;
      case MODE_GET:
        break;
    }
    return connection;
  }

  private void sendPost(final HttpConnection connection) throws IOException {
    final String postData = getPostData();
    final OutputStream dos = connection.openOutputStream();
    dos.write(postData.getBytes());
    dos.close();
  }

  public void sendMultipart(final HttpConnection connection) throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    StringBuffer buf;
    if (params.size() > 0) {
      for (int i = 0; i < params.size(); i++) {
        final Pair p = (Pair) params.elementAt(i);
        buf = new StringBuffer(200);
        buf.append(BOUNDARY_PRE).append(HTTPClient.BOUNDARY).append(SEP);
        buf.append("content-disposition: form-data; name=").append('"').append(p.name).append('"').append(SEP);
        buf.append(SEP).append((p.value != null ? p.value : "")).append(SEP);
        out.write(buf.toString().getBytes());
      }
    }
    for (int i = 0; i < attach.size(); i++) {
      final HTTPAttach sendable = (HTTPAttach) attach.elementAt(i);
      final String mimeType = sendable.getMimeType();
      final byte[] data = sendable.getData();
      String name = "file_" + Integer.toString(i);
      buf = new StringBuffer(200);
      buf.append(BOUNDARY_PRE).append(HTTPClient.BOUNDARY).append(SEP);
      buf.append("content-disposition: form-data;");
      buf.append(" name=").append('"').append(name).append('"');
      buf.append("; filename=").append('"').append(name).append('"');
      buf.append(SEP);
      buf.append(CONTENT_TYPE + ": ").append(mimeType).append(SEP).append(SEP);
      out.write(buf.toString().getBytes());
      out.write(data);
      out.write(SEP.getBytes());
    }
    buf = new StringBuffer(200);
    buf.append(BOUNDARY_PRE).append(HTTPClient.BOUNDARY).append(BOUNDARY_PRE).append(SEP);
    out.write(buf.toString().getBytes());
    final OutputStream dos = connection.openOutputStream();
    final byte[] b = out.toByteArray();
    dos.write(b);
    dos.close();
  }

  public void sendGet(final HttpConnection connection) throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    for (int i = 0; i < attach.size(); i++) {
      final HTTPAttach sendable = (HTTPAttach) attach.elementAt(i);
      final String mimeType = sendable.getMimeType();
      final byte[] data = sendable.getData();
      if (i == 0) {
        connection.setRequestProperty(CONTENT_TYPE, mimeType);
      }
      out.write(data);
    }
    final OutputStream dos = connection.openOutputStream();
    final byte[] b = out.toByteArray();
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

  public void execute() {
    result = null;
    setStatus(0);
    try {
      final HttpConnection httpConn = getConnection();
      switch (mode) {
        case MODE_MULTIPART:
          sendMultipart(httpConn);
          break;
        case MODE_POST:
          sendPost(httpConn);
          break;
        case MODE_GET:
          sendGet(httpConn);
          break;
      }
      final int responseCode = httpConn.getResponseCode();
      result = readResult(httpConn);
      setStatus(responseCode);
      httpConn.close();
    }
    catch (final IOException e) {
      result = e.getMessage();
      setStatus(999);
    }
  }

  public void submit(final String url, final boolean async) {
    this.url = url;
    final int first = url.indexOf('/');
    host = url.substring(first + 2, url.indexOf('/', first + 2));
    if (async) {
      new Thread(this).start();
    }
    else {
      execute();
    }
  }

  public void run() {
    execute();
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

  public String getResult() {
    return result;
  }

}
