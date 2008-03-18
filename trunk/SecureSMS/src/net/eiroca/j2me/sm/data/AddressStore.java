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

import net.eiroca.j2me.sm.util.Store;
import net.eiroca.j2me.sm.util.StoreException;
import net.eiroca.j2me.sm.util.StoreFilterByID;
import net.eiroca.j2me.sm.util.StoreObserver;

/**
 * Implementation of the <code>AddressStore</code> interface.
 */
public final class AddressStore extends Store {

  /**
   * Creates new <code>AddressStoreImpl</code> instance. The additional string parameter allows us to create different AddressStores (for example different address books).
   */
  public AddressStore(final String storeName) throws StoreException {
    super(storeName);
  }

  public void store(final Address address) throws StoreException {
    replaceFirst(new StoreFilterByID(address.id), address.serialize());
    notifyAction(StoreObserver.ADD, address);
  }

  public Address remove(final long id) throws StoreException {
    final Address address = getById(id);
    removeFirst(new StoreFilterByID(id));
    notifyAction(StoreObserver.DEL, address);
    return address;
  }

  public Address getById(final long id) throws StoreException {
    final byte[] res = findFirst(new StoreFilterByID(id));
    if (res == null) { throw new MessageHandlerException(MessageHandlerException.ERR_NOADDRESS); }
    return new Address(res);
  }

  public Address getByNumber(final String number, final boolean fails) throws StoreException {
    final byte[] res = findFirst(new AddressFilterByNumber(number));
    if (res == null) {
      if (fails) { throw new MessageHandlerException(MessageHandlerException.ERR_NOADDRESS); }
      return null;
    }
    else {
      return new Address(res);
    }
  }

}
