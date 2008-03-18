/** GPL >= 2.0
 * Based upon SecureMessenger
 * Copyright (C) 2002 Eugene Morozov
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

  private final ByteArrayOutputStream bout = new ByteArrayOutputStream();
  private final DataOutputStream dout = new DataOutputStream(bout);

  public final long date;
  public String number;
  public String text;
  public int status;

  /**
   * Creates new <code>Message</code> instance
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
   * @param serializedMessage Serialized message (received from RMS store for example)
   * @return The deserialized <code>Message</code> object.
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
   * @param deserializedMessage The Message object.
   * @return The serialized <code>byte[]</code> representation of the Message.
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
