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
import net.eiroca.j2me.util.HTTPAttach;
import net.eiroca.j2me.util.HTTPClient;
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

public class Suite implements HTTPAttach {

  public static final String MAPPING = "/mapping.txt";
  public static final String VERSION = "1.0.0";

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
      if (benchmarks[i] != null) {
        benchmarks[i].execute();
      }
    }
    for (int i = 0; i < inspectors.length; i++) {
      if (inspectors[i] != null) {
        inspectors[i].execute();
      }
    }
  }

  public void addResult(final TestResult test) {
    if (test != null) {
      tests.addElement(test);
    }
  }

  public String getDesc(final String key) {
    if (mapping == null) {
      mapping = BaseApp.readPairs(Suite.MAPPING, '=');
      if (mapping == null) {
        mapping = new Pair[0];
      }
    }
    String res = key;
    if (key != null) {
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
    StringBuffer sb;
    for (int i = 0; i < tests.size(); i++) {
      final TestResult inf = (TestResult) tests.elementAt(i);
      if (inf.category.equals(category)) {
        if (inf.val != null) {
          sb = new StringBuffer(40);
          sb.append(getDesc(inf.key)).append('=').append(inf.val).append('\n');
          list.append(sb.toString());
        }
      }
    }
  }

  public void writeData(final HTTPClient ss) {
    for (int i = 0; i < tests.size(); i++) {
      final TestResult inf = (TestResult) tests.elementAt(i);
      final String v = (inf.val == null ? "" : inf.val.toString());
      ss.addParameter(inf.key.toString(), v);
    }
  }

  public byte[] getData() {
    StringBuffer buf = new StringBuffer(8192);
    for (int i = 0; i < tests.size(); i++) {
      final TestResult inf = (TestResult) tests.elementAt(i);
      final String v = (inf.val == null ? "" : inf.val.toString());
      buf.append(inf.key).append('=').append(v).append('\n');
    }
    return buf.toString().getBytes();
  }

  public String getMimeType() {
    return "text/plain";
  }

}