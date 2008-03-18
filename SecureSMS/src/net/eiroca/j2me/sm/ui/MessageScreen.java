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
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.sm.data.SecureMessage;

/**
 * The message screen. Can be extended to create specific message screens (Incoming, Outgoing).
 */
public class MessageScreen extends Form {

  protected StringItem address;
  protected StringItem text;

  /**
   * Creates new MessageScreen
   */
  public MessageScreen(final int title, final int addressTitle, final int textTitle, final Command cmd1, final Command cmd2) {
    super(BaseApp.messages[title]);
    // Create the string items
    address = new StringItem(BaseApp.messages[addressTitle], "");
    text = new StringItem(BaseApp.messages[textTitle], "");
    append(address);
    append(text);
    // Add commands
    addCommand(BaseApp.cBACK);
    BaseApp.setup(this, cmd1, cmd2);
  }

  /**
   * Updates the message Form.
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
