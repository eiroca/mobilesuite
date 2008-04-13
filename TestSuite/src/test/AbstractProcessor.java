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

abstract public class AbstractProcessor {

  String cat;
  Suite suite;
  String prefix;

  public AbstractProcessor(final String cat, final String prefix) {
    this.cat = cat;
    this.prefix = prefix;
  }

  public void setSuite(final Suite suite) {
    this.suite = suite;
  }

  public void addResult(final String key, final Object val) {
    if (suite != null) {
      final TestResult test = new TestResult();
      test.category = cat;
      test.key = (prefix != null ? prefix + key : key);
      test.val = val;
      suite.addResult(test);
    }
  }

  abstract public void execute();

}
