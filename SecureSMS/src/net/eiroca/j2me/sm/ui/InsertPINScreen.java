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
package net.eiroca.j2me.sm.ui;

import SecureSMS;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.BaseApp;

public class InsertPINScreen extends Form {

  public static final int MAX_PIN_LENGTH = 8;
  public static final int MIN_PIN_LENGTH = 4;

  public StringItem err;
  private final TextField pinText;

  public InsertPINScreen(final int title) {
    super(BaseApp.messages[title]);
    // Create and add the form items
    err = new StringItem(null, null);
    pinText = new TextField(BaseApp.messages[SecureSMS.MSG_PIN], "", InsertPINScreen.MAX_PIN_LENGTH, TextField.NUMERIC);
    append(err);
    append(pinText);
    BaseApp.setup(this, SecureSMS.cPINOK, null);
  }

  public String getPIN() {
    String pin = pinText.getString();
    if (pin == null) {
      pin = "";
    }
    return pin;
  }

}
