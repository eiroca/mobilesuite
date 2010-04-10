/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.Application;

/**
 * The Class InsertPINScreen.
 */
public class InsertPINScreen extends Form {

  /** The Constant MAX_PIN_LENGTH. */
  public static final int MAX_PIN_LENGTH = 8;
  
  /** The Constant MIN_PIN_LENGTH. */
  public static final int MIN_PIN_LENGTH = 4;

  /** The err. */
  public StringItem err;
  
  /** The pin text. */
  private final TextField pinText;

  /**
   * Instantiates a new insert pin screen.
   * 
   * @param title the title
   */
  public InsertPINScreen(final int title) {
    super(Application.messages[title]);
    // Create and add the form items
    err = new StringItem(null, null);
    pinText = new TextField(Application.messages[SecureSMS.MSG_PIN], "", InsertPINScreen.MAX_PIN_LENGTH, TextField.NUMERIC);
    append(err);
    append(pinText);
    Application.setup(this, SecureSMS.cPINOK, null);
  }

  /**
   * Gets the pIN.
   * 
   * @return the pIN
   */
  public String getPIN() {
    String pin = pinText.getString();
    if (pin == null) {
      pin = "";
    }
    return pin;
  }

}
