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
package net.eiroca.j2me.game;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;

import net.eiroca.j2me.app.BaseApp;

public class GameUISettings extends Form {

  public static ChoiceGroup stVibrate = null;
  public static ChoiceGroup stBackLight = null;
  public static Gauge stVolume = null;

  public GameUISettings(final GameApp owner, final int gmFeature) {
    super(BaseApp.messages[GameApp.MSG_MENU_MAIN_SETTINGS]);
    if ((gmFeature & GameApp.FT_AUDIO) != 0) {
      GameUISettings.stVolume = new Gauge(BaseApp.messages[GameApp.MSG_MENU_SETTINGS_VOLUME], true, 5, 0);
      append(GameUISettings.stVolume);
    }
    if ((gmFeature & GameApp.FT_VIBRATE) != 0) {
      GameUISettings.stVibrate = new ChoiceGroup(BaseApp.messages[GameApp.MSG_MENU_SETTINGS_VIBRATE], Choice.EXCLUSIVE);
      GameUISettings.stVibrate.append(BaseApp.messages[GameApp.MSG_LABEL_YES], null);
      GameUISettings.stVibrate.append(BaseApp.messages[GameApp.MSG_LABEL_NO], null);
      append(GameUISettings.stVibrate);
    }
    if ((gmFeature & GameApp.FT_LIGHT) != 0) {
      GameUISettings.stBackLight = new ChoiceGroup(BaseApp.messages[GameApp.MSG_MENU_SETTINGS_BACKLIGHT], Choice.EXCLUSIVE);
      GameUISettings.stBackLight.append(BaseApp.messages[GameApp.MSG_LABEL_YES], null);
      GameUISettings.stBackLight.append(BaseApp.messages[GameApp.MSG_LABEL_NO], null);
      append(GameUISettings.stBackLight);
    }
    BaseApp.setup(this, BaseApp.cBACK, BaseApp.cOK);
  }

  public static void setVals() {
    if (GameUISettings.stVolume != null) {
      GameUISettings.stVolume.setValue(GameApp.usVolume / 20);
    }
    if (GameUISettings.stVibrate != null) {
      if (GameApp.usVibrate) {
        GameUISettings.stVibrate.setSelectedIndex(0, true);
      }
      else {
        GameUISettings.stVibrate.setSelectedIndex(1, true);
      }
    }
    if (GameUISettings.stBackLight != null) {
      if (GameApp.usBackLight) {
        GameUISettings.stBackLight.setSelectedIndex(0, true);
      }
      else {
        GameUISettings.stBackLight.setSelectedIndex(1, true);
      }
    }
  }

  public static void getVals() {
    if (GameUISettings.stVolume != null) {
      GameApp.usVolume = GameUISettings.stVolume.getValue() * 20;
    }
    if (GameUISettings.stVibrate != null) {
      if (GameUISettings.stVibrate.getSelectedIndex() == 0) {
        GameApp.usVibrate = true;
      }
      else {
        GameApp.usVibrate = false;
      }
    }
    if (GameUISettings.stBackLight != null) {
      if (GameUISettings.stBackLight.getSelectedIndex() == 0) {
        GameApp.usBackLight = true;
      }
      else {
        GameApp.usBackLight = false;
      }
    }
  }

}
