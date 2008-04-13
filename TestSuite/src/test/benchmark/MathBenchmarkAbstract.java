package test.benchmark;

import java.util.Random;

abstract public class MathBenchmarkAbstract extends BenchmarkAbstract {

  public int result;

  protected final static int NUMBER_OF_OPS = 5000000;

  protected final int arrayA[];
  protected final int arrayB[];

  protected static int staticA;
  protected static int staticB;

  protected int instanceA;
  protected int instanceB;

  protected final Random random = new Random();

  public MathBenchmarkAbstract(final SuiteAbstract suite) {
    super(suite);
    do {
      instanceA = random.nextInt();
    }
    while (instanceA == 0);
    do {
      instanceB = random.nextInt();
    }
    while (instanceB == 0);
    do {
      MathBenchmarkAbstract.staticA = random.nextInt();
    }
    while (MathBenchmarkAbstract.staticA == 0);
    do {
      MathBenchmarkAbstract.staticB = random.nextInt();
    }
    while (MathBenchmarkAbstract.staticB == 0);
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

}
