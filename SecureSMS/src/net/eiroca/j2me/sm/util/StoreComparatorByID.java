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
package net.eiroca.j2me.sm.util;

import javax.microedition.rms.RecordComparator;

/**
 * The Class StoreComparatorByID.
 */
public class StoreComparatorByID implements RecordComparator {

  /* (non-Javadoc)
   * @see javax.microedition.rms.RecordComparator#compare(byte[], byte[])
   */
  public int compare(final byte[] rec1, final byte[] rec2) {
    final long id1 = Store.getID(rec1);
    final long id2 = Store.getID(rec2);
    if (id1 < id2) {
      return RecordComparator.PRECEDES;
    }
    else if (id1 > id2) {
      return RecordComparator.FOLLOWS;
    }
    else {
      return RecordComparator.EQUIVALENT;
    }
  }
}
