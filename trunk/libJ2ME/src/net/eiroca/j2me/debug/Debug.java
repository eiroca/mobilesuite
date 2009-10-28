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
package net.eiroca.j2me.debug;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;

public class Debug {

  private final static int MAX_MESSAGES = 50;

  private final Vector messages = new Vector();

  BaseApp app;

  public Debug(final BaseApp app) {
    this.app = app;
  }

  public void showMessageScreen(final Command back) {
    final Form f = new Form("Debug messages");
    DebugMessage msg;
    StringItem si;
    for (int i = messages.size() - 1; i >= 0; i--) {
      msg = (DebugMessage) messages.elementAt(i);
      si = new StringItem((msg.err == null) ? null : msg.err.toString(), msg.message);
      f.append(si);
    }
    f.addCommand(back);
    f.setCommandListener(app);
    Application.show(null, f, true);
  }

  public void addMessage(final String message) {
    addMessage(new DebugMessage(0, message, null));
  }

  public void addException(final Throwable e) {
    addMessage(new DebugMessage(1, null, e));
  }

  public void addException(final String message, final Throwable e) {
    addMessage(new DebugMessage(2, message, e));
  }

  public void addMessage(final DebugMessage msg) {
    checkSize(msg.priority);
    messages.addElement(msg);
  }

  public void checkSize(final int priority) {
    if (messages.size() > Debug.MAX_MESSAGES) {
      DebugMessage msg;
      boolean spaceDone = false;
      for (int i = 0; i < messages.size(); i++) {
        msg = (DebugMessage) messages.elementAt(i);
        if (msg.priority < priority) {
          messages.removeElementAt(i);
          spaceDone = true;
          break;
        }
      }
      if (!spaceDone) {
        messages.removeElementAt(0);
      }
    }
  }

}
