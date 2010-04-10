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

import net.eiroca.j2me.sm.util.StoreException;

/**
 * This is a superclass for all exceptions in SecureMessenger API.
 */
public class MessageHandlerException extends StoreException {

  /** The Constant ERR_OPENCONNECTION. */
  public static final int ERR_OPENCONNECTION = 100;
  
  /** The Constant ERR_CLOSECONNECTION. */
  public static final int ERR_CLOSECONNECTION = 101;
  
  /** The Constant ERR_INVALIDKEY. */
  public static final int ERR_INVALIDKEY = 102;
  
  /** The Constant ERR_SENDMESSAGE. */
  public static final int ERR_SENDMESSAGE = 103;
  
  /** The Constant ERR_NOADDRESS. */
  public static final int ERR_NOADDRESS = 104;
  
  /** The Constant ERR_NOMESSAGE. */
  public static final int ERR_NOMESSAGE = 105;

  /**
   * Constructs an <code>SecureMessengerException</code> with the specified detail message.
   * 
   * @param errCode the err code
   */
  public MessageHandlerException(final int errCode) {
    super(errCode);
  }
}
