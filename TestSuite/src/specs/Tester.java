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
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.media.Manager;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.util.Info;

public class Tester extends Thread {

  public static final String CAT_SCREEN = "Screen";
  public static final String CAT_SYSTEM = "System Info";
  public static final String CAT_API = "API";
  public static final String CAT_KEYS = "Keys";
  public static final String CAT_MMEDIA = "Multimedia";
  public static final String CAT_BENCHMARK = "Benchmark";

  public static Vector tests = new Vector();

  private long iResolution = 0;
  private PrecisionThread tSleeper;
  private Canvas canvas;
  private Canvas canvasFull;
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

  private static final String YES = "yes";
  private static final String NO = "no";

  final private void testBool(String cat, String desc, boolean val) {
    addStr(cat, desc, (val ? YES : NO));
  }

  final private void testClas(String cat, String desc, String clas) {
    addStr(cat, desc, (isClass(clas) ? YES : NO));
  }

  final private void testProp(String cat, String desc, String prop) {
    addStr(cat, desc, readProperty(prop, "?"));
  }

  final private void testKey(String cat, String desc, int key) {
    addStr(cat, desc, canvas.getKeyName(canvas.getKeyCode(key)));
  }

  final private void testFont(String cat, String desc, Font f) {
    addStr(cat, desc, Integer.toString(f.getHeight()));
  }

  final private void testInt(String cat, String desc, int x) {
    addStr(cat, desc, Integer.toString(x));
  }

  private void screenInfo() {
    // Screen Info
    Display d = Application.display;
    testInt(Tester.CAT_SCREEN, "Screen (normal) width", canvas.getWidth());
    testInt(Tester.CAT_SCREEN, "Screen (normal) height", canvas.getHeight());
    testInt(Tester.CAT_SCREEN, "Screen (full) width", canvasFull.getWidth());
    testInt(Tester.CAT_SCREEN, "Screen (full) height", canvasFull.getHeight());
    testInt(Tester.CAT_SCREEN, "Color Depth", d.numColors());
    testBool(Tester.CAT_SCREEN, "Is grayscale", d.isColor());
    testInt(Tester.CAT_SCREEN, "Alpha Levels", d.numAlphaLevels());
    testBool(Tester.CAT_SCREEN, "Screen buffered", canvas.isDoubleBuffered());
    testFont(Tester.CAT_SCREEN, "Font Default Height", Font.getDefaultFont());
    testFont(Tester.CAT_SCREEN, "Font Small Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
    testFont(Tester.CAT_SCREEN, "Font Small Bold Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
    testFont(Tester.CAT_SCREEN, "Font Medium Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
    testFont(Tester.CAT_SCREEN, "Font Medium Bold Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
    testFont(Tester.CAT_SCREEN, "Font Large Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
    testFont(Tester.CAT_SCREEN, "Font Large Bold Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
  }

  private void systemInfo() {
    // System
    // get the timezone id
    final TimeZone tz = TimeZone.getDefault();
    final String[] timeZoneIDs = java.util.TimeZone.getAvailableIDs();
    final StringBuffer timeZonesBuffer = new StringBuffer();
    for (int i = 0; i < timeZoneIDs.length; i++) {
      if (i > 0) {
        timeZonesBuffer.append(", ");
      }
      timeZonesBuffer.append(timeZoneIDs[i]);
    }
    Runtime.getRuntime().gc();
    addStr(Tester.CAT_SYSTEM, "Total mem", Long.toString(Runtime.getRuntime().totalMemory()));
    addStr(Tester.CAT_SYSTEM, "Free mem", Long.toString(Runtime.getRuntime().freeMemory()));
    testProp(Tester.CAT_SYSTEM, "Configuration", "microedition.configuration");
    testProp(Tester.CAT_SYSTEM, "Profiles", "microedition.profiles");
    testProp(Tester.CAT_SYSTEM, "Locale", "microedition.locale");
    testProp(Tester.CAT_SYSTEM, "Platform", "microedition.platform");
    testProp(Tester.CAT_SYSTEM, "Char encoding", "microedition.encoding");
    testProp(Tester.CAT_SYSTEM, "Comm Ports", "microedition.commports");
    addStr(Tester.CAT_SYSTEM, "Default Time Zone", tz.getID());
    addStr(Tester.CAT_SYSTEM, "Available Time Zone", timeZonesBuffer.toString());
  }

  private void APIInfo() {
    // API
    final String mmVer = readProperty("microedition.media.version", "");
    String jwVer = System.getProperty("microedition.jtwi.version");
    if (jwVer == null) {
      jwVer = "";
    }
    testClas(Tester.CAT_API, "JSR-135 MMAPI " + mmVer + " - multimedia", "javax.microedition.media.Manager");
    testClas(Tester.CAT_API, "JSR-120 WMAPI 1.1 - messaging", "javax.wireless.messaging.Message");
    testClas(Tester.CAT_API, "JSR-205 WMAPI 2.0 - messaging", "javax.wireless.messaging.MessagePart");
    testClas(Tester.CAT_API, "JSR-082 bluetooth", "javax.bluetooth.LocalDevice");
    testClas(Tester.CAT_API, "JSR-082 bluetooth-obex", "javax.obex.HeaderSet");
    testClas(Tester.CAT_API, "JSR-184 M3G - 3D graphics", "javax.microedition.m3g.Node");
    testClas(Tester.CAT_API, "JSR-118 MIDP2", "javax.microedition.io.HttpsConnection");
    testClas(Tester.CAT_API, "JSR-135 video", "javax.microedition.media.TimeBase");
    testClas(Tester.CAT_API, "JSR-172 web services", "javax.xml.parsers.SAXParser");
    testClas(Tester.CAT_API, "JSR-177 security services", "java.security.Signature");
    testClas(Tester.CAT_API, "JSR-179 location", "javax.microedition.location.Location");
    testClas(Tester.CAT_API, "JSR-180 SIP", "javax.microedition.sip.SipConnection");
    testBool(Tester.CAT_API, "JSR-185 JTWI " + jwVer, (jwVer.length() > 0));
    testClas(Tester.CAT_API, "API - PIM", "javax.microedition.pim.PIM");
    testClas(Tester.CAT_API, "API - FileSystem", "javax.microedition.io.file.FileSystemRegistry");
    testClas(Tester.CAT_API, "Nokia - UI", "com.nokia.mid.ui.DeviceControl");
    testClas(Tester.CAT_API, "Nokia - sound", "com.nokia.mid.sound.Sound");
    testClas(Tester.CAT_API, "Nokia - graphics", "com.nokia.mid.ui.FullCanvas");
    testClas(Tester.CAT_API, "Siemens - UI", "com.siemens.mp.MIDlet");
    testClas(Tester.CAT_API, "Siemens - graphics", "com.siemens.mp.color_game.GameCanvas");
  }

  private void keyInfo() {
    // KEY
    testBool(Tester.CAT_KEYS, "Has pointer events", canvas.hasPointerEvents());
    testBool(Tester.CAT_KEYS, "Has motion events", canvas.hasPointerMotionEvents());
    testBool(Tester.CAT_KEYS, "Has key-held events", canvas.hasRepeatEvents());
    testKey(Tester.CAT_KEYS, "Key GAME_A", Canvas.GAME_A);
    testKey(Tester.CAT_KEYS, "Key GAME_B", Canvas.GAME_B);
    testKey(Tester.CAT_KEYS, "Key GAME_C", Canvas.GAME_C);
    testKey(Tester.CAT_KEYS, "Key GAME_D", Canvas.GAME_D);
    testKey(Tester.CAT_KEYS, "Key UP", Canvas.UP);
    testKey(Tester.CAT_KEYS, "Key DOWN", Canvas.DOWN);
    testKey(Tester.CAT_KEYS, "Key FIRE", Canvas.FIRE);
    testKey(Tester.CAT_KEYS, "Key LEFT", Canvas.LEFT);
    testKey(Tester.CAT_KEYS, "Key RIGTH", Canvas.RIGHT);
  }

  private void multimediaInfo() {
    // MultiMedia
    testProp(Tester.CAT_MMEDIA, "Video capture", "supports.video.capture");
    testProp(Tester.CAT_MMEDIA, "Video encodings", "video.encodings");
    testProp(Tester.CAT_MMEDIA, "Video snapshot encodings", "video.snapshot.encodings");
    testProp(Tester.CAT_MMEDIA, "Audio capture", "supports.audio.capture");
    testProp(Tester.CAT_MMEDIA, "Audio encoding", "audio.encoding");
    testProp(Tester.CAT_MMEDIA, "Supports recording", "supports.recording");
    testProp(Tester.CAT_MMEDIA, "Supports mixing", "supports.mixing");
    testProp(Tester.CAT_MMEDIA, "Streamable contents", "streamable.contents");
    final String[] supportedProtocols = getSupportedProtocols(null);
    if (supportedProtocols != null) {
      for (int i = 0; i < supportedProtocols.length; i++) {
        final String protocol = supportedProtocols[i];
        final String[] supportedContentTypes = getSupportedContentTypes(protocol);
        final StringBuffer buffer = new StringBuffer(32);
        for (int j = 0; j < supportedContentTypes.length; j++) {
          if (j > 0) {
            buffer.append(", ");
          }
          buffer.append(supportedContentTypes[j]);
        }
        addStr(Tester.CAT_MMEDIA, "Protocol " + protocol, buffer.toString());
      }
    }
  }

  public int result;

  public void run() {
    tSleeper = new PrecisionThread();
    tSleeper.start();
    Displayable cur = Application.getDisplay();
    canvas = new TestCanvas(false);
    canvasFull = new TestCanvas(true);
    Application.setDisplay(canvas);
    try {
      Thread.sleep(10);
    }
    catch (InterruptedException e) {
      // ignore
    }
    Application.setDisplay(canvasFull);
    try {
      Thread.sleep(10);
    }
    catch (InterruptedException e) {
      // ignore
    }
    Application.setDisplay(cur);
    try {
      tSleeper.join(); // Retrieve the minimum resolution timers can measure
    }
    catch (final InterruptedException ie) {
      // ignore
    }
    iResolution = tSleeper.iAfter - tSleeper.iBefore;
    screenInfo();
    systemInfo();
    APIInfo();
    keyInfo();
    multimediaInfo();
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

  /**
   * Checks to see if a given class/interface exists in this Java
   * implementation.
   * @param sName the full name of the class
   * @return true if the class/interface exists
   */
  private boolean isClass(final String sName) {
    boolean fFound = false;
    try {
      if (sName != null) {
        Class.forName(sName);
        fFound = true;
      }
    }
    catch (final ClassNotFoundException cnfe) {
      // do nothing
    }
    return fFound;
  }

  /**
   * Retrieves the system property, and returns it, or "unknown" if it is null.
   * @param sName the name of the system property, eg. for System.getProperty
   * @return the contents of the property, never null
   */
  private String readProperty(final String sName, final String def) {
    String sValue = null;
    try {
      sValue = System.getProperty(sName);
    }
    catch (Exception e) {
    }
    return (sValue == null ? def : sValue);
  }

  public String[] getSupportedProtocols(final String contentType) {
    return Manager.getSupportedProtocols(contentType);
  }

  public String[] getSupportedContentTypes(final String protocol) {
    return Manager.getSupportedContentTypes(protocol);
  }

  public static void export(final Form list, final String category) {
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
