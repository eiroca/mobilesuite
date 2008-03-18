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

import javax.microedition.rms.RecordFilter;

/**
 * Implementation of the RMS RecordFilter interface.
 */
public class AddressFilterByNumber implements RecordFilter {

  private final String number;

  /**
   * Creates new <code>AddressRecordFilter</code> instance.
   * @param addressId Address Id to filter
   * @param addressNamó Address name to filter
   * @param addressNumber Address MSISDN to filter
   */
  public AddressFilterByNumber(final String aNumber) {
    number = aNumber;
  }

  public boolean matches(final byte[] values) {
    return Address.check(values, 0, null, number);
  }

}
