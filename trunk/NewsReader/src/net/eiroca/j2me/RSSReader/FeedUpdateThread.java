/** GPL >= 2.0
 * Based upon RSS Reader MIDlet
 * Copyright (C) 2004 Gösta Jonasson <gosta(at)brothas.net>
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.eiroca.j2me.RSSReader;

import NewsReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import net.eiroca.j2me.app.BaseApp;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * A class representing an updating thread.
 */

public class FeedUpdateThread extends Thread {

  private static String TAG_ITEM = "item";
  private static String TAG_PUBDATE = "pubDate";
  private static String TAG_TITLE = "title";
  private static String TAG_DESCRIPTION = "description";
  private static String TAG_LINK = "link";
  private static String TAG_IMAGE = "image";
  private static String TAG_LASTBUILDDATE1 = "lastBuildDate";
  private static String TAG_LASTBUILDDATE2 = "dc:date";
  private static String TAG_INFO1 = "info1";

  private static String HEADER_LASTMOD = "Last-Modified";
  private static String HEADER_IFNONE = "If-None-Match";
  private static String HEADER_ETAG = "ETag";

  public int MAXSTAT = 7;

  // The feed that is being updated
  private final RSSFeed feed;
  /*
   * Mapping item titles -> item so we quickly can look up if we already have
   * the item or not when parsing.
   */
  private final Hashtable itemtable;

  protected StatusScreen sStatus;

  /* The number of new items that was added at last update */
  protected int newitems;

  /**
   * Constructor for the class.
   * @param theFeed the feed that should be updated
   * @param newmidlet the running MIDlet
   * @param cl1 the object that receives the STOP/Interrupt command
   * @param cl2 the object that receives the OK command
   */
  public FeedUpdateThread(final RSSFeed theFeed, final NewsReader newmidlet) {
    super();
    newitems = 0;
    feed = theFeed;
    itemtable = buildItemTable();
    sStatus = new StatusScreen(BaseApp.messages[NewsReader.MSG_UPDATETITLE], BaseApp.cOK, NewsReader.cSTOP, null);
    sStatus.init(BaseApp.messages[NewsReader.MSG_UPDATEST00], MAXSTAT);
    start();
  }

  /**
   * Method that starts running in a separate thread when UpdateThread.start()
   * is called. This is where the downloading, parsing and item adding takes
   * place. The method checkForInterrupt() is called every now and then to see
   * if the user wants to abort the update.
   */
  public void run() {
    String donetext = "";
    HttpConnection httpconnection = null;
    InputStream istream = null;
    boolean tmp_bool;
    final Object[] o = new Object[9];
    try {
      int items = 0;
      newitems = 0;
      String tagname, text;
      updateStatus(BaseApp.messages[NewsReader.MSG_UPDATEST01], 1);
      // Connect to the url of the feed. Shouldn't it be true???
      httpconnection = (HttpConnection) Connector.open(feed.URL, Connector.READ_WRITE, false);
      /*
       * Perhaps do a conditional GET request Last-Modified and ETag
       * If-Modified-Since If-None-Match 304 Not Modified HTTP_NOT_MODIFIED
       */
      if (!feed.serverLastModified.equals("")) {
        httpconnection.setRequestMethod(HttpConnection.GET);
        httpconnection.setRequestProperty(FeedUpdateThread.HEADER_LASTMOD, feed.serverLastModified);
        httpconnection.setRequestProperty(FeedUpdateThread.HEADER_IFNONE, feed.servereTag);
      }
      updateStatus(BaseApp.messages[NewsReader.MSG_UPDATEST02], 2);
      istream = httpconnection.openInputStream();
      updateStatus(BaseApp.messages[NewsReader.MSG_UPDATEST03], 3);

      final KXmlParser parser = new KXmlParser();
      parser.setInput(new InputStreamReader(istream));
      if (httpconnection.getResponseCode() == HttpConnection.HTTP_NOT_MODIFIED) {
        /* Feed has not been updated */
        donetext = BaseApp.format(NewsReader.MSG_UPDATEOK01, new Object[] {
          feed.title
        });
      }
      else {
        String tmp;
        tmp = httpconnection.getHeaderField(FeedUpdateThread.HEADER_LASTMOD);
        if (tmp != null) {
          feed.serverLastModified = tmp;
        }
        tmp = httpconnection.getHeaderField(FeedUpdateThread.HEADER_ETAG);
        if (tmp != null) {
          feed.servereTag = tmp;
        }
        feed.lastFeedLen = httpconnection.getLength();
        updateStatus(BaseApp.messages[NewsReader.MSG_UPDATEST04], 4);
        parser.nextTag();
        // <rss> parser.require(parser.START_TAG, null, RSS_TAG);
        parser.nextTag();
        // <channel> parser.require(parser.START_TAG, null, CHANNEL_TAG);
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
        // We have found the first <item>
        RSSItem tmpitem;
        final long parsetime = System.currentTimeMillis();
        updateStatus(BaseApp.messages[NewsReader.MSG_UPDATEST05], 5);
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
        updateStatus(BaseApp.messages[NewsReader.MSG_UPDATEST06], 6);
        /* Save the feed */
        feed.lastUpdateTime = parsetime;
        if (newitems > 0) {
          RSSFeed.save(feed, true);
        }
        o[0] = feed.title;
        o[1] = new Integer(newitems);
        o[2] = new Integer((items - newitems));
        donetext = BaseApp.format(NewsReader.MSG_UPDATEOK02, o);
        if (feed.lastFeedLen > 0) {
          o[0] = new Integer((int) (feed.lastFeedLen / 1024));
          donetext += BaseApp.CR + BaseApp.format(NewsReader.MSG_UPDATEOK03, o);
        }
      }
    }
    catch (final IllegalArgumentException e) {
      o[0] = feed.URL;
      donetext = BaseApp.format(NewsReader.MSG_UPDATEERR01, o);
    }
    catch (final XmlPullParserException e) {
      o[0] = feed.URL;
      donetext = BaseApp.format(NewsReader.MSG_UPDATEERR02, o);
    }
    catch (final InterruptedException e) {
      o[0] = feed.URL;
      donetext = BaseApp.format(NewsReader.MSG_UPDATEERR03, o);
    }
    catch (final Exception e) {
      o[0] = feed.URL;
      donetext = BaseApp.format(NewsReader.MSG_UPDATEERR04, o);
    }
    finally {
      sStatus.setStatus(BaseApp.messages[NewsReader.MSG_UPDATEST07], MAXSTAT);
      sStatus.append(donetext);
      sStatus.done();
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
    }
  }

  /**
   * Checks if the user want to abort the update. If so, the
   * <code>donetext</code> is set.
   * @param status current status
   * @param statusvalue current status value (between <code>0</code> and
   *            <code>MAXSTAT</code>)
   * @return <code>TRUE</code> if the updating should be interrupted
   * @throws InterruptedException
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
