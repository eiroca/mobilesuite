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

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;

public abstract class Application extends BaseApp implements CommandListener, ItemCommandListener {

  /**
   * @param list
   * @param ps
   * @param def
   */
  public static void insertMenuItem(final List list, final int ps, final short[] def) {
    Image icon = null;
    if (def[BaseApp.MD_MENUIC] >= 0) {
      icon = BaseApp.icons[def[BaseApp.MD_MENUIC]];
    }
    list.insert(ps, BaseApp.messages[def[BaseApp.MD_MENUTX]], icon);
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
    for (int i = 0; i < BaseApp.menu.length; i++) {
      def = BaseApp.menu[i];
      if (def[BaseApp.MD_MENUID] == id) {
        if (ps == idx) { return def[BaseApp.MD_MENUAC]; }
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
    for (int i = 0; i < BaseApp.menu.length; i++) {
      def = BaseApp.menu[i];
      final int action = def[BaseApp.MD_MENUAC];
      if (def[BaseApp.MD_MENUID] == menuID) {
        if (action == special) {
          BaseApp.pSpecial = i;
        }
        else {
          Application.insertMenuItem(list, ps, def);
          if (action != BaseApp.AC_NONE) {
            BaseApp.registerListItem(list, ps, action);
          }
          ps++;
        }
      }
    }
    BaseApp.setup(list, cmd, null);
    return list;
  }

  public void confirm(final int title, final int question, final Command yes, final Command no) {
    final Form qform = new Form(BaseApp.messages[title]);
    qform.append(BaseApp.messages[question]);
    qform.addCommand(yes);
    qform.addCommand(no);
    qform.setCommandListener(this);
    BaseApp.show(null, qform, true);
  }

}
