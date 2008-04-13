/** MIT LICENSE
 * Based upon Mobile Device Tools written by Andrew Scott
 *
 * Copyright (C) 2004 Andrew Scott
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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

public class BenchmarkADD extends MathBenchmarkAbstract {

  public static final String RESID = "add.";

  public BenchmarkADD(final SuiteAbstract suite) {
    super(suite);
  }

  public void execute() {
    long before;
    long after;
    long elapsed;
    // Array SUM
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS / 100; i++) {
      for (int j = 0; j < 100; j++) {
        result = arrayA[j] + arrayB[j];
      }
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkADD.RESID + "array", Long.toString(elapsed));
    // Local SUM
    final int localA = random.nextInt();
    final int localB = random.nextInt();
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS; i++) {
      result = localA + localB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkADD.RESID + "locals", Long.toString(elapsed));
    // Instance SUM
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS; i++) {
      result = instanceA + instanceB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkADD.RESID + "instance", Long.toString(elapsed));
    // Static SUM
    before = System.currentTimeMillis();
    for (int i = 0; i < MathBenchmarkAbstract.NUMBER_OF_OPS; i++) {
      result = MathBenchmarkAbstract.staticA + MathBenchmarkAbstract.staticB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      result = 1;
    }
    elapsed = after - before;
    suite.addResult(BenchmarkADD.RESID + "static", Long.toString(elapsed));
  }

}
