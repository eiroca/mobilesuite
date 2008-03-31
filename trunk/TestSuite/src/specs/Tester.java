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
package specs;

import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Form;

import net.eiroca.j2me.util.Info;

public class Tester extends Thread {

  public static final String CAT_BENCHMARK = "Benchmark";

  public static Vector tests = new Vector();

  private long iResolution = 0;
  private PrecisionThread tSleeper;

  private static boolean finished = false;

  private final static int NUMBER_OF_OPS = 10000000;

  private final int arrayA[];
  private final int arrayB[];

  private static int staticA;
  private static int staticB;

  private int instanceA;
  private int instanceB;

  private final Random random = new Random();

  public Tester() {
    do {
      instanceA = random.nextInt();
    }
    while (instanceA == 0);
    do {
      instanceB = random.nextInt();
    }
    while (instanceB == 0);
    do {
      Tester.staticA = random.nextInt();
    }
    while (Tester.staticA == 0);
    do {
      Tester.staticB = random.nextInt();
    }
    while (Tester.staticB == 0);
    arrayA = new int[100];
    arrayB = new int[100];
    final Random r = new Random();
    for (int i = 0; i < 100; i++) {
      do {
        arrayA[i] = r.nextInt();
      }
      while (arrayA[i] == 0);
      do {
        arrayB[i] = r.nextInt();
      }
      while (arrayB[i] == 0);
    }
  }

  public int result;

  public void run() {
    tSleeper = new PrecisionThread();
    tSleeper.start();

    try {
      tSleeper.join(); // Retrieve the minimum resolution timers can measure
    }
    catch (final InterruptedException ie) {
      // ignore
    }
    iResolution = tSleeper.iAfter - tSleeper.iBefore;
    // Benchmark
    addStr(Tester.CAT_BENCHMARK, "Timer res. est.", iResolution + "ms");
    result = 0;
    result += performAdditionBenchmark();
    result += performDivisionBenchmark();
    result += performMultiplicationBenchmark();
    Tester.finished = true;
  }

  public void addStr(final String category, final String name, final String value) {
    Tester.tests.addElement(new Info(category, name, value));
  }

  public void export(final Form list, final String category) {
    if (!Tester.finished) {
      list.append("... still working ...\n");
    }
    for (int i = 0; i < Tester.tests.size(); i++) {
      final Info inf = (Info) Tester.tests.elementAt(i);
      if (inf.category == category) {
        list.append(inf.toString() + "\n");
      }
    }
  }

  private int performAdditionBenchmark() {
    long before;
    long after;
    int res = 0;
    // Array SUM
    before = System.currentTimeMillis();
    int result = 0;
    for (int i = 0; i < Tester.NUMBER_OF_OPS / 100; i++) {
      for (int j = 0; j < 100; j++) {
        result = arrayA[j] + arrayB[j];
      }
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedArray = after - before;
    // Local SUM
    final int localA = random.nextInt();
    final int localB = random.nextInt();
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = localA + localB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedLocal = after - before;
    // Instance SUM
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = instanceA + instanceB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedInstance = after - before;
    // Static SUM
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = Tester.staticA + Tester.staticB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedStatic = after - before;
    addStr(Tester.CAT_BENCHMARK, "add of array values", elapsedArray + " ms");
    addStr(Tester.CAT_BENCHMARK, "add of locals", elapsedLocal + " ms");
    addStr(Tester.CAT_BENCHMARK, "add of instance variables ", elapsedInstance + " ms");
    addStr(Tester.CAT_BENCHMARK, "add of static variables ", elapsedStatic + " ms");
    return res;
  }

  private int performMultiplicationBenchmark() {
    long before;
    long after;
    int result = 0;
    int res = 0;
    // Array MUL
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS / 100; i++) {
      for (int j = 0; j < 100; j++) {
        result = arrayA[j] * arrayB[j];
      }
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedArray = after - before;
    // Local MUL
    final int localA = random.nextInt();
    final int localB = random.nextInt();
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = localA * localB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedLocal = after - before;
    // Instance MUL
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = instanceA * instanceB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedInstance = after - before;
    // Static MUL
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = Tester.staticA * Tester.staticB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedStatic = after - before;
    addStr(Tester.CAT_BENCHMARK, "mul of array values", elapsedArray + " ms");
    addStr(Tester.CAT_BENCHMARK, "mul of locals", elapsedLocal + " ms");
    addStr(Tester.CAT_BENCHMARK, "mul of instance variables ", elapsedInstance + " ms");
    addStr(Tester.CAT_BENCHMARK, "mul of static variables ", elapsedStatic + " ms");
    return res;
  }

  private int performDivisionBenchmark() {
    long before;
    long after;
    int res = 0;
    // Array DIV
    before = System.currentTimeMillis();
    int result = 0;
    for (int i = 0; i < Tester.NUMBER_OF_OPS / 100; i++) {
      for (int j = 0; j < 100; j++) {
        result = arrayA[j] / arrayB[j];
      }
    }
    if (result > 0) {
      res = 1;
    }
    after = System.currentTimeMillis();
    final long elapsedArray = after - before;
    // Local DIV
    int localA;
    int localB;
    do {
      localA = random.nextInt();
    }
    while (localA == 0);
    do {
      localB = random.nextInt();
    }
    while (localB == 0);
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = localA / localB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedLocal = after - before;
    // Instance DIV
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = instanceA / instanceB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedInstance = after - before;
    // Static DIV
    before = System.currentTimeMillis();
    for (int i = 0; i < Tester.NUMBER_OF_OPS; i++) {
      result = Tester.staticA / Tester.staticB;
    }
    after = System.currentTimeMillis();
    if (result > 0) {
      res = 1;
    }
    final long elapsedStatic = after - before;
    addStr(Tester.CAT_BENCHMARK, "div of array values", elapsedArray + " ms");
    addStr(Tester.CAT_BENCHMARK, "div of locals", elapsedLocal + " ms");
    addStr(Tester.CAT_BENCHMARK, "div of instance variables ", elapsedInstance + " ms");
    addStr(Tester.CAT_BENCHMARK, "div of static variables ", elapsedStatic + " ms");
    return res;
  }

}
