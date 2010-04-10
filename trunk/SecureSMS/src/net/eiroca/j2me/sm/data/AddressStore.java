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
   * 
   * @param storeName the store name
   * @throws StoreException the store exception
   */
  public AddressStore(final String storeName) throws StoreException {
    super(storeName);
  }

  /**
   * Store.
   * 
   * @param address the address
   * @throws StoreException the store exception
   */
  public void store(final Address address) throws StoreException {
    replaceFirst(new StoreFilterByID(address.id), address.serialize());
    notifyAction(StoreObserver.ADD, address);
  }

  /**
   * Removes the.
   * 
   * @param id the id
   * @return the address
   * @throws StoreException the store exception
   */
  public Address remove(final long id) throws StoreException {
    final Address address = getById(id);
    removeFirst(new StoreFilterByID(id));
    notifyAction(StoreObserver.DEL, address);
    return address;
  }

  /**
   * Gets the by id.
   * 
   * @param id the id
   * @return the by id
   * @throws StoreException the store exception
   */
  public Address getById(final long id) throws StoreException {
    final byte[] res = findFirst(new StoreFilterByID(id));
    if (res == null) { throw new MessageHandlerException(MessageHandlerException.ERR_NOADDRESS); }
    return new Address(res);
  }

  /**
   * Gets the by number.
   * 
   * @param number the number
   * @param fails the fails
   * @return the by number
   * @throws StoreException the store exception
   */
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
