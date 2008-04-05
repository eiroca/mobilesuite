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
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.Pair;
import test.benchmark.MathSuite;
import test.benchmark.PrecisionSuite;
import test.benchmark.SuiteAbstract;
import test.inspector.APIsInspector;
import test.inspector.CanvasInspector;
import test.inspector.Graphic3DInspector;
import test.inspector.LocalDeviceInspector;
import test.inspector.MultimediaInspector;
import test.inspector.PrivacyPropertyInspector;
import test.inspector.PropertyInspector;
import test.inspector.SystemInspector;

public class Suite {

  public static final String MAPPING = "/mapping.txt";

  private boolean finished = false;
  private final Vector tests = new Vector();
  private Pair[] mapping = null;
  private final AbstractProcessor[] inspectors;
  private final SuiteAbstract[] benchmarks;

  public Suite() {
    inspectors = new AbstractProcessor[8];
    benchmarks = new SuiteAbstract[2];
    inspectors[0] = new CanvasInspector();
    inspectors[1] = new PropertyInspector();
    inspectors[2] = new APIsInspector();
    inspectors[3] = new SystemInspector();
    inspectors[4] = new MultimediaInspector();
    inspectors[5] = new PrivacyPropertyInspector();
    inspectors[6] = new LocalDeviceInspector();
    inspectors[7] = new Graphic3DInspector();
    benchmarks[0] = new PrecisionSuite();
    benchmarks[1] = new MathSuite();
    for (int i = 0; i < inspectors.length; i++) {
      if (inspectors[i] != null) {
        inspectors[i].setSuite(this);
      }
    }
    for (int i = 0; i < benchmarks.length; i++) {
      if (benchmarks[i] != null) {
        benchmarks[i].setSuite(this);
      }
    }
  }

  public void run() {
    finished = false;
    for (int i = 0; i < benchmarks.length; i++) {
      benchmarks[i].execute();
    }
    for (int i = 0; i < inspectors.length; i++) {
      inspectors[i].execute();
    }
  }

  public void addResult(final TestResult test) {
    tests.addElement(test);
  }

  public String getDesc(final String key) {
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

  public void benchmark(final Form list, final String category) {
    if (!finished) {
      finished = true;
      for (int i = 0; i < benchmarks.length; i++) {
        if (!benchmarks[i].finished) {
          finished = false;
          break;
        }
      }
    }
    export(list, category);
    if (!finished) {
      list.append("... still working ...\n");
    }
  }

  public void export(final Form list, final String category) {
    if (mapping == null) {
      mapping = BaseApp.readPairs(Suite.MAPPING, '=');
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
