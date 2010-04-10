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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.RMSInfo;

/**
 * A class representing an RSS item. (A feed consists of items)
 */
public class RSSItem {

  /** The Constant ITEM_BYTE_ARRAY_LEN. */
  private static final int ITEM_BYTE_ARRAY_LEN = 1024;

  /** The Constant ITEM_BYTE_ARRAY_INC. */
  private static final int ITEM_BYTE_ARRAY_INC = 256;

  /* The feed id in the RMS that this item belongs to */
  /** The feed id. */
  public int feedID = -1;
  /* This items id in the RMS */
  /** The item id. */
  public int itemID = -1;
  /* Parsed time in milliseconds since January 1, 1970 UTC */
  /** The parse time. */
  public long parseTime;

  /** The title. */
  public String title = "";

  /** The description. */
  public String description = "";

  /** The pub date. */
  public String pubDate = "";

  /** The link. */
  public String link = "";

  /** The image. */
  public String image = "";

  /** The is read. */
  public boolean isRead = false;

  /**
   * Constructor for the class.
   */
  public RSSItem() {
    parseTime = System.currentTimeMillis();
  }

  /**
   * Returns this item as a <code>byte[]</code> so this item could be recreated by using it.
   * 
   * @param bout the ByteArrayOutputStream that should be used. Gets reset at first
   * @param dout the DataOutputStream that should be used. Gets flushed after all data is written to it.
   * @return this item as a
   */
  private byte[] getBytes(final ByteArrayOutputStream bout, final DataOutputStream dout) {
    try {
      bout.reset();
      // Print the Item info
      dout.writeInt(feedID);
      dout.writeLong(parseTime);
      dout.writeBoolean(isRead);
      dout.writeUTF(title);
      dout.writeUTF(pubDate);
      dout.writeUTF(image);
      dout.writeUTF(link);
      dout.writeUTF(description);
      // Finish
      dout.flush();
      return bout.toByteArray();
    }
    catch (final Exception e) {
      return null;
    }
  }

  /**
   * Recreates a RSSItem from the given data as a <code>byte[]</code>.
   * @param data the item as a <code>byte[]</code>
   * @param datalen the number of bytes in data we should parse
   * @param itemid the RMS id for this item
   * @return the parsed item
   */
  private static RSSItem parseItem(final byte[] data, final int datalen, final int itemid) {
    try {
      final RSSItem item = new RSSItem();
      item.itemID = itemid;
      final ByteArrayInputStream bin = new ByteArrayInputStream(data, 0, datalen);
      final DataInputStream din = new DataInputStream(bin);
      // Read the Item info
      item.feedID = din.readInt();
      item.parseTime = din.readLong();
      item.isRead = din.readBoolean();
      item.title = din.readUTF();
      item.pubDate = din.readUTF();
      item.image = din.readUTF();
      item.link = din.readUTF();
      item.description = din.readUTF();
      // Finish
      din.close();
      bin.close();
      return item;
    }
    catch (final Exception e) {
      return null;
    }
  }

  /**
   * Saves the given feed and its items.
   * 
   * @param item the item
   * @return the RMS id of the item if it was inserted in the RMS, otherwise -1.
   */
  public static int save(final RSSItem item) {
    ByteArrayOutputStream bout = null;
    DataOutputStream dout = null;
    if (item == null) { return -1; }
    bout = new ByteArrayOutputStream();
    dout = new DataOutputStream(bout);
    try {
      // Create stream objects
      // Store feed data
      byte[] data;
      // Store item data
      final RecordStore rs = RSSItem.rs_open(item.feedID);
      data = item.getBytes(bout, dout);
      if (item.itemID == -1) {
        // Save a new item
        item.itemID = rs.addRecord(data, 0, data.length);
      }
      else {
        // Overwrite item
        rs.setRecord(item.itemID, data, 0, data.length);
      }
      RSSItem.rs_close(false);
      return item.feedID;
    }
    catch (final Exception e) {
      return -1;
    }
    finally {
      try {
        // Close streams
        if ((dout != null) && (bout != null)) {
          dout.close();
          bout.close();
        }
      }
      catch (final Exception e) {
        //
      }
    }
  }

  /**
   * Load items.
   * 
   * @param feed the feed
   */
  public static void loadItems(final RSSFeed feed) {
    // Get ALL the items associated with this feed
    byte[] record = new byte[RSSItem.ITEM_BYTE_ARRAY_LEN];
    feed.nn = 0;
    feed.nr = 0;
    try {
      final RecordStore itemrs = RSSItem.rs_open(feed.feedID);
      final RecordEnumeration renum = itemrs.enumerateRecords(null, null, false);
      int reclen;
      int itemid = -1;
      while (renum.hasNextElement()) {
        itemid = renum.nextRecordId();
        reclen = itemrs.getRecordSize(itemid);
        if (reclen > record.length) {
          record = new byte[reclen + RSSItem.ITEM_BYTE_ARRAY_INC];
        }
        reclen = itemrs.getRecord(itemid, record, 0);
        final RSSItem item = RSSItem.parseItem(record, reclen, itemid);
        feed.addItem(item);
      }
      renum.destroy();
      RSSItem.rs_close(false);
    }
    catch (final RecordStoreException e) {
    }
  }

  /**
   * Delete all.
   * 
   * @param feedID the feed id
   * @return the int
   */
  public static int deleteAll(final int feedID) {
    int siz = 0;
    try {
      final RecordStore rs = RSSItem.rs_open(feedID);
      siz = rs.getNumRecords();
      RSSItem.rs_delete(feedID);
    }
    catch (final RecordStoreException e) {
    }
    return siz;
  }

  /**
   * Deletes ALL items.
   * @return A string describing how much was deleted. For example: "Deleted 10 items"
   */
  public static int deleteAll() {
    int siz = 0;
    final int[] ids = RSSFeed.getIDs();
    if (ids != null) {
      int recordid;
      for (int i = 0; i < ids.length; i++) {
        recordid = ids[i];
        siz += RSSItem.deleteAll(recordid);
      }
    }
    return siz;
  }

  /**
   * Deletes ALL items matching the given ItemFilter.
   * @param ff The filter the items should be matched with.
   * @return A string describing how much was deleted. For example: "Deleted 10 items" If an error occurs, "Deleted 0 items" will be returned.
   */
  public static int deleteItems(final ItemFilter ff) {
    int siz = 0;
    final int[] ids = RSSFeed.getIDs();
    if (ids != null) {
      int recordid;
      for (int i = 0; i < ids.length; i++) {
        recordid = ids[i];
        siz += RSSItem.deleteItems(recordid, ff);
      }
    }
    return siz;
  }

  /**
   * Delete items.
   * 
   * @param feedID the feed id
   * @param ff the ff
   * @return the int
   */
  public static int deleteItems(final int feedID, final ItemFilter ff) {
    int items = 0;
    try {
      // Try to open the Recordstore, if it fails it doesn't exist = no problem!
      final RecordStore rs = RSSItem.rs_open(feedID);
      final RecordEnumeration renum = rs.enumerateRecords(ff, null, false);
      while (renum.hasNextElement()) {
        rs.deleteRecord(renum.nextRecordId());
        items++;
      }
      renum.destroy();
      RSSItem.rs_close(false);
    }
    catch (final Exception e) {
      //
    }
    return items;
  }

  /**
   * Gets the info.
   * 
   * @param feedID the feed id
   * @return the info
   */
  public static RMSInfo getInfo(final int feedID) {
    final RMSInfo info = new RMSInfo();
    BaseApp.getRecordStoreInfo(RSSItem.RS_RSSITEMS_ + feedID, info);
    return info;
  }

  /** The Constant RS_RSSITEMS_. */
  private static final String RS_RSSITEMS_ = "RSSItems";

  /** The rs. */
  private static RecordStore rs = null;

  /** The rs_id. */
  private static int rs_id = -1;

  /**
   * Rs_open.
   * 
   * @param id the id
   * @return the record store
   */
  public static synchronized RecordStore rs_open(final int id) {
    if (id != RSSItem.rs_id) {
      RSSItem.rs_close(true);
    }
    if (RSSItem.rs == null) {
      try {
        RSSItem.rs_id = id;
        RSSItem.rs = RecordStore.openRecordStore(RSSItem.RS_RSSITEMS_ + id, true);
      }
      catch (final RecordStoreException e) {
      }
    }
    return RSSItem.rs;
  }

  /**
   * Rs_close.
   * 
   * @param force the force
   */
  public static synchronized void rs_close(final boolean force) {
    if (force) {
      if (RSSItem.rs != null) {
        try {
          RSSItem.rs.closeRecordStore();
        }
        catch (final RecordStoreException e) {
        }
        RSSItem.rs = null;
        RSSItem.rs_id = -1;
      }
    }
  }

  /**
   * Rs_delete.
   * 
   * @param id the id
   */
  public static synchronized void rs_delete(final int id) {
    RSSItem.rs_close(true);
    try {
      RecordStore.deleteRecordStore(RSSItem.RS_RSSITEMS_ + id);
    }
    catch (final RecordStoreException e) {
    }
  }

}
