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

import javax.microedition.rms.RecordFilter;

/**
 * Implementation of the RMS RecordFilter interface.
 */
public class StoreFilterByID implements RecordFilter {

  /** The id. */
  private final long id;

  /**
   * Creates new <code>AddressRecordFilter</code> instance.
   * 
   * @param aID the a id
   */
  public StoreFilterByID(final long aID) {
    id = aID;
  }

  /* (non-Javadoc)
   * @see javax.microedition.rms.RecordFilter#matches(byte[])
   */
  public boolean matches(final byte[] values) {
    if ((id != 0) && (Store.getID(values) == id)) {
      return true;
    }
    return false;
  }

}
