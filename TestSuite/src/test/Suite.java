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
package test;

import java.util.Vector;

import javax.microedition.lcdui.Form;

import test.inspector.APIsInspector;
import test.inspector.CanvasInspector;
import test.inspector.MultimediaInspector;
import test.inspector.PropertyInspector;
import test.inspector.SystemInspector;

import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.Pair;

public class Suite {

  public static final String MAPPING = "/mapping.txt";

  private Vector tests = new Vector();
  private Pair[] mapping = null;
  private AbstractInspector[] t;

  public Suite() {
    t = new AbstractInspector[5];
    t[0] = new PropertyInspector();
    t[1] = new APIsInspector();
    t[2] = new CanvasInspector();
    t[3] = new SystemInspector();
    t[4] = new MultimediaInspector();
    for (int i = 0; i < t.length; i++) {
      t[i].setSuite(this);
    }
  }

  public void run() {
    for (int i = 0; i < t.length; i++) {
      t[i].run();
    }
  }

  public void addResult(TestResult test) {
    tests.addElement(test);
  }

  public String getDesc(String key) {
    String res = key;
    if (mapping != null) {
      for (int i = 0; i < mapping.length; i++) {
        if (key.equals(mapping[i].name)) {
          res = mapping[i].value.toString();
          break;
        }
      }
    }
    return res;
  }

  public void export(final Form list, final String category) {
    if (mapping == null) {
      mapping = Application.readPairs(MAPPING, '=');
    }
    for (int i = 0; i < tests.size(); i++) {
      final TestResult inf = (TestResult) tests.elementAt(i);
      if (inf.category.equals(category)) {
        if (inf.val != null) {
          list.append(getDesc(inf.key) + '=' + inf.val + '\n');
        }
      }
    }
  }

}