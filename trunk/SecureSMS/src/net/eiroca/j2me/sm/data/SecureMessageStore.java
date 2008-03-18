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
 * Implementation of the MessageStore interface. Note: this implementation may be platform-specific even if it uses the RMS.
 */
public final class SecureMessageStore extends Store {

  /**
   * Creates new <code>MessageStoreImpl</code> instance. The additional string parameter allows us to create different MessageStores (different folders).
   * @param storeName The store name
   */
  public SecureMessageStore(final String storeName) throws StoreException {
    super(storeName);
  }

  public void store(final SecureMessage message) throws StoreException {
    replaceFirst(new StoreFilterByID(message.date), message.serialize());
    notifyAction(StoreObserver.ADD, message);
  }

  public SecureMessage remove(final long id) throws StoreException {
    final SecureMessage message = getById(id);
    removeFirst(new StoreFilterByID(id));
    notifyAction(StoreObserver.DEL, message);
    return message;
  }

  public SecureMessage getById(final long id) throws StoreException {
    final byte[] res = findFirst(new StoreFilterByID(id));
    if (res == null) { throw new MessageHandlerException(MessageHandlerException.ERR_NOMESSAGE); }
    return new SecureMessage(res);
  }

}
