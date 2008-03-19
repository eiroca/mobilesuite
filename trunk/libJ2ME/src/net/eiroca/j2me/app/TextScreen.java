/** GPL >= 2.0
 *
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
package net.eiroca.j2me.app;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

public class TextScreen extends Form implements CommandListener {

  private final Displayable next;

  public TextScreen(final String title, final Displayable next, final String label, final String[] msg) {
    super(title);
    Command backCommand;
    this.next = next;
    for (int i = 0; i < msg.length; i++) {
      append(msg[i]);
      append("\n");
    }
    backCommand = new Command(label, Command.BACK, 1);
    addCommand(backCommand);
    setCommandListener(this);
  }

  public void commandAction(final Command c, final Displayable d) {
    BaseApp.setDisplay(next);
  }

}
