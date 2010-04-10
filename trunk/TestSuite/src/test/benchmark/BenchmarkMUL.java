/** GPL >= 3.0 + MIT
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
 * 
 * Copyright (C) 2004 Andrew Scott
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package test.benchmark;

/**
 * The Class BenchmarkMUL.
 */
public class BenchmarkMUL extends MathBenchmarkAbstract {

  /** The Constant RES_ID1. */
  public static final String RES_ID1 = "M.a";
  
  /** The Constant RES_ID2. */
  public static final String RES_ID2 = "M.l";
  
  /** The Constant RES_ID3. */
  public static final String RES_ID3 = "M.i";
  
  /** The Constant RES_ID4. */
  public static final String RES_ID4 = "M.s";

  /**
   * Instantiates a new benchmark mul.
   * 
   * @param suite the suite
   */
  public BenchmarkMUL(final SuiteAbstract suite) {
    super(suite);
  }

  /* (non-Javadoc)
   * @see test.benchmark.BenchmarkAbstract#execute()
   */
  public void execute() {
    long before;
    long after;
    long elapsed;
    // Array MUL
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS / 100; i++) {
      for (int j = 0; j < 100; j++) {
        result = arrayA[j] * arrayB[j];
      }
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkMUL.RES_ID1, Long.toString(elapsed));
    // Local MUL
    final int localA = random.nextInt();
    final int localB = random.nextInt();
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS; i++) {
      result = localA * localB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkMUL.RES_ID2, Long.toString(elapsed));
    // Instance MUL
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS; i++) {
      result = instanceA * instanceB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkMUL.RES_ID3, Long.toString(elapsed));
    // Static MUL
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS; i++) {
      result = MathBenchmarkAbstract.staticA * MathBenchmarkAbstract.staticB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkMUL.RES_ID4, Long.toString(elapsed));
  }

}
