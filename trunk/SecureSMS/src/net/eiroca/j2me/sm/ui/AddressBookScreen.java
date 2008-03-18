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
package net.eiroca.j2me.sm.ui;

import SecureSMS;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.sm.data.Address;
import net.eiroca.j2me.sm.data.AddressStore;
import net.eiroca.j2me.sm.util.StoreException;

/**
 * The address book screen.
 */
public class AddressBookScreen extends List {

  long id;
  private long[] addressIds;

  /**
   * Creates new MessageStoreScreen
   */
  public AddressBookScreen() {
    // Create the list
    super(BaseApp.messages[SecureSMS.MSG_ADDRESSBOOK], Choice.IMPLICIT);
    BaseApp.setup(this, BaseApp.cBACK, SecureSMS.cADRADD);
  }

  /**
   * Returns the Id of the currently selected message
   */
  public long getSelectedAddressId() {
    // Get the list's selected index and map it into the mesage id
    final int selectedIndex = getSelectedIndex();
    if ((addressIds != null) && (selectedIndex >= 0) && (selectedIndex < addressIds.length)) {
      // Note: We add addresses in the reverse order (always to the first
      // position in list)
      return addressIds[addressIds.length - 1 - selectedIndex];
    }
    // This should actually never happen
    return -1;
  }

  /**
   * Updates the content of the List.
   */
  public void updateAddressList(final AddressStore addressStore) throws StoreException {
    id = getSelectedAddressId();
    deleteAll();
    addressIds = null;
    // Delete the command
    removeCommand(SecureSMS.cADRDEL);
    // Get the list of the Ids
    addressIds = addressStore.listIds(null);
    final int addressIdsLength = addressIds.length;
    if (addressIdsLength > 0) {
      // Add the addresses from the storage
      Address address;
      StringBuffer addressListItem;
      for (int i = 0; i < addressIdsLength; i++) {
        address = addressStore.getById(addressIds[i]);
        addressListItem = new StringBuffer(32);
        addressListItem.append(address.name).append(' ').append(address.number);
        insert(0, addressListItem.toString(), null);
        // Automatically select the message that has been selected the last time
        // user left the list
        if (address.id == id) {
          setSelectedIndex(i, true);
        }
      }
      // Add command
      addCommand(SecureSMS.cADRDEL);
    }
  }

}
