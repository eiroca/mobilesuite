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
package net.eiroca.j2me.sm.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.eiroca.j2me.sm.util.StoreException;

/**
 * Implementation of the Message interface.
 */
public final class SecureMessage {

  /** The bout. */
  private final ByteArrayOutputStream bout = new ByteArrayOutputStream();
  
  /** The dout. */
  private final DataOutputStream dout = new DataOutputStream(bout);

  /** The date. */
  public final long date;
  
  /** The number. */
  public String number;
  
  /** The text. */
  public String text;
  
  /** The status. */
  public int status;

  /**
   * Creates new <code>Message</code> instance.
   * 
   * @param aDate the a date
   * @param messageNumber the message number
   * @param messageText the message text
   * @param messageStatus the message status
   */

  public SecureMessage(final Long aDate, final String messageNumber, final String messageText, final int messageStatus) {
    if (aDate == null) {
      date = System.currentTimeMillis();
    }
    else {
      date = aDate.longValue();
    }
    number = messageNumber;
    text = messageText;
    status = messageStatus;
  }

  /**
   * Deserializes the byte[] into the Message object.
   * 
   * @param data the data
   * @return The deserialized object.
   * @throws StoreException the store exception
   */
  public SecureMessage(final byte[] data) throws StoreException {
    final ByteArrayInputStream bin = new ByteArrayInputStream(data);
    final DataInputStream din = new DataInputStream(bin);
    try {
      date = din.readLong();
      number = din.readUTF();
      text = din.readUTF();
      status = din.readInt();
    }
    catch (final IOException e) {
      throw new StoreException(StoreException.ERR_STOREREAD);
    }
    finally {
      try {
        din.close();
      }
      catch (final IOException e) {
      }
    }
  }

  /**
   * Serializes the <code>Message</code> object into the byte[] to be stored using the RMS.
   * 
   * @return The serialized representation of the Message.
   */
  public synchronized byte[] serialize() {
    try {
      bout.reset();
      dout.writeLong(date);
      dout.writeUTF(number);
      dout.writeUTF(text);
      dout.writeInt(status);
      dout.flush();
    }
    catch (final IOException e) {
      return null;
    }
    return bout.toByteArray();
  }

}
