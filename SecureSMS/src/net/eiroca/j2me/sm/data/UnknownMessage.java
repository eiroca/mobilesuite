/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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
package net.eiroca.j2me.sm.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.eiroca.j2me.sm.util.Store;

/**
 * The Class UnknownMessage.
 */
public class UnknownMessage {

  /** The bout. */
  private final ByteArrayOutputStream bout = new ByteArrayOutputStream();
  
  /** The dout. */
  private final DataOutputStream dout = new DataOutputStream(bout);

  /** The date. */
  public final long date;
  
  /** The number. */
  public String number;
  
  /** The data. */
  public byte[] data;

  /**
   * Instantiates a new unknown message.
   * 
   * @param number the number
   * @param data the data
   */
  public UnknownMessage(final String number, final byte[] data) {
    date = System.currentTimeMillis();
    this.number = number;
    this.data = data;
  }

  /**
   * Instantiates a new unknown message.
   * 
   * @param message the message
   */
  public UnknownMessage(final SecureMessage message) {
    date = message.date;
    number = message.number;
    data = Store.encodeData(message.text);
  }

  /**
   * Serialize.
   * 
   * @return the byte[]
   */
  public synchronized byte[] serialize() {
    try {
      bout.reset();
      dout.writeLong(date);
      dout.writeUTF(number);
      dout.writeInt(data.length);
      dout.write(data);
      dout.flush();
    }
    catch (final IOException e) {
      return null;
    }
    return bout.toByteArray();
  }

}
