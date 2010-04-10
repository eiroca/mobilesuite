/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2004 Gösta Jonasson
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
package net.eiroca.j2me.RSSReader;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import net.eiroca.j2me.app.Application;

/**
 * The Class StatusScreen.
 */
public class StatusScreen extends Form implements CommandListener {

  /** The g status. */
  protected Gauge gStatus;
  // true if the user wants to abort the action
  /** The stopped. */
  public boolean stopped = false;

  /** The c ok. */
  private final Command cOK;

  /** The c stop. */
  private final Command cSTOP;

  /** The next. */
  private final Displayable next;

  /**
   * Instantiates a new status screen.
   * 
   * @param name the name
   * @param cOK the c ok
   * @param cSTOP the c stop
   * @param next the next
   */
  public StatusScreen(final String name, final Command cOK, final Command cSTOP, final Displayable next) {
    super(name);
    this.cOK = cOK;
    this.cSTOP = cSTOP;
    this.next = next;
    gStatus = new Gauge("?", false, 1, 0);
    append(gStatus);
    setCommandListener(this);
  }

  /**
   * Inits the.
   * 
   * @param stepName the step name
   * @param steps the steps
   */
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

  /**
   * Done.
   */
  public void done() {
    removeCommand(cSTOP);
    addCommand(cOK);
    stopped = true;
  }

  /**
   * Sets the status.
   * 
   * @param stepName the step name
   * @param value the value
   */
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
