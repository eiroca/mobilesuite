/** GPL >= 2.0
 * Based upon RSS Reader MIDlet
 * Copyright (C) 2004 Gösta Jonasson <gosta(at)brothas.net>
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
package net.eiroca.j2me.RSSReader;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import net.eiroca.j2me.app.Application;

public class StatusScreen extends Form implements CommandListener {

  protected Gauge gStatus;
  // true if the user wants to abort the action
  public boolean stopped = false;
  private final Command cOK;
  private final Command cSTOP;
  private final Displayable next;

  public StatusScreen(final String name, final Command cOK, final Command cSTOP, final Displayable next) {
    super(name);
    this.cOK = cOK;
    this.cSTOP = cSTOP;
    this.next = next;
    gStatus = new Gauge("?", false, 1, 0);
    append(gStatus);
    setCommandListener(this);
  }

  public void init(final String stepName, final int steps) {
    gStatus.setMaxValue(steps - 1);
    gStatus.setValue(0);
    gStatus.setLabel(stepName);
    stopped = false;
    removeCommand(cOK);
    addCommand(cSTOP);
    if (next == null) {
      Application.show(null, this, true);
    }
    else {
      Application.show(null, this, false);
    }
  }

  public void done() {
    removeCommand(cSTOP);
    addCommand(cOK);
    stopped = true;
  }

  public void setStatus(final String stepName, final int value) {
    gStatus.setLabel(stepName);
    gStatus.setValue(value);
  }

  /**
   * Takes care of the command event that occured on the Displayable.
   * @param c the command event that occured
   * @param d the Displayable on which the command occured
   */
  public void commandAction(final Command c, final Displayable d) {
    /* Only called if stop is pressed */
    if (!stopped) {
      stopped = true;
    }
    else {
      if (next == null) {
        Application.back(null);
      }
      else {
        Application.show(null, next, true);
      }
    }
  }

}
