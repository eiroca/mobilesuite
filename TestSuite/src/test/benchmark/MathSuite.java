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
package test.benchmark;

/**
 * The Class MathSuite.
 */
public class MathSuite extends SuiteAbstract {

  /** The Constant PREFIX. */
  public static final String PREFIX = "B.M.";

  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Math";

  /**
   * Instantiates a new math suite.
   */
  public MathSuite() {
    super(MathSuite.CATEGORY, MathSuite.PREFIX);
    benchmark = new BenchmarkAbstract[3];
    benchmark[0] = new BenchmarkADD(this);
    benchmark[1] = new BenchmarkMUL(this);
    benchmark[2] = new BenchmarkDIV(this);
  }

}
