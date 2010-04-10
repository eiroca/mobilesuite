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
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.sm.data.SecureMessage;

/**
 * The message screen. Can be extended to create specific message screens (Incoming, Outgoing).
 */
public class MessageScreen extends Form {

  /** The address. */
  protected StringItem address;
  
  /** The text. */
  protected StringItem text;

  /**
   * Creates new MessageScreen.
   * 
   * @param title the title
   * @param addressTitle the address title
   * @param textTitle the text title
   * @param cmd1 the cmd1
   * @param cmd2 the cmd2
   */
  public MessageScreen(final int title, final int addressTitle, final int textTitle, final Command cmd1, final Command cmd2) {
    super(Application.messages[title]);
    // Create the string items
    address = new StringItem(Application.messages[addressTitle], "");
    text = new StringItem(Application.messages[textTitle], "");
    append(address);
    append(text);
    // Add commands
    addCommand(Application.cBACK);
    Application.setup(this, cmd1, cmd2);
  }

  /**
   * Updates the message Form.
   * 
   * @param app the app
   * @param message the message
   */
  public final void updateMessage(final SecureSMS app, final SecureMessage message) {
    // Set the title
    final StringBuffer sb = new StringBuffer();
    final Calendar timestamp = Calendar.getInstance();
    timestamp.setTime(new Date(message.date));
    app.formatDate(timestamp, sb).append(' ');
    app.formatTime(timestamp, true, sb);
    setTitle(sb.toString());
    final String name = app.handler.addressName(message);
    address.setText((name == null ? "" : name));
    text.setText(message.text);
  }

}
