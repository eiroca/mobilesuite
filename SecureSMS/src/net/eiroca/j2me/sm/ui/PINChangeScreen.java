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
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.Application;

public class PINChangeScreen extends Form {

  public static final int MAX_PIN_LENGTH = 8;
  public static final int MIN_PIN_LENGTH = 4;

  private final TextField pinText;

  public PINChangeScreen(final int title) {
    super(Application.messages[title]);
    // Create and add the form items
    append(Application.messages[SecureSMS.MSG_INSERTPINTEXT]);
    pinText = new TextField(Application.messages[SecureSMS.MSG_PIN], "", PINChangeScreen.MAX_PIN_LENGTH, TextField.NUMERIC);
    append(pinText);
    Application.setup(this, Application.cBACK, SecureSMS.cPINSAV);
    setPIN(null);
  }

  public String getPIN() {
    String pin = pinText.getString();
    if ((pin != null) & (pin.length() < PINChangeScreen.MIN_PIN_LENGTH)) {
      pin = null;
    }
    return pin;
  }

  public void setPIN(final String pin) {
    if (pin == null) {
      pinText.setString("");
      removeCommand(SecureSMS.cPINDEL);
    }
    else {
      pinText.setString(pin);
      addCommand(SecureSMS.cPINDEL);
    }
  }

}
