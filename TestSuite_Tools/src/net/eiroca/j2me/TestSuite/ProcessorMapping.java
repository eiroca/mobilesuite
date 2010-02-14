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
import java.util.HashMap;

/**
 * The Class ProcessorMapping.
 */
public class ProcessorMapping extends Processor {

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#parseValue(java.lang.String)
   */
  @Override
  public Object parseValue(final String val) {
    return new Category(val, "|");
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#parseCategory(java.lang.String)
   */
  @Override
  public Category parseCategory(final String categoryName) {
    return new Category(categoryName, ".");
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.TestSuite.Processor#writeOutput(java.io.OutputStream)
   */
  @Override
  public void writeOutput(final OutputStream fout) throws IOException {
    final BufferedOutputStream out = new BufferedOutputStream(fout);
    final PrintWriter pw = new PrintWriter(out);
    for (final Category name : map.keySet()) {
      final Category vl = (Category) map.get(name);
      pw.println(Category.getCategoryName(name.categories, ".", name.categories.size()) + "=" + Category.getCategoryName(vl.categories, "|", vl.categories.size()));
    }
    pw.close();
  }

  /**
   * Gets the mapping.
   * 
   * @return the mapping
   */
  public HashMap<Category, Category> getMapping() {
    final HashMap<Category, Category> mapping = new HashMap<Category, Category>();
    for (final Category name : map.keySet()) {
      final Category cat = (Category) map.get(name);
      mapping.put(name, cat);
    }
    return mapping;
  }
}
