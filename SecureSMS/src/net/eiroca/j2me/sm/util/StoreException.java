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
package net.eiroca.j2me.sm.util;

/**
 * This is a superclass for all exceptions in SecureMessenger API.
 */
public class StoreException extends Exception {

  public static final int ERR_STOREOPEN = 1;
  public static final int ERR_STORESAVE = 2;
  public static final int ERR_STOREDELETE = 3;
  public static final int ERR_STOREREAD = 4;
  public static final int ERR_STOREFIND = 5;
  public static final int ERR_STORELISTIDS = 6;

  private static final long serialVersionUID = -5364585897287450564L;

  /**
   * Constructs an <code>SecureMessengerException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public StoreException(final int errCode) {
    super("Error #" + errCode);
  }
}
