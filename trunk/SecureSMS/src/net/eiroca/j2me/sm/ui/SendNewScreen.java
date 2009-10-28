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
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.sm.data.Address;
import net.eiroca.j2me.sm.data.AddressStore;
import net.eiroca.j2me.sm.data.SecureMessage;
import net.eiroca.j2me.sm.util.StoreException;

/**
 * New message screen.
 */
public class SendNewScreen extends Form {

  public static final int MAX_TEXT_LENGTH = 5000;

  private final TextField text;
  private final ChoiceGroup addresses;

  private long[] addressIds;

  /**
   * Creates new MessageScreen.
   */
  public SendNewScreen() {
    super(Application.messages[SecureSMS.MSG_NEWMESSAGE]);
    // Create and add the form items
    text = new TextField(Application.messages[SecureSMS.MSG_TEXT], "", SendNewScreen.MAX_TEXT_LENGTH, TextField.ANY);
    addresses = new ChoiceGroup(Application.messages[SecureSMS.MSG_TO], Choice.EXCLUSIVE);
    append(text);
    append(addresses);
    Application.setup(this, Application.cBACK, SecureSMS.cSENDNEW);
  }

  /**
   * Updates the screen.
   */
  public final boolean updateMessage(final SecureMessage message, final AddressStore addressStore) throws StoreException {
    boolean result = false;
    // Set the text
    text.setString(message.text);
    // Get the address of the message;
    final String messageAddress = (message.number == null) ? "" : message.number;
    // Clear the address list
    addresses.deleteAll();
    // Add possible addresses and mark the one that matches the address in the
    // message (for the case of the reply operation). Get the list of the Ids
    addressIds = addressStore.listIds(null);
    final int addressIdsLength = addressIds.length;
    Address address;
    if (addressIdsLength > 0) {
      result = true;
      // Add the addresses from the storage
      StringBuffer addressListItem;
      for (int i = 0; i < addressIdsLength; i++) {
        address = addressStore.getById(addressIds[i]);
        addressListItem = new StringBuffer(32);
        addressListItem.append(address.name).append(' ').append(address.number);
        addresses.insert(0, addressListItem.toString(), null);
        // Automatically select the message that has been selected
        // the last time user left the list
        if (messageAddress.equals(address.number)) {
          addresses.setSelectedIndex(i, true);
        }
      }
    }
    return result;
  }

  /**
   * Returns the new message text.
   */
  public final String getMessageText() {
    return text.getString();
  }

  /**
   * Returns the id of the address to send the message to.
   */
  public final long getSelectedAddressId() {
    final int selectedIndex = addresses.getSelectedIndex();
    if ((addressIds != null) && (selectedIndex >= 0) && (selectedIndex < addressIds.length)) {
      // Addresses are added in the reverse order
      return addressIds[addressIds.length - 1 - selectedIndex];
    }
    // This should actually never happen
    return -1;
  }

}
