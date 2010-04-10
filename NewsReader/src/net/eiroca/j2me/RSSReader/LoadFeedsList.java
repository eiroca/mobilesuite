/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2004 Gösta Jonasson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package net.eiroca.j2me.RSSReader;

import NewsReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Displayable;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * The Class LoadFeedsList.
 */
public class LoadFeedsList extends Thread {

  /** The Constant TAG_FEEDS. */
  private static final String TAG_FEEDS = "feeds";

  /** The Constant TAG_FEEDDEF. */
  private static final String TAG_FEEDDEF = "feed";

  /** The Constant TAG_TITLE. */
  private static final String TAG_TITLE = "title";

  /** The Constant TAG_URL. */
  private static final String TAG_URL = "URL";

  /** The Constant TAG_TYPE. */
  private static final String TAG_TYPE = "type";

  /** The Constant TAG_COLOR. */
  private static final String TAG_COLOR = "color";

  /** The Constant ATR_BCKG. */
  private static final String ATR_BCKG = "background";

  /** The Constant ATR_BORD. */
  private static final String ATR_BORD = "border";

  /** The Constant ATR_TITL. */
  private static final String ATR_TITL = "title";

  /** The Constant ATR_TEXT. */
  private static final String ATR_TEXT = "text";

  /** The loaded. */
  public boolean loaded;

  /** The a url. */
  String aURL;

  /** The remote. */
  boolean remote;

  /** The midlet. */
  NewsReader midlet;

  /** The s status. */
  StatusScreen sStatus;

  /**
   * Instantiates a new load feeds list.
   * 
   * @param midlet the midlet
   * @param aURL the a url
   * @param remote the remote
   * @param d the d
   */
  public LoadFeedsList(final NewsReader midlet, final String aURL, final boolean remote, final Displayable d) {
    this.midlet = midlet;
    this.aURL = aURL;
    this.remote = remote;
    if (remote) {
      sStatus = new StatusScreen(Application.messages[NewsReader.MSG_LOADFEEDLISTTITLE], NewsReader.cOK2, NewsReader.cSTOP, d);
      sStatus.init(Application.messages[NewsReader.MSG_LOADFEEDLISTST00], 3);
      start();
      loaded = false;
    }
    else {
      final InputStream istream = getClass().getResourceAsStream("/" + aURL);
      try {
        parse(istream);
      }
      catch (final Exception e) {
        midlet.browseFeedList.removeAllElements();
      }
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Thread#run()
   */
  public void run() {
    HttpConnection httpconnection = null;
    InputStream is = null;
    try {
      sStatus.setStatus(Application.messages[NewsReader.MSG_LOADFEEDLISTST01], 1);
      httpconnection = (HttpConnection) Connector.open(aURL, Connector.READ, false);
      is = httpconnection.openInputStream();
      sStatus.setStatus(Application.messages[NewsReader.MSG_LOADFEEDLISTST02], 2);
      parse(is);
      sStatus.setStatus(Application.messages[NewsReader.MSG_LOADFEEDLISTST03], 3);
      sStatus.append(Application.messages[NewsReader.MSG_LOADFEEDLISTEND]);
    }
    catch (final Exception e) {
      midlet.browseFeedList.removeAllElements();
    }
    finally {
      try {
        sStatus.done();
        if (is != null) {
          is.close();
        }
        if (httpconnection != null) {
          httpconnection.close();
        }
      }
      catch (final Exception e) {
        //
      }
    }
    loaded = true;
  }

  /**
   * Parses the.
   * 
   * @param is the is
   * @throws XmlPullParserException the xml pull parser exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void parse(final InputStream is) throws XmlPullParserException, IOException {
    RSSFeed feed;
    String tagname;
    final KXmlParser parser = new KXmlParser();
    parser.setInput(new InputStreamReader(is));
    parser.nextTag();
    parser.require(XmlPullParser.START_TAG, null, LoadFeedsList.TAG_FEEDS);
    if (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
      parser.nextTag();
      do {
        feed = new RSSFeed();
        // For every item
        parser.require(XmlPullParser.START_TAG, null, LoadFeedsList.TAG_FEEDDEF);
        while (parser.nextTag() != XmlPullParser.END_TAG) {
          // Go through all the tags within <item>
          parser.require(XmlPullParser.START_TAG, null, null);
          tagname = parser.getName();
          if (LoadFeedsList.TAG_TITLE.equals(tagname)) {
            feed.title = parser.nextText();
          }
          else if (LoadFeedsList.TAG_URL.equals(tagname)) {
            feed.URL = parser.nextText();
          }
          else if (LoadFeedsList.TAG_TYPE.equals(tagname)) {
            feed.type = parser.nextText();
          }
          else if (LoadFeedsList.TAG_COLOR.equals(tagname)) {
            feed.colBckG = BaseApp.val(parser.getAttributeValue(null, LoadFeedsList.ATR_BCKG), feed.colBckG, 16);
            feed.colBord = BaseApp.val(parser.getAttributeValue(null, LoadFeedsList.ATR_BORD), feed.colBord, 16);
            feed.colTitl = BaseApp.val(parser.getAttributeValue(null, LoadFeedsList.ATR_TITL), feed.colTitl, 16);
            feed.colText = BaseApp.val(parser.getAttributeValue(null, LoadFeedsList.ATR_TEXT), feed.colText, 16);
            parser.nextText();
          }
          else {
            parser.nextText();
          }
          parser.require(XmlPullParser.END_TAG, null, tagname);
        }
        parser.require(XmlPullParser.END_TAG, null, LoadFeedsList.TAG_FEEDDEF);
        // Add the item if it's new (All items have titles)
        if (!BaseApp.isEmpty(feed.URL) && !BaseApp.isEmpty(feed.title) && !BaseApp.isEmpty(feed.type)) {
          midlet.browseFeedList.addElement(feed);
        }
      }
      while ((parser.nextTag() != XmlPullParser.END_TAG) && LoadFeedsList.TAG_FEEDDEF.equals(parser.getName()));
    }

  }
}
