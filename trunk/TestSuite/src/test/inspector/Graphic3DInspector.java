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
package test.inspector;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.m3g.Graphics3D;
import net.eiroca.j2me.app.BaseApp;
import test.AbstractInspector;

public class Graphic3DInspector extends AbstractInspector {

  public static final String CATEGORY = "Graphic3D";

  public Graphic3DInspector() {
    super(Graphic3DInspector.CATEGORY);
  }

  public void run() {
    if (BaseApp.isClass("javax.microedition.m3g.Graphics3D")) {
      final Hashtable props = Graphics3D.getProperties();
      Object key;
      Object val;
      for (final Enumeration e = props.keys(); e.hasMoreElements();) {
        key = e.nextElement();
        val = props.get(key);
        addResult("Graphics3D." + key, val);
      }

    }
  }

}
