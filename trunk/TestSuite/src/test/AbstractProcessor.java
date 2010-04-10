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
package test;

/**
 * The Class AbstractProcessor.
 */
abstract public class AbstractProcessor {

  /** The cat. */
  String cat;
  
  /** The suite. */
  Suite suite;
  
  /** The prefix. */
  String prefix;

  /**
   * Instantiates a new abstract processor.
   * 
   * @param cat the cat
   * @param prefix the prefix
   */
  public AbstractProcessor(final String cat, final String prefix) {
    this.cat = cat;
    this.prefix = prefix;
  }

  /**
   * Sets the suite.
   * 
   * @param suite the new suite
   */
  public void setSuite(final Suite suite) {
    this.suite = suite;
  }

  /**
   * Adds the result.
   * 
   * @param key the key
   * @param val the val
   */
  public void addResult(final String key, final Object val) {
    if (suite != null) {
      final TestResult test = new TestResult();
      test.category = cat;
      test.key = (prefix != null ? prefix + key : key);
      test.val = val;
      suite.addResult(test);
    }
  }

  /**
   * Execute.
   */
  abstract public void execute();

}
