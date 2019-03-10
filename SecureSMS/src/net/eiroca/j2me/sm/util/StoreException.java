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

/**
 * This is a superclass for all exceptions in SecureMessenger API.
 */
public class StoreException extends Exception {

  /** The Constant ERR_STOREOPEN. */
  public static final int ERR_STOREOPEN = 1;

  /** The Constant ERR_STORESAVE. */
  public static final int ERR_STORESAVE = 2;

  /** The Constant ERR_STOREDELETE. */
  public static final int ERR_STOREDELETE = 3;

  /** The Constant ERR_STOREREAD. */
  public static final int ERR_STOREREAD = 4;

  /** The Constant ERR_STOREFIND. */
  public static final int ERR_STOREFIND = 5;

  /** The Constant ERR_STORELISTIDS. */
  public static final int ERR_STORELISTIDS = 6;

  /**
   * Constructs an <code>SecureMessengerException</code> with the specified detail message.
   * 
   * @param errCode the err code
   */
  public StoreException(final int errCode) {
    super("Error #" + errCode);
  }
}
