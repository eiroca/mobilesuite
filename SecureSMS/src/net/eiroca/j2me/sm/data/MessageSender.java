/** GPL >= 2.0
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

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.wireless.messaging.BinaryMessage;

public class MessageSender extends Thread {

  private final MessageHandler handler;
  private final BinaryMessage message;
  private int status;

  public MessageSender(final MessageHandler handler, final BinaryMessage wmaMessage) {
    super();
    this.handler = handler;
    message = wmaMessage;
  }

  /**
   * Implementation of runnable interface
   */
  public void run() {
    setStatus(0);
    try {
      try {
        Thread.sleep(50);
      }
      catch (final InterruptedException e) {
      }
      handler.connection.send(message);
      setStatus(1);
    }
    catch (final InterruptedIOException e) {
      setStatus(-1);
    }
    catch (final IOException e) {
      setStatus(-2);
    }
  }

  public synchronized int getStatus() {
    return status;
  }

  public synchronized void setStatus(final int status) {
    this.status = status;
  }

}
