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
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * A class representing an updating thread.
 */

public class FeedUpdateThread extends Thread {

  /** The TA g_ item. */
  private static String TAG_ITEM = "item";

  /** The TA g_ pubdate. */
  private static String TAG_PUBDATE = "pubDate";

  /** The TA g_ title. */
  private static String TAG_TITLE = "title";

  /** The TA g_ description. */
  private static String TAG_DESCRIPTION = "description";

  /** The TA g_ link. */
  private static String TAG_LINK = "link";

  /** The TA g_ image. */
  private static String TAG_IMAGE = "image";

  /** The TA g_ lastbuilddat e1. */
  private static String TAG_LASTBUILDDATE1 = "lastBuildDate";

  /** The TA g_ lastbuilddat e2. */
  private static String TAG_LASTBUILDDATE2 = "dc:date";

  /** The TA g_ inf o1. */
  private static String TAG_INFO1 = "info1";

  /** The MAXSTAT. */
  public int MAXSTAT = 7;

  // The feed that is being updated
  /** The feed. */
  private final RSSFeed feed;
  /*
   * Mapping item titles -> item so we quickly can look up if we already have
   * the item or not when parsing.
   */
  /** The itemtable. */
  private final Hashtable itemtable;

  /** The s status. */
  protected StatusScreen sStatus;

  /* The number of new items that was added at last update */
  /** The newitems. */
  protected int newitems;

  /**
   * Constructor for the class.
   * 
   * @param theFeed the feed that should be updated
   * @param newmidlet the running MIDlet
   */
  public FeedUpdateThread(final RSSFeed theFeed, final NewsReader newmidlet) {
    super();
    newitems = 0;
    feed = theFeed;
    itemtable = buildItemTable();
    sStatus = new StatusScreen(Application.messages[NewsReader.MSG_UPDATETITLE], Application.cOK, NewsReader.cSTOP, null);
    sStatus.init(Application.messages[NewsReader.MSG_UPDATEST00], MAXSTAT);
    start();
  }

  /**
   * Parse1.
   * 
   * @param parser the parser
   * @throws XmlPullParserException the xml pull parser exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws InterruptedException the interrupted exception
   */
  private void parse1(final KXmlParser parser) throws XmlPullParserException, IOException, InterruptedException {
    String tmp;
    // skip <?xml>
    parser.nextTag();
    // skip <rss>
    parser.nextTag();
    // skip <channel>
    parser.nextTag();
    // Go through tags about the channel
    String tagName = parser.getName();
    while (!FeedUpdateThread.TAG_ITEM.equals(tagName) && (parser.getEventType() != XmlPullParser.END_DOCUMENT)) {
      if (parser.getEventType() == XmlPullParser.START_TAG) {
        if ((FeedUpdateThread.TAG_LASTBUILDDATE1.equals(tagName) || FeedUpdateThread.TAG_LASTBUILDDATE2.equals(tagName))) {
          tmp = parser.nextText();
          if (tmp != null) {
            feed.lastBuildDate = tmp;
          }
        }
        else if (FeedUpdateThread.TAG_DESCRIPTION.equals(tagName)) {
          tmp = parser.nextText();
          if (tmp != null) {
            if (NewsReader.stgUseHTML) {
              feed.description = tmp;
            }
            else {
              feed.description = BaseApp.removeHtml(tmp);
            }
          }
        }
        else if (FeedUpdateThread.TAG_TITLE.equals(tagName) && feed.title.equals("")) {
          // Set the title of the feed if its nothing
          tmp = parser.nextText();
          if (tmp != null) {
            feed.title = parser.nextText();
          }
        }
      }
      parser.next();
      tagName = parser.getName();
    }
  }

  /**
   * Parse2.
   * 
   * @param parser the parser
   * @param parsetime the parsetime
   * @return the int
   * @throws XmlPullParserException the xml pull parser exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws InterruptedException the interrupted exception
   */
  private int parse2(final KXmlParser parser, final long parsetime) throws XmlPullParserException, IOException, InterruptedException {
    // We have found the first <item>
    int items = 0;
    boolean tmp_bool;
    String tagname;
    String text;
    RSSItem tmpitem;
    if (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
      do {
        tmpitem = new RSSItem();
        // For every item
        parser.require(XmlPullParser.START_TAG, null, FeedUpdateThread.TAG_ITEM);
        while (parser.nextTag() != XmlPullParser.END_TAG) {
          // Go through all the tags within <item>
          // Modifica per corriere
          if (FeedUpdateThread.TAG_INFO1.equals(parser.getName())) {
            tmp_bool = true;
            while (tmp_bool) {
              if (parser.nextTag() == XmlPullParser.END_TAG) {
                if (FeedUpdateThread.TAG_INFO1.equals(parser.getName())) {
                  tmp_bool = false;
                }
              }
            }
            parser.nextTag();
          }
          // Fine modifiche per corriere
          parser.require(XmlPullParser.START_TAG, null, null);
          tagname = parser.getName();
          text = parser.nextText();
          if (text != null) {
            if (FeedUpdateThread.TAG_PUBDATE.equals(tagname)) {
              tmpitem.pubDate = text;
            }
            else if (FeedUpdateThread.TAG_TITLE.equals(tagname)) {
              tmpitem.title = text;
            }
            else if (FeedUpdateThread.TAG_LINK.equals(tagname)) {
              tmpitem.link = text;
            }
            else if (FeedUpdateThread.TAG_IMAGE.equals(tagname)) {
              tmpitem.image = text;
            }
            else if (FeedUpdateThread.TAG_DESCRIPTION.equals(tagname)) {
              tmpitem.description = text;
            }
          }
          parser.require(XmlPullParser.END_TAG, null, tagname);
        }
        parser.require(XmlPullParser.END_TAG, null, FeedUpdateThread.TAG_ITEM);
        // Add the item if it's new (All items have titles)
        if (tmpitem.title != null) {
          items++;
          if (itemtable.get(tmpitem.title) == null) {
            newitems++;
            // A bit ugly hack for correct item ordering
            tmpitem.parseTime = parsetime - items;
            feed.addItem(tmpitem);
          }
        }
      }
      while ((parser.nextTag() != XmlPullParser.END_TAG) && FeedUpdateThread.TAG_ITEM.equals(parser.getName()));
    }
    return items;
  }

  /**
   * Method that starts running in a separate thread when UpdateThread.start() is called. This is where the downloading, parsing and item adding takes place. The method checkForInterrupt() is called
   * every now and then to see if the user wants to abort the update.
   */
  public void run() {
    String donetext = "";
    final Object[] o = new Object[9];
    HTTPClient data = null;
    try {
      updateStatus(Application.messages[NewsReader.MSG_UPDATEST01], 1);
      newitems = 0;
      updateStatus(Application.messages[NewsReader.MSG_UPDATEST02], 2);
      data = new HTTPClient(feed);
      updateStatus(Application.messages[NewsReader.MSG_UPDATEST03], 3);
      data.open();
      if (data.resCode == HttpConnection.HTTP_NOT_MODIFIED) {
        /* Feed has not been updated */
        donetext = Application.format(NewsReader.MSG_UPDATEOK01, new Object[] {
          feed.title
        });
      }
      else {
        updateStatus(Application.messages[NewsReader.MSG_UPDATEST04], 4);
        final KXmlParser parser = new KXmlParser();
        parser.setInput(new InputStreamReader(data.istream));
        parse1(parser);
        updateStatus(Application.messages[NewsReader.MSG_UPDATEST05], 5);
        final long parsetime = System.currentTimeMillis();
        final int items = parse2(parser, parsetime);
        updateStatus(Application.messages[NewsReader.MSG_UPDATEST06], 6);
        /* Save the feed */
        feed.lastUpdateTime = parsetime;
        if (newitems > 0) {
          RSSFeed.save(feed, true);
        }
        o[0] = feed.title;
        o[1] = new Integer(newitems);
        o[2] = new Integer((items - newitems));
        donetext = Application.format(NewsReader.MSG_UPDATEOK02, o);
        if (feed.lastFeedLen > 0) {
          o[0] = new Integer((int) (feed.lastFeedLen / 1024));
          donetext += BaseApp.CR + Application.format(NewsReader.MSG_UPDATEOK03, o);
        }
      }
    }
    catch (final IllegalArgumentException e) {
      o[0] = feed.URL;
      donetext = Application.format(NewsReader.MSG_UPDATEERR01, o);
    }
    catch (final XmlPullParserException e) {
      o[0] = feed.URL;
      donetext = Application.format(NewsReader.MSG_UPDATEERR02, o);
    }
    catch (final InterruptedException e) {
      o[0] = feed.URL;
      donetext = Application.format(NewsReader.MSG_UPDATEERR03, o);
    }
    catch (final Exception e) {
      o[0] = feed.URL;
      donetext = Application.format(NewsReader.MSG_UPDATEERR04, o);
    }
    finally {
      sStatus.setStatus(Application.messages[NewsReader.MSG_UPDATEST07], MAXSTAT);
      sStatus.append(donetext);
      sStatus.done();
      if (data != null) {
        data.close();
      }
    }
  }

  /**
   * Checks if the user want to abort the update. If so, the <code>donetext</code> is set.
   * 
   * @param status current status
   * @param statusvalue current status value (between <code>0</code> and <code>MAXSTAT</code>)
   * @return if the updating should be interrupted
   * @throws InterruptedException the interrupted exception
   */
  private void updateStatus(final String status, final int statusvalue) throws InterruptedException {
    sStatus.setStatus(status, statusvalue);
    if (sStatus.stopped) { throw new InterruptedException(); }
  }

  /**
   * Builds and return a Hashtable with the mapping item title -> item.
   * @return Hashtable with mapping item title -> item
   */
  private Hashtable buildItemTable() {
    if (feed == null) { return null; }
    final int size = feed.getItemsCount();
    final Hashtable itemtable = new Hashtable(size + 10);
    RSSItem item;
    for (int i = 0; i < size; i++) {
      item = feed.getItem(i);
      itemtable.put(item.title, item);
    }
    return itemtable;
  }

}
