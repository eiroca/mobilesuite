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

import java.util.Hashtable;

public class Device {

  private static Device self = null;

  public static final String LOCALE = "lo";
  public static final String PLATFORM = "pl";
  public static Hashtable prop = new Hashtable();

  private Device() {
    String locale = BaseApp.readProperty("microedition.locale", null);
    if (locale != null) {
      final int ps = locale.indexOf("-");
      if (ps > 0) {
        locale = locale.substring(0, ps);
      }
      if (locale.length() == 0) {
        locale = null;
      }
      if (locale != null) {
        Device.prop.put(Device.LOCALE, locale);
      }
    }
    final String platform = BaseApp.readProperty("microedition.platform", null);
    Device.prop.put(Device.PLATFORM, platform);
  }

  public static void init() {
    if (Device.self == null) {
      Device.self = new Device();
    }
  }

  public static final String getLocale() {
    return (String) Device.prop.get(Device.LOCALE);
  }

  public static final String getPlatform() {
    return (String) Device.prop.get(Device.PLATFORM);
  }

}
