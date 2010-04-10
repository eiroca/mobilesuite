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
package net.eiroca.j2me.sm.ui;

import SecureSMS;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.sm.data.Address;
import net.eiroca.j2me.sm.data.AddressStore;
import net.eiroca.j2me.sm.util.StoreException;

/**
 * The address book screen.
 */
public class AddressBookScreen extends List {

  /** The id. */
  long id;
  
  /** The address ids. */
  private long[] addressIds;

  /**
   * Creates new MessageStoreScreen.
   */
  public AddressBookScreen() {
    // Create the list
    super(Application.messages[SecureSMS.MSG_ADDRESSBOOK], Choice.IMPLICIT);
    Application.setup(this, Application.cBACK, SecureSMS.cADRADD);
  }

  /**
   * Returns the Id of the currently selected message.
   * 
   * @return the selected address id
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
   * 
   * @param addressStore the address store
   * @throws StoreException the store exception
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
