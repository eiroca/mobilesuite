/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002 Eugene Morozov
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
package net.eiroca.j2me.sm.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.eiroca.j2me.app.BaseApp;

/**
 * This is the superclass for all storage implementations.
 */
public class Store {

  /** The Constant MESSAGE_ENCODING. */
  private static final String MESSAGE_ENCODING = "UTF-8";

  /** The natural order. */
  public static RecordComparator naturalOrder = new StoreComparatorByID();

  /**
   * Decode data.
   * 
   * @param data the data
   * @return the string
   */
  public static String decodeData(final byte[] data) {
    String msg;
    try {
      msg = new String(data, Store.MESSAGE_ENCODING);
    }
    catch (final UnsupportedEncodingException uee) {
      msg = new String(data);
    }
    return msg;
  }

  /**
   * Encode data.
   * 
   * @param msg the msg
   * @return the byte[]
   */
  public static byte[] encodeData(final String msg) {
    byte[] data;
    try {
      data = msg.getBytes(Store.MESSAGE_ENCODING);
    }
    catch (final UnsupportedEncodingException uee) {
      data = msg.getBytes();
    }
    return data;
  }

  /**
   * Gets the iD.
   * 
   * @param serializedMessage the serialized message
   * @return the iD
   */
  public static final long getID(final byte[] serializedMessage) {
    return BaseApp.encodeBytesToLong(serializedMessage, 0);
  }

  /** The observers. */
  private final Vector observers = new Vector();
  
  /** The name. */
  protected final String name;
  
  /** The record store. */
  protected RecordStore recordStore;

  /**
   * Instantiates a new store.
   * 
   * @param storeName the store name
   * @throws StoreException the store exception
   */
  public Store(final String storeName) throws StoreException {
    name = storeName;
    try {
      // Get the RecordStore instance for the given name, create the underlying
      // RDS store if needed.
      recordStore = RecordStore.openRecordStore(name, true);
    }
    catch (final RecordStoreException rse) {
      // Rethrow the exception
      throw new StoreException(StoreException.ERR_STOREOPEN);
    }

  }

  /**
   * Register observer.
   * 
   * @param observer the observer
   */
  public void registerObserver(final StoreObserver observer) {
    observers.addElement(observer);
  }

  /**
   * Unregister observer.
   * 
   * @param observer the observer
   */
  public void unregisterObserver(final StoreObserver observer) {
    observers.removeElement(observer);
  }

  /**
   * Notifies all observers of the store about action on the object.
   * 
   * @param action the action
   * @param obj The added object.
   */
  protected void notifyAction(final int action, final Object obj) {
    // Notify each of the observers
    final Enumeration enm = observers.elements();
    while (enm.hasMoreElements()) {
      ((StoreObserver) enm.nextElement()).actionDone(action, obj, this);
    }
  }

  /**
   * List ids.
   * 
   * @param c the c
   * @return the long[]
   * @throws StoreException the store exception
   */
  public long[] listIds(final RecordComparator c) throws StoreException {
    // Request the enumeration
    RecordEnumeration enm = null;
    try {
      // Create the array of IDs
      final long[] ids = new long[recordStore.getNumRecords()];
      int idCounter = 0;
      // Enumerate records
      enm = recordStore.enumerateRecords(null, c, false);
      byte[] what;
      while (enm.hasNextElement()) {
        what = enm.nextRecord();
        ids[idCounter] = Store.getID(what);
        idCounter++;
      }
      return ids;
    }
    catch (final RecordStoreException rse) {
      // Notify the caller
      throw new StoreException(StoreException.ERR_STORELISTIDS);
    }
    finally {
      // Make sure the enumeration destroyed
      if (enm != null) {
        enm.destroy();
      }
    }
  }

  /**
   * Find first.
   * 
   * @param f the f
   * @return the byte[]
   * @throws StoreException the store exception
   */
  public byte[] findFirst(final RecordFilter f) throws StoreException {
    RecordEnumeration enm = null;
    byte[] result = null;
    try {
      enm = recordStore.enumerateRecords(f, null, false);
      if (enm.hasNextElement()) {
        result = enm.nextRecord();
      }
    }
    catch (final RecordStoreException rse) {
      throw new StoreException(StoreException.ERR_STOREFIND);
    }
    finally {
      // Make sure the enumeration destroyed
      if (enm != null) {
        enm.destroy();
      }
    }
    return result;
  }

  // Javadoc inherited from the interface
  /**
   * Removes the first.
   * 
   * @param f the f
   * @return true, if successful
   * @throws StoreException the store exception
   */
  public boolean removeFirst(final RecordFilter f) throws StoreException {
    RecordEnumeration enm = null;
    boolean res = false;
    try {
      enm = recordStore.enumerateRecords(f, null, false);
      if (enm.hasNextElement()) {
        // Replace the record to storage
        recordStore.deleteRecord(enm.nextRecordId());
        res = true;
      }
    }
    catch (final RecordStoreException rse) {
      // Notify the caller
      throw new StoreException(StoreException.ERR_STOREDELETE);
    }
    finally {
      // Make sure the enumeration destroyed
      if (enm != null) {
        enm.destroy();
      }
    }
    return res;
  }

  /**
   * Replace first.
   * 
   * @param f the f
   * @param data the data
   * @throws StoreException the store exception
   */
  public void replaceFirst(final RecordFilter f, final byte[] data) throws StoreException {
    RecordEnumeration enm = null;
    try {
      enm = recordStore.enumerateRecords(f, null, false);
      // Replace the record to storage or add a new one
      if (enm.hasNextElement()) {
        recordStore.setRecord(enm.nextRecordId(), data, 0, data.length);
      }
      else {
        recordStore.addRecord(data, 0, data.length);
      }
    }
    catch (final RecordStoreException rse) {
      // Notify the caller
      throw new StoreException(StoreException.ERR_STORESAVE);
    }
    finally {
      // Make sure the enumeration has been destroyed before the call
      // to the method returns
      if (enm != null) {
        enm.destroy();
      }
    }
  }

  /**
   * Cleanup.
   */
  public void cleanup() {
    try {
      if (recordStore != null) {
        recordStore.closeRecordStore();
      }
      RecordStore.deleteRecordStore(name);
      recordStore = RecordStore.openRecordStore(name, true);
    }
    catch (final RecordStoreException e) {
    }
  }

}
