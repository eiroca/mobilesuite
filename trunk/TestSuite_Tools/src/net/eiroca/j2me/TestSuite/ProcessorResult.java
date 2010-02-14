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
package net.eiroca.j2me.TestSuite;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.Vector;

/**
 * The Class ProcessorResult.
 */
public class ProcessorResult extends Processor {

  private static String NOWIKI1 = "<nowiki>";
  private static String NOWIKI2 = "</nowiki>";

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#missingMapping(net.eiroca.j2me.TestSuite.Category)
   */
  public Category missingMapping(final Category missingName) {
    System.err.println(Category.getCategoryName(missingName.categories, ".", missingName.categories.size()));
    return null;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#process(net.eiroca.j2me.TestSuite.Category, java.lang.Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public void process(final Category prpNam, final Object prpVal) {
    final Category name = prpNam;
    if (prpNam.index != null) {
      final int pos = prpNam.index.intValue() + 1;
      Vector<Object> props;
      if (!map.containsKey(name)) {
        props = new Vector<Object>();
      }
      else {
        props = (Vector<Object>) map.get(name);
      }
      if (props.size() < pos) {
        props.setSize(pos);
      }
      props.set(pos - 1, prpVal);
    }
    else {
      map.put(name, prpVal);
    }
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#getValue(java.lang.Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public String getValue(final Object vl) {
    final StringBuffer buf = new StringBuffer();
    if (vl instanceof Boolean) {
      buf.append(vl);
    }
    else if (vl instanceof Integer) {
      buf.append(vl);
    }
    else if (vl instanceof Long) {
      buf.append(vl);
    }
    else if (vl instanceof Vector) {
      buf.append(NOWIKI1);
      final Vector<Object> v = (Vector<Object>) vl;
      for (final Object vv : v) {
        if (buf.length() > 0) {
          buf.append(';');
        }
        buf.append(vv);
      }
      buf.append(NOWIKI2);
    }
    else {
      buf.append(NOWIKI1);
      buf.append(vl);
      buf.append(NOWIKI2);
    }
    return buf.toString();
  }

  /**
   * Category wiki.
   * 
   * @param cat the cat
   * @param ff the ff
   * 
   * @return the string
   */
  public String categoryWiki(String cat, String ff) {
    return "".equals(cat) ? " ::: " : ff + cat + ff;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#writeOutput(java.io.OutputStream)
   */
  @Override
  public void writeOutput(final OutputStream fout) throws IOException {
    final TreeMap<Category, Object> sorted = new TreeMap<Category, Object>();
    sorted.putAll(map);
    final BufferedOutputStream out = new BufferedOutputStream(fout);
    final PrintWriter pw = new PrintWriter(out);
    String oldC1 = "\0";
    String oldC2 = "\0";
    String c1;
    String c2;
    String c3;
    for (final Category name : sorted.keySet()) {
      final String val = getValue(map.get(name));
      c1 = name.categories.elementAt(1);
      c2 = name.categories.elementAt(2);
      c3 = (name.categories.size() > 3 ? name.categories.elementAt(3) : "");
      if (c1.equals(oldC1)) {
        c1 = "";
        if (c2.equals(oldC2)) {
          c2 = "";
        }
        else {
          oldC2 = c2;
        }
      }
      else {
        oldC1 = c1;
        oldC2 = c2;
      }
      pw.println("^" + categoryWiki(c1, "") + "|" + categoryWiki(c2, "//") + "|" + c3 + "|" + val + "|");
    }
    pw.close();
  }

}
