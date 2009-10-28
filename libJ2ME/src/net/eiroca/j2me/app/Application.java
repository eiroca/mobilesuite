/**
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002 Eugene Morozov (xonixboy@hotmail.com)
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
 *
 * Copyright (c) 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The  above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */
package net.eiroca.j2me.app;

import java.util.Hashtable;
import java.util.Stack;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;

public abstract class Application extends BaseApp {

  /*
   * UI Manager
   */

  public static final int MD_MENUID = 0;
  public static final int MD_MENUTX = 1;
  public static final int MD_MENUAC = 2;
  public static final int MD_MENUIC = 3;

  public static Command cBACK;
  public static Command cEXIT;
  public static Command cOK;

  public static int background = 0x00000000;
  public static int foreground = 0x00FFFFFF;
  public static short[][] menu;
  public static String[] messages;
  public static Image[] icons;
  public static int pSpecial;

  /**
   * Go and back for all Displayables.
   */
  private static final Stack displayableStack = new Stack();
  private static final Hashtable commands = new Hashtable();
  private static final Hashtable listItems = new Hashtable();

  public static final int AC_NONE = 0;
  public static final int AC_BACK = -101;
  public static final int AC_EXIT = -100;

  public static final int EV_BEFORECHANGE = 1;
  public static final int EV_AFTERCHANGE = 2;

  /**
   * Go back to previous Displayable with one return code.
   *
   * @param alert
   * @param returnCode,
   * @return
   */
  public static Displayable back(final Alert alert) {
    // Back to A Displayable from B Displayable, so the Stack's size must be
    // more or equals to 2.
    if (Application.displayableStack.size() >= 2) {
      final Displayable previous = (Displayable) Application.displayableStack.pop();
      // get the instance of the previous one but remain it in the stack.
      final Displayable next = (Displayable) Application.displayableStack.peek();
      BaseApp.midlet.changed(Application.EV_BEFORECHANGE, previous, next);
      if (alert == null) {
        BaseApp.setDisplay(next);
      }
      else {
        BaseApp.setDisplay(alert, next);
      }
      BaseApp.midlet.changed(Application.EV_AFTERCHANGE, previous, next);
      return next;
    }
    return null;
  }

  /**
   * Go back to specify Displayable. It's same like calling go() to add one new
   * Displayable, if the next is not existing in the stack.
   *
   * @param alert
   * @param next
   */
  public static void back(final Alert alert, final Displayable next, final boolean keepPrevious) {
    if (!Application.displayableStack.empty()) {
      final int index = Application.displayableStack.search(next);
      final Displayable previous = (Displayable) Application.displayableStack.pop();
      for (int i = index - 1; i >= 1; i--) {
        Application.displayableStack.pop();
      }
      if (keepPrevious) {
        Application.displayableStack.push(previous);
      }
      Application.show(alert, next, true);
    }
  }

  /**
   * Add one new Displayable. save is used to set the Displayable to the stack
   * or not.
   *
   * @param alert
   * @param next, the new Displayable to be the current Dislayable in the
   *          screen, if next is null, it will
   * @param save, next will be saved into the Stack (for back() to return to
   *          previous Displayable) if save is set to true
   */
  public static void show(final Alert alert, Displayable next, final boolean save) {
    Displayable previous = null;
    if (!Application.displayableStack.empty()) {
      previous = (Displayable) Application.displayableStack.peek();
    }
    if (next == null) {
      next = BaseApp.getDisplay();
    }
    else {
      final boolean isNew = (previous == null) || (previous != next);
      final boolean isAlert = (next instanceof Alert) && (((Alert) next).getTimeout() != Alert.FOREVER);
      if (save && isNew && !isAlert) {
        Application.displayableStack.push(next);
      }
    }
    BaseApp.midlet.changed(Application.EV_BEFORECHANGE, previous, next);
    if (alert == null) {
      BaseApp.setDisplay(next);
    }
    else {
      BaseApp.setDisplay(alert, next);
    }
    BaseApp.midlet.changed(Application.EV_AFTERCHANGE, previous, next);
  }

  public static Command newCommand(final int label, final int commandType, final int priority, final int action) {
    final Command cmd = new Command(Application.messages[label], commandType, priority);
    Application.commands.put(cmd, new Integer(action));
    return cmd;
  }

  /**
   * @param cmd
   * @param action
   */
  public static void registerCommand(final Command cmd, final int action) {
    Application.commands.put(cmd, new Integer(action));
  }

  /**
   * @param list
   * @param action
   */
  public static void registerList(final List list, final int action) {
    Application.listItems.put(list, new Integer(action));
  }

  /**
   * @param list
   * @param index
   * @param action
   */
  public static void registerListItem(final List list, final int index, final int action) {
    Application.listItems.put(list + "#" + index, new Integer(action));
  }

  /**
   * @param action
   * @param d
   * @param cmd
   * @return
   */
  abstract public boolean handleAction(int action, Displayable d, Command cmd);

  public void process(final Command cmd, final Displayable d, final Item i) {
    // if cmd is list selection, we change cmd to actual command
    Object at = null;
    if (cmd == List.SELECT_COMMAND) {
      if ((d != null) && (d instanceof List)) {
        final List list = (List) d;
        final int index = list.getSelectedIndex();
        at = Application.listItems.get(list + "#" + index);
        if (at == null) {
          at = Application.listItems.get(list);
        }
      }
    }
    if ((at == null) && (cmd != null)) {
      at = Application.commands.get(cmd);
    }
    if (at != null) {
      final int action = ((Integer) at).intValue();
      boolean processed = handleAction(action, d, cmd);
      if (!processed) {
        switch (action) {
          case AC_BACK: {
            Application.back(null);
            processed = true;
            break;
          }
          case AC_EXIT: {
            BaseApp.midlet.notifyDestroyed();
            processed = true;
            break;
          }
        }
      }
    }
  }

  /**
   * @param d
   * @param c1
   * @param c2
   */
  public static void setup(final Displayable d, final Command c1, final Command c2) {
    d.setCommandListener(BaseApp.midlet);
    if (c1 != null) {
      d.addCommand(c1);
    }
    if (c2 != null) {
      d.addCommand(c2);
    }
  }

  /**
   * @param list
   * @param ps
   * @param def
   */
  public static void insertMenuItem(final List list, final int ps, final short[] def) {
    Image icon = null;
    if (def[Application.MD_MENUIC] >= 0) {
      icon = Application.icons[def[Application.MD_MENUIC]];
    }
    list.insert(ps, Application.messages[def[Application.MD_MENUTX]], icon);
  }

  /**
   * @param id
   * @param list
   * @return
   */
  public static short getAction(final int id, final List list) {
    final int idx = list.getSelectedIndex();
    short[] def;
    int ps = 0;
    for (int i = 0; i < Application.menu.length; i++) {
      def = Application.menu[i];
      if (def[Application.MD_MENUID] == id) {
        if (ps == idx) { return def[Application.MD_MENUAC]; }
        ps++;
      }
    }
    return -1;
  }

  /**
   * @param owner
   * @param title
   * @param menuID
   * @param menuAction
   * @param special
   * @param cmd
   * @return
   */
  public static List getMenu(final String title, final int menuID, final int special, final Command cmd) {
    final List list = new List(title, Choice.IMPLICIT);
    short[] def;
    int ps = 0;
    for (int i = 0; i < Application.menu.length; i++) {
      def = Application.menu[i];
      final int action = def[Application.MD_MENUAC];
      if (def[Application.MD_MENUID] == menuID) {
        if (action == special) {
          Application.pSpecial = i;
        }
        else {
          Application.insertMenuItem(list, ps, def);
          if (action != Application.AC_NONE) {
            Application.registerListItem(list, ps, action);
          }
          ps++;
        }
      }
    }
    Application.setup(list, cmd, null);
    return list;
  }

  /**
   * @param cl
   * @param title
   * @param textRes
   * @return
   */
  public static Displayable getTextForm(final int title, final String textRes) {
    final Form form = new Form(Application.messages[title]);
    final String msg = BaseApp.readString(textRes);
    if (msg != null) {
      form.append(msg);
    }
    Application.setup(form, Application.cBACK, null);
    return form;
  }

  /**
   * @param cl
   * @param title
   * @param textRes
   * @param o
   * @return
   */
  public static Displayable getTextForm(final int title, final String textRes, final Object[] o) {
    final Form form = new Form(Application.messages[title]);
    final String msg = BaseApp.readString(textRes);
    if (msg != null) {
      form.append(BaseApp.format(msg, o));
    }
    Application.setup(form, Application.cBACK, null);
    return form;
  }

  /**
   * Displays the alert.
   */
  public static void showAlert(final int alertTitle, final int alertMessage, final Image alertImage, final AlertType alertType, final Displayable alertNext, final int timeOut) {
    final Alert alert = new Alert(Application.messages[alertTitle], Application.messages[alertMessage], alertImage, alertType);
    alert.setTimeout(timeOut);
    Application.back(alert, alertNext, true);
  }

  public void confirm(final int title, final int question, final Command yes, final Command no) {
    final Form qform = new Form(Application.messages[title]);
    qform.append(Application.messages[question]);
    qform.addCommand(yes);
    qform.addCommand(no);
    qform.setCommandListener(this);
    Application.show(null, qform, true);
  }

  /**
   * @param msg
   * @param o
   * @return
   */
  public static String format(final int msg, final Object[] o) {
    return BaseApp.format(Application.messages[msg], o);
  }

  /**
   * Application destroy
   */
  protected void done() {
    Application.displayableStack.removeAllElements();
    super.done();
  }

}
