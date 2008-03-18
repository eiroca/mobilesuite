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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.sm.data.Address;

/**
 * This is the message screen. Message can not be updated and saved into the storage.
 */
public class AddressScreen extends Form {

  public static final int MAX_NAME_LENGTH = 50;
  public static final int MAX_NUMBER_LENGTH = 30;
  public static final int MAX_PASSWORD_LENGTH = 16;

  private final TextField name;
  private final TextField number;
  private final TextField password;

  /**
   * Creates new <code>AddressScreen</code> instance.
   */
  public AddressScreen(final int title, boolean isNew) {
    super(BaseApp.messages[title]);
    // Create and add the form items
    name = new TextField(BaseApp.messages[SecureSMS.MSG_NAME], "", AddressScreen.MAX_NAME_LENGTH, TextField.ANY);
    number = new TextField(BaseApp.messages[SecureSMS.MSG_NUMBER], "", AddressScreen.MAX_NUMBER_LENGTH, TextField.PHONENUMBER);
    password = new TextField(BaseApp.messages[SecureSMS.MSG_KEY], "", AddressScreen.MAX_PASSWORD_LENGTH, TextField.PASSWORD);
    append(name);
    append(number);
    append(password);
    BaseApp.setup(this, BaseApp.cBACK, SecureSMS.cADRSAV);
    if (!isNew) {
      addCommand(SecureSMS.cADRDEL);
    }
  }

  /**
   * Updates the fields of the Form
   */
  public final void fromAddress(final Address address) {
    name.setString(address.name);
    number.setString(address.number);
    password.setString(address.key);
  }

  /**
   * Updates the fields of the Address
   */
  public final void toAddress(final Address address, final String prefix) {
    String numTel = number.getString();
    if (numTel != null) {
      if (numTel.startsWith("00")) {
        numTel = "+" + numTel.substring(2);
      }
      if (!numTel.startsWith("+")) {
        numTel = prefix + numTel;
      }
    }
    number.setString(numTel);
    address.name=name.getString();
    address.number=numTel;
    address.key=password.getString();
  }
}
