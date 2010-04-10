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
 * Implementation of the MessageStore interface. Note: this implementation may be platform-specific even if it uses the RMS.
 */
public final class SecureMessageStore extends Store {

  /**
   * Creates new <code>MessageStoreImpl</code> instance. The additional string parameter allows us to create different MessageStores (different folders).
   * 
   * @param storeName The store name
   * @throws StoreException the store exception
   */
  public SecureMessageStore(final String storeName) throws StoreException {
    super(storeName);
  }

  /**
   * Store.
   * 
   * @param message the message
   * @throws StoreException the store exception
   */
  public void store(final SecureMessage message) throws StoreException {
    replaceFirst(new StoreFilterByID(message.date), message.serialize());
    notifyAction(StoreObserver.ADD, message);
  }

  /**
   * Removes the.
   * 
   * @param id the id
   * @return the secure message
   * @throws StoreException the store exception
   */
  public SecureMessage remove(final long id) throws StoreException {
    final SecureMessage message = getById(id);
    removeFirst(new StoreFilterByID(id));
    notifyAction(StoreObserver.DEL, message);
    return message;
  }

  /**
   * Gets the by id.
   * 
   * @param id the id
   * @return the by id
   * @throws StoreException the store exception
   */
  public SecureMessage getById(final long id) throws StoreException {
    final byte[] res = findFirst(new StoreFilterByID(id));
    if (res == null) { throw new MessageHandlerException(MessageHandlerException.ERR_NOMESSAGE); }
    return new SecureMessage(res);
  }

}
