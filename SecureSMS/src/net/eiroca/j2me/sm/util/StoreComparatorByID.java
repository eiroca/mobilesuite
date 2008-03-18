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
package net.eiroca.j2me.sm.util;

import javax.microedition.rms.RecordComparator;

public class StoreComparatorByID implements RecordComparator {

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
