/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package net.eiroca.j2me.TestSuite;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

/**
 * The Class Processor.
 */
abstract public class Processor {

  /** The map. */
  Map<Category, Object> map = new HashMap<Category, Object>();

  /**
   * Parses the value.
   * 
   * @param val the val
   * 
   * @return the object
   */
  public Object parseValue(final String val) {
    if ("true".equalsIgnoreCase(val)) { return Boolean.TRUE; }
    if ("false".equalsIgnoreCase(val)) { return Boolean.FALSE; }
    try {
      final Integer vl = Integer.valueOf(val);
      return vl;
    }
    catch (final NumberFormatException e) {
    }
    try {
      final Long vl = Long.valueOf(val);
      return vl;
    }
    catch (final NumberFormatException e) {
    }
    return val;
  }

  /**
   * Parses the category.
   * 
   * @param categoryName the category name
   * 
   * @return the category
   */
  public Category parseCategory(final String categoryName) {
    return new Category(categoryName, "_");
  }

  /**
   * Read input.
   * 
   * @param fin the fin
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void readInput(final InputStream fin) throws IOException {
    map.clear();
    final LineInputStream in = new LineInputStream(fin);
    String line;
    String cod;
    String val;
    do {
      line = in.readLine();
      if (line != null) {
        if (line.startsWith("#")) {
          continue;
        }
        final StringTokenizer st = new StringTokenizer(line, "=");
        if (st.hasMoreTokens()) {
          cod = st.nextToken().trim();
          if (st.hasMoreTokens()) {
            val = st.nextToken().trim();
          }
          else {
            val = null;
          }
        }
        else {
          cod = null;
          val = null;
        }
        if (cod == null) {
          System.err.println("Invalid " + line);
        }
        else if (val != null) {
          final Object prpVal = parseValue(val);
          final Category prpNam = parseCategory(cod);
          process(prpNam, prpVal);
        }
      }
    }
    while (line != null);
    in.close();
  }

  /**
   * Missing mapping.
   * 
   * @param missingName the missing name
   * 
   * @return the category
   */
  public Category missingMapping(final Category missingName) {
    return missingName;
  }

  /**
   * Transform.
   * 
   * @param mapping the mapping
   */
  public void transform(final HashMap<Category, Category> mapping) {
    final HashMap<Category, Object> newMap = new HashMap<Category, Object>();
    for (Category name : map.keySet()) {
      final Object val = map.get(name);
      if (mapping.containsKey(name)) {
        name = mapping.get(name);
      }
      else {
        name = missingMapping(name);
      }
      if (name != null) {
        newMap.put(name, val);
      }
    }
    map = newMap;
  }

  /**
   * Gets the max level.
   * 
   * @return the max level
   */
  public int getMaxLevel() {
    int maxLev = 0;
    for (final Category name : map.keySet()) {
      if (name.categories.size() > maxLev) {
        maxLev = name.categories.size();
      }
    }
    return maxLev;
  }

  /**
   * Gets the value.
   * 
   * @param vl the vl
   * 
   * @return the value
   */
  public String getValue(final Object vl) {
    return vl.toString();
  }

  /**
   * Write output.
   * 
   * @param fout the fout
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeOutput(final OutputStream fout) throws IOException {
    final int maxLev = getMaxLevel();
    final BufferedOutputStream out = new BufferedOutputStream(fout);
    final PrintWriter pw = new PrintWriter(out);
    for (final Category name : map.keySet()) {
      final Object vl = map.get(name);
      pw.println(Category.getCategoryName(name.categories, "\t", maxLev) + "\t=\t" + getValue(vl));
    }
    pw.close();
  }

  /**
   * Process.
   * 
   * @param prpNam the prp nam
   * @param prpVal the prp val
   */
  public void process(final Category prpNam, final Object prpVal) {
    map.put(prpNam, prpVal);
  }

}
