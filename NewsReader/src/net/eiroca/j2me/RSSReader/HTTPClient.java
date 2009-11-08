package net.eiroca.j2me.RSSReader;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import net.eiroca.j2me.app.BaseApp;

public class HTTPClient {

  private static String HEADER_LASTMOD = "Last-Modified";
  private static String HEADER_IFNONE = "If-None-Match";
  private static String HEADER_ETAG = "ETag";
  private static String HEADER_LOCATION = "Location";

  HttpConnection httpconnection = null;
  InputStream istream = null;
  IOException lastErr = null;
  int resCode = 0;
  RSSFeed feed;
  String baseURL;
  String lastURL;

  /**
   * Returns the URL of the redirected webpage.
   * 
   * <code>baseURL</code> is the requested HTTP URL and <code>
     * location</code> is the HTTP Location Header value.
   * 
   * @param baseURL The requested HTTP URL.
   * @param location The Location Header value.
   * 
   * @return An absolute URL that corresponds to the target URL.
   */
  public static String redirectURL(final String baseURL, final String location) {
    if (location.startsWith("http://")) {
      return location;
    }
    else if (location.startsWith("/")) {
      return "http://" + getHost(baseURL) + location;
    }
    else {
      if (baseURL.endsWith("/")) {
        return baseURL + location;
      }
      else {
        return baseURL + "/" + location;
      }
    }
  }

  /**
   * Returns the host part of the URL, without the trailing slash. http://jorgecardoso.org/ - returns jorgecardoso.org
   * @param URL The URL to get the host part from. Must start with http://
   * 
   * @return The host part of the URL.
   */
  public static String getHost(final String URL) {
    if (!URL.startsWith("http://")) { throw new java.lang.IllegalArgumentException("URL must start with 'http://'"); }
    final String n = URL.substring(7); // strip http://
    final int slashPos = n.indexOf('/');
    if (slashPos == -1) { // no slash, so this is the host only
      return n;
    }
    else {
      return n.substring(0, slashPos);
    }
  }

  /**
   * Indicates whether this URL has a file part.
   * 
   * @param URL The URL to check for the file part.
   * 
   * @return true if this URL has a file part, false otherwise.
   */
  public static boolean hasFile(String URL) {
    URL = URL.trim();
    if (!URL.startsWith("http://")) { throw new java.lang.IllegalArgumentException("URL must start with 'http://'"); }
    final String n = URL.substring(7); // strip http://
    final int lastSlashPos = n.lastIndexOf('/');
    if (lastSlashPos == -1) { // no slash, so no file
      return false;
    }
    return (lastSlashPos < n.length());
  }

  public HTTPClient(final RSSFeed feed) {
    this.feed = feed;
  }

  public void close() {
    try {
      if (istream != null) {
        istream.close();
      }
      if (httpconnection != null) {
        httpconnection.close();
      }
    }
    catch (final Exception e) {
      //
    }
    istream = null;
    httpconnection = null;
  }

  public void open() throws IOException {
    baseURL = feed.URL;
    lastURL = baseURL;
    int redirCount = 5;
    // Connect to the URL of the feed. Shouldn't it be true???
    while (redirCount > 0) {
      httpconnection = (HttpConnection) Connector.open(lastURL, Connector.READ_WRITE, false);
      /*
       * Perhaps do a conditional GET request Last-Modified and ETag
       * If-Modified-Since If-None-Match 304 Not Modified HTTP_NOT_MODIFIED
       */
      if (!BaseApp.isEmpty(feed.serverLastModified)) {
        httpconnection.setRequestMethod(HttpConnection.GET);
        httpconnection.setRequestProperty(HTTPClient.HEADER_LASTMOD, feed.serverLastModified);
        httpconnection.setRequestProperty(HTTPClient.HEADER_IFNONE, feed.serverETag);
      }
      istream = httpconnection.openInputStream();
      resCode = httpconnection.getResponseCode();
      if ((resCode == HttpConnection.HTTP_MOVED_PERM) || (resCode == HttpConnection.HTTP_MOVED_TEMP)) {
        String location = httpconnection.getHeaderField(HTTPClient.HEADER_LOCATION);
        lastURL = redirectURL(lastURL, location);
        if (resCode == HttpConnection.HTTP_MOVED_PERM) {
          baseURL = lastURL;
        }
        redirCount--;
        close();
      }
      else {
        break;
      }
    }
    if (httpconnection.getResponseCode() != HttpConnection.HTTP_NOT_MODIFIED) {
      String tmp;
      tmp = httpconnection.getHeaderField(HTTPClient.HEADER_LASTMOD);
      if (tmp != null) {
        feed.serverLastModified = tmp;
      }
      tmp = httpconnection.getHeaderField(HTTPClient.HEADER_ETAG);
      if (tmp != null) {
        feed.serverETag = tmp;
      }
      feed.lastFeedLen = httpconnection.getLength();
    }
  }

}
