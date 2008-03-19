/** GPL >= 2.0
 *
 * Copyright (c) 2004 Ang Kok Chai
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
package net.eiroca.j2me.rms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.eiroca.j2me.app.BaseApp;

public class Settings {

  private static final String RMS_PROPERTIES = "properties";
  private static final Hashtable properties = new Hashtable();

  /**
   * Load properties
   */
  public static void load() {
    Settings.properties.clear();
    final RecordStore rs = BaseApp.getRecordStore(Settings.RMS_PROPERTIES, true, true);
    try {
      final RecordEnumeration e = rs.enumerateRecords(null, null, false);
      if (e.hasNextElement()) {
        final byte[] data = e.nextRecord();
        final DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(data));
        for (int i = dataIn.readInt() - 1; i >= 0; i--) {
          Settings.properties.put(dataIn.readUTF(), dataIn.readUTF());
        }
        dataIn.close();
      }
    }
    catch (final Exception e) {
      //
    }
  }

  /**
   * Set property.
   * @param name
   * @param value
   */
  public static void put(final String name, final String value) {
    if (value != null) {
      Settings.properties.put(name, value);
    }
    else {
      Settings.properties.remove(name);
    }
  }

  /**
   * @param name
   * @return
   */
  public static String get(final String name) {
    return (String) Settings.properties.get(name);
  }

  /**
   * Get property count.
   * @return
   */
  public static int size() {
    return Settings.properties.size();
  }

  /**
   * Save properties into RecordStore.
   * @return
   */
  public static boolean save() {
    try {
      final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      final DataOutputStream dataOut = new DataOutputStream(byteOut);
      dataOut.writeInt(Settings.properties.size());
      final Enumeration keys = Settings.properties.keys();
      while (keys.hasMoreElements()) {
        final String key = keys.nextElement().toString();
        dataOut.writeUTF(key);
        dataOut.writeUTF(Settings.properties.get(key).toString());
      }
      final byte[] data = byteOut.toByteArray();
      byteOut.close();
      dataOut.close();
      try {
        final RecordStore rs = BaseApp.getRecordStore(Settings.RMS_PROPERTIES, true, true);
        final RecordEnumeration e = rs.enumerateRecords(null, null, false);
        if (e.hasNextElement()) {
          rs.setRecord(e.nextRecordId(), data, 0, data.length);
        }
        else {
          rs.addRecord(data, 0, data.length);
        }
        return true;
      }
      catch (final RecordStoreException e) {
        //
      }
    }
    catch (final IOException e) {
      //
    }
    return false;
  }

}
