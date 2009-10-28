/** GPL >= 2.0
 * Based upon J2ME Minesweeper.
 * Copyright (C) M. Jumari
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
package net.eiroca.j2me.minesweeper;

import MineSweeper;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.app.Application;

public class CustomLevelForm extends Form {

  private final TextField tHeight;
  private final TextField tWidth;
  private final TextField tBomb;

  public CustomLevelForm() {
    super(Application.messages[MineSweeper.MSG_CUSTOMLEVEL]);
    tHeight = new TextField(Application.messages[MineSweeper.MSG_CL_HEIGTH], "", 2, TextField.NUMERIC);
    tWidth = new TextField(Application.messages[MineSweeper.MSG_CL_WIDTH], "", 2, TextField.NUMERIC);
    tBomb = new TextField(Application.messages[MineSweeper.MSG_CL_BOMBS], "", 2, TextField.NUMERIC);
    append(tHeight);
    append(tWidth);
    append(tBomb);
  }

  public void setInputs() {
    tHeight.setString(Integer.toString(MineSweeper.height));
    tWidth.setString(Integer.toString(MineSweeper.width));
    tBomb.setString(Integer.toString(MineSweeper.bomb));
  }

  public void getInputs() {
    MineSweeper.height = Integer.parseInt(tHeight.getString());
    MineSweeper.width = Integer.parseInt(tWidth.getString());
    MineSweeper.bomb = Integer.parseInt(tBomb.getString());
  }

}
