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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.Comparator;
import net.eiroca.j2me.app.Pair;
import net.eiroca.j2me.app.RMSInfo;

/**
 * A class representing a RSS feed.
 */
public class RSSFeed implements Comparator {

  // In minutes between updates
  public final int STANDARD_UPDATE = 60;
  public static final String WEB_ITEM = "W";

  // RMS ID
  public int feedID = -1;
  // DATA
  public String title = "";
  public String description = "";
  public String URL = "";
  public String lastBuildDate = "";
  public long lastUpdateTime = 0;
  public long lastFeedLen = 0;
  public int minsBetweenUpdates = STANDARD_UPDATE;
  public String serverLastModified = "";
  public String servereTag = "";
  public String type = RSSFeed.WEB_ITEM;
  public int nn = 0;
  public int nr = 0;
  public int colBckG;
  public int colBord;
  public int colTitl;
  public int colText;

  /* All items for this feed */
  private final Vector items;

  /**
   * Constructor for the class.
   */
  public RSSFeed() {
    colBckG = NewsReader.stgColBckG;
    colBord = NewsReader.stgColBord;
    colText = NewsReader.stgColText;
    colTitl = NewsReader.stgColTitl;
    items = new Vector();
  }

  /**
   * Returns this feed as a <code>byte[]</code> so this feed could be
   * recreated using it. No items are returned!
   * @param bout the ByteArrayOutputStream that should be used. Gets reset at
   *            first
   * @param dout the DataOutputStream that should be used. Gets flushed after
   *            all data is written to it.
   * @return this feed as a <code>byte[]</code>
   */
  private byte[] getBytes(final ByteArrayOutputStream bout, final DataOutputStream dout) {
    try {
      bout.reset();
      dout.writeUTF(title);
      dout.writeUTF(description);
      dout.writeUTF(URL);
      dout.writeUTF(lastBuildDate);
      dout.writeLong(lastUpdateTime);
      dout.writeLong(lastFeedLen);
      dout.writeInt(minsBetweenUpdates);
      dout.writeUTF(serverLastModified);
      dout.writeUTF(servereTag);
      dout.writeUTF(type);
      dout.writeInt(nn);
      dout.writeInt(nr);
      dout.writeInt(colBckG);
      dout.writeInt(colBord);
      dout.writeInt(colTitl);
      dout.writeInt(colText);
      dout.flush();
      return bout.toByteArray();
    }
    catch (final Exception e) {
      return null;
    }
  }

  /**
   * Recreates a RSSFeed from the given data as a <code>byte[]</code>. This
   * includes no items!
   * @param data the feed as a <code>byte[]</code>
   * @param datalen the number of bytes in data we should parse
   * @param id the RMS id for this feed
   * @return the parsed feed
   */
  private static RSSFeed parseFeed(final int id, final byte[] data) {
    try {
      final RSSFeed feed = new RSSFeed();
      final ByteArrayInputStream bin = new ByteArrayInputStream(data);
      final DataInputStream din = new DataInputStream(bin);
      feed.title = din.readUTF();
      feed.description = din.readUTF();
      feed.URL = din.readUTF();
      feed.lastBuildDate = din.readUTF();
      feed.lastUpdateTime = din.readLong();
      feed.lastFeedLen = din.readLong();
      feed.minsBetweenUpdates = din.readInt();
      feed.serverLastModified = din.readUTF();
      feed.servereTag = din.readUTF();
      feed.type = din.readUTF();
      feed.feedID = id;
      feed.nn = din.readInt();
      feed.nr = din.readInt();
      feed.colBckG = din.readInt();
      feed.colBord = din.readInt();
      feed.colTitl = din.readInt();
      feed.colText = din.readInt();
      din.close();
      bin.close();
      return feed;
    }
    catch (final Exception e) {
      return null;
    }
  }

  /**
   * Adds the given item to the feed.
   * @param item item to be added to the feed
   */
  public void addItem(final RSSItem item) {
    item.feedID = feedID;
    items.addElement(item);
    nn++;
    if (!item.isRead) {
      nr++;
    }
  }

  /**
   * Returns the number of items.
   * @return the number of items
   */
  public int getItemsCount() {
    return items.size();
  }

  /**
   * Returns the item with the given internal id
   * @param itemid the id of the item
   * @return the item with the given id. <code>NULL</code> if an error occurs
   *         or if no item exists with that title.
   */
  public RSSItem getItem(final int itemid) {
    try {
      return (RSSItem) items.elementAt(itemid);
    }
    catch (final Exception e) {
      return null;
    }
  }

  public int compare(final Object o1, final Object o2) {
    final RSSItem i1 = (RSSItem) o1;
    final RSSItem i2 = (RSSItem) o2;
    if (i1.parseTime < i2.parseTime) {
      return 1;
    }
    else if (i1.parseTime > i2.parseTime) {
      return -1;
    }
    else {
      return 0;
    }
  }

  public void sortItems() {
    BaseApp.sort(items, this);
  }

  /**
   * Looks up all the feeds in the RMS, creates and returns a Hashtable which
   * maps Feed title -> Feed RMS id.
   * @return a Hashtable which maps Feed title -> Feed RMS id
   */
  public static Vector getIndex(final Comparator c) {
    try {
      final RecordStore feedrs = RSSFeed.rs_open();
      if (feedrs.getNumRecords() == 0) { return null; }
      // Sort these with a FeedComparator?
      final RecordEnumeration renum = feedrs.enumerateRecords(null, null, false);
      final Vector lutable = new Vector(feedrs.getNumRecords());
      ByteArrayInputStream bin;
      DataInputStream din;
      int recordid = 0;
      // Build the lookup hashtable
      while (renum.hasNextElement()) {
        recordid = renum.nextRecordId();
        bin = new ByteArrayInputStream(feedrs.getRecord(recordid));
        din = new DataInputStream(bin);
        final Pair p = new Pair();
        p.name = din.readUTF();
        p.value = new Integer(recordid);
        lutable.addElement(p);
        bin.close();
        din.close();
      }
      // Clean up
      renum.destroy();
      RSSFeed.rs_close(false);
      if (c != null) {
        BaseApp.sort(lutable, c);
      }
      return lutable;
    }
    catch (final Exception e) {
      return null;
    }
  }

  public void read(final RSSItem item) {
    if (!item.isRead) {
      item.isRead = true;
      RSSItem.save(item);
      nr--;
      RSSFeed.save(this, false);
    }
  }

  public static int[] getIDs() {
    try {
      final RecordStore feedrs = RSSFeed.rs_open();
      if (feedrs.getNumRecords() == 0) { return null; }
      // Sort these with a FeedComparator?
      final RecordEnumeration renum = feedrs.enumerateRecords(null, null, false);
      final int[] lutable = new int[feedrs.getNumRecords()];
      int recordid = 0;
      int i = 0;
      // Build the lookup hashtable
      while (renum.hasNextElement()) {
        recordid = renum.nextRecordId();
        lutable[i] = recordid;
        i++;
      }
      // Clean up
      renum.destroy();
      RSSFeed.rs_close(false);
      return lutable;
    }
    catch (final Exception e) {
      return null;
    }
  }

  /**
   * Loads the feed (and its items) with the given RMS id.
   * @param feedID the RMS id of the feed
   * @return the feed with the given RMS id
   */
  public static RSSFeed load(final int feedID) {
    byte[] record = null;
    // Get the feed data
    try {
      final RecordStore feedrs = RSSFeed.rs_open();
      record = feedrs.getRecord(feedID);
      RSSFeed.rs_close(false);
    }
    catch (final RecordStoreException e) {
      return null;
    }
    // Create a feed object
    final RSSFeed feed = RSSFeed.parseFeed(feedID, record);
    if (feed == null) { return null; }
    RSSItem.loadItems(feed);
    return feed;
  }

  /**
   * Saves the given feed and its items.
   * @param feed the feed that should be saved
   * @return the RMS id of the item if it was inserted in the RMS, otherwise -1.
   */
  public static int save(final RSSFeed feed, final boolean full) {
    ByteArrayOutputStream bout = null;
    DataOutputStream dout = null;
    if (feed == null) { return -1; }
    bout = new ByteArrayOutputStream();
    dout = new DataOutputStream(bout);
    try {
      // Create stream objects
      // Store feed data
      final RecordStore feedrs = RSSFeed.rs_open();
      final byte[] data = feed.getBytes(bout, dout);
      // Insert or update the feed
      if (feed.feedID == -1) {
        feed.feedID = feedrs.addRecord(data, 0, data.length);
      }
      else {
        feedrs.setRecord(feed.feedID, data, 0, data.length);
      }
      // Store it in the RMS
      RSSFeed.rs_close(false);
      // Store item data
      if (full) {
        final int nrofitems = feed.getItemsCount();
        RSSItem item;
        for (int i = 0; i < nrofitems; i++) {
          item = feed.getItem(i);
          RSSItem.save(item);
        }
      }
      return feed.feedID;
    }
    catch (final Exception e) {
      return -1;
    }
    finally {
      try {
        // Close streams
        if (dout != null) {
          dout.close();
        }
        if (bout != null) {
          bout.close();
        }
      }
      catch (final Exception e) {
        //
      }
    }
  }

  /**
   * Deletes the given feed BUT NOT its items.
   * @param feed the feed that should be deleted
   * @return <code>TRUE</code> if the feed was deleted, <code>FALSE</code>
   *         otherwise
   */
  public static boolean deleteFeed(final int feedID) {
    if (feedID < 0) { return false; }
    try {
      RSSItem.deleteAll(feedID);
      final RecordStore rs = RSSFeed.rs_open();
      rs.deleteRecord(feedID);
      RSSFeed.rs_close(false);
      return true;
    }
    catch (final Exception e) {
      return false;
    }
  }

  /**
   * Deletes the given RecordStore and returns the number of records deleted. If
   * the RecordStore not existed, 0 is returned.
   * @param rstore The name of the RecordStore that should be deleted.
   * @return number of records deleted
   */
  public static int deleteAll() {
    int items = 0;
    try {
      // Try to open the Recordstore, if it fails it doesn't exist = no problem!
      final RecordStore rs = RSSFeed.rs_open();
      items = rs.getNumRecords();
      RSSFeed.rs_delete();
      return items;
    }
    catch (final Exception e) {
      return items;
    }
  }

  public static RMSInfo getInfo() {
    final RMSInfo info = new RMSInfo();
    BaseApp.getRecordStoreInfo(RSSFeed.RS_RSSFEEDS, info);
    return info;
  }

  private static final String RS_RSSFEEDS = "RSSFeeds";
  private static RecordStore rs = null;

  public static synchronized RecordStore rs_open() {
    if (RSSFeed.rs == null) {
      try {
        RSSFeed.rs = RecordStore.openRecordStore(RSSFeed.RS_RSSFEEDS, true);
      }
      catch (final RecordStoreException e) {
      }
    }
    return RSSFeed.rs;
  }

  public static synchronized void rs_close(final boolean force) {
    if (force) {
      if (RSSFeed.rs != null) {
        try {
          RSSFeed.rs.closeRecordStore();
        }
        catch (final RecordStoreException e) {
        }
        RSSFeed.rs = null;
      }
    }
  }

  public static synchronized void rs_delete() {
    RSSFeed.rs_close(true);
    try {
      RecordStore.deleteRecordStore(RSSFeed.RS_RSSFEEDS);
    }
    catch (final RecordStoreException e) {
    }
  }

}
