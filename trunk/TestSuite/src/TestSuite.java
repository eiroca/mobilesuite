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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import keys.KeyStateCanvas;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.observable.Observable;
import net.eiroca.j2me.observable.Observer;
import net.eiroca.j2me.util.HTTPClient;
import test.DataSender;
import test.Suite;
import test.benchmark.MathSuite;
import test.benchmark.PrecisionSuite;
import test.inspector.APIsInspector;
import test.inspector.CanvasInspector;
import test.inspector.Graphic3DInspector;
import test.inspector.MultimediaInspector;
import test.inspector.PrivacyPropertyInspector;
import test.inspector.PropertyInspector;
import test.inspector.SystemInspector;
import classbrowser.ClassBrowserHelper;

public final class TestSuite extends Application implements Observer {

  public static final int MSG_APPLICATION = 0;
  public static final int MSG_OK = 1;
  public static final int MSG_BACK = 2;
  public static final int MSG_EXIT = 3;
  public static final int MSG_PREV = 4;
  public static final int MSG_ABOUT = 5;

  static final int COUNT = 15;

  private final String[] INSPECTOR_CAT = new String[] {
      PropertyInspector.CATEGORY, APIsInspector.CATEGORY, CanvasInspector.CATEGORY, SystemInspector.CATEGORY, MultimediaInspector.CATEGORY, Graphic3DInspector.CATEGORY,
      PrivacyPropertyInspector.CATEGORY
  };
  private final String[] BENCHMARK_CAT = new String[] {
      PrecisionSuite.CATEGORY, MathSuite.CATEGORY
  };

  private Command cPREV;
  private List fMenu;
  private List fMenuInspector;
  private List fMenuBenchmark;
  private Form fSpec;
  private Form fPostData;
  private Canvas fKeyState;
  private List fClassBrowser;
  private Displayable fAbout;

  /** The full path name to the level of class hierarchy being shown. */
  private String cbPackagePath;
  private Suite suite;
  private String[] classes;

  private String url;
  private String ver;
  private String[] menuDesc;

  public TestSuite() {
    super();
  }

  public boolean handleAction(final int action, final Displayable d, final Command cmd) {
    return false;
  }

  /**
   * @param c
   * @param d
   */
  public void commandAction(final Command c, final Displayable d) {
    boolean processed = false;
    if (d == fClassBrowser) {
      if (c == List.SELECT_COMMAND) {
        processed = true;
        final int iIdx = fClassBrowser.getSelectedIndex();
        final String sPackageElement = fClassBrowser.getString(iIdx);
        // package elements always begin with a lower-case letter..
        if ((sPackageElement != null) && Character.isLowerCase(sPackageElement.charAt(0))) {
          // set the new package path
          if ((cbPackagePath == null) || (cbPackagePath.length() == 0)) {
            cbPackagePath = sPackageElement;
          }
          else {
            cbPackagePath = cbPackagePath + "." + sPackageElement;
          }
          // redraw the screen object
          loadClasses();
        }
      }
      else if (c == cPREV) {
        processed = true;
        // strip off the last bit of the package path
        final int iPos = cbPackagePath.lastIndexOf('.');
        if (iPos == -1) {
          cbPackagePath = ""; // back at the top level
        }
        else {
          cbPackagePath = cbPackagePath.substring(0, iPos);
        }
        // redraw the screen object
        loadClasses();
      }
    }
    else if (d == fMenuInspector) {
      if (c == List.SELECT_COMMAND) {
        processed = true;
        fSpec.deleteAll();
        final int i = fMenuInspector.getSelectedIndex();
        fSpec.setTitle("Inspector: " + INSPECTOR_CAT[i]);
        if (suite != null) {
          suite.export(fSpec, INSPECTOR_CAT[i]);
        }
        BaseApp.show(null, fSpec, true);
      }
    }
    if (d == fMenuBenchmark) {
      if (c == List.SELECT_COMMAND) {
        processed = true;
        fSpec.deleteAll();
        final int i = fMenuBenchmark.getSelectedIndex();
        fSpec.setTitle("Benchmark: " + BENCHMARK_CAT[i]);
        if (suite != null) {
          suite.benchmark(fSpec, BENCHMARK_CAT[i]);
        }
        BaseApp.show(null, fSpec, true);
      }
    }
    else if (d == fPostData) {
      if (c == BaseApp.cOK) {
        processed = true;
        final DataSender ds = new DataSender(suite, ver);
        ds.addObserver(this);
        ds.submit(url);
        BaseApp.back(null);
      }
    }
    if (!processed) {
      super.commandAction(c, d);
    }
  }

  /**
   * init
   */
  protected void init() {
    super.init();
    try {
      url = readAppProperty("POSTURL", "http://www.eiroca.net/services/testsuite/store.php");
      ver = readAppProperty("MIDlet-Version", "1.0.0");
      BaseApp.messages = BaseApp.readStrings("messages.txt");
      classes = BaseApp.readStrings("classes.txt");
      ClassBrowserHelper.imPlus = BaseApp.createImage("Plus.png");
      ClassBrowserHelper.imDash = BaseApp.createImage("Dash.png");
      final Image[] icons = new Image[6];
      icons[0] = BaseApp.createImage("Specs.png");
      icons[1] = icons[0];
      icons[2] = BaseApp.createImage("ClassBrowser.png");
      icons[3] = BaseApp.createImage("Keys.png");
      icons[4] = ClassBrowserHelper.imDash;
      icons[5] = BaseApp.createImage("icon.png");
      suite = new Suite();
      suite.run();
      menuDesc = new String[] {
          "Inspectors", "Benchmark", "Class browser", "Keys", "Post data", "About"
      };
      fMenu = new List("Main Menu", Choice.IMPLICIT, menuDesc, icons);
      BaseApp.cOK = BaseApp.newCommand(TestSuite.MSG_OK, Command.OK, 30);
      BaseApp.cBACK = BaseApp.newCommand(TestSuite.MSG_BACK, Command.BACK, 20, BaseApp.AC_BACK);
      BaseApp.cEXIT = BaseApp.newCommand(TestSuite.MSG_EXIT, Command.EXIT, 10, BaseApp.AC_EXIT);
      cPREV = BaseApp.newCommand(TestSuite.MSG_PREV, Command.SCREEN, 1);
      fMenuInspector = new List("Inspectors", Choice.IMPLICIT, INSPECTOR_CAT, null);
      fMenuBenchmark = new List("Benchmark", Choice.IMPLICIT, BENCHMARK_CAT, null);
      fClassBrowser = new List("", Choice.IMPLICIT);
      fSpec = new Form("");
      fKeyState = new KeyStateCanvas();
      fPostData = getPostDataForm();
      fAbout = BaseApp.getTextForm(TestSuite.MSG_ABOUT, "about.txt");
      BaseApp.registerListItem(fMenu, 0, fMenuInspector);
      BaseApp.registerListItem(fMenu, 1, fMenuBenchmark);
      BaseApp.registerListItem(fMenu, 2, fClassBrowser);
      BaseApp.registerListItem(fMenu, 3, fKeyState);
      BaseApp.registerListItem(fMenu, 4, fPostData);
      BaseApp.registerListItem(fMenu, 5, fAbout);
      BaseApp.setup(fMenu, BaseApp.cEXIT, null);
      BaseApp.setup(fMenuInspector, BaseApp.cBACK, null);
      BaseApp.setup(fMenuBenchmark, BaseApp.cBACK, null);
      BaseApp.setup(fKeyState, BaseApp.cBACK, null);
      BaseApp.setup(fClassBrowser, cPREV, BaseApp.cBACK);
      BaseApp.setup(fSpec, BaseApp.cBACK, null);
      cbPackagePath = "";
      loadClasses();
    }
    catch (final Exception e) {
      fMenu.setTitle(e.getMessage());
    }
    BaseApp.show(null, fMenu, true);
  }

  private Form getPostDataForm() {
    final Form f = new Form("Post Data");
    f.append("Post data to server " + url);
    BaseApp.setup(f, BaseApp.cBACK, BaseApp.cOK);
    return f;
  }

  public void loadClasses() {
    ClassBrowserHelper.generateList(fClassBrowser, cbPackagePath, classes);
    fClassBrowser.setCommandListener(this);
    if ((cbPackagePath == null) || (cbPackagePath.length() == 0)) {
      fClassBrowser.setTitle("Top Level");
      fClassBrowser.removeCommand(cPREV);
    }
    else {
      fClassBrowser.setTitle(cbPackagePath);
      fClassBrowser.addCommand(cPREV);
    }
  }

  public void notify(final Observable observable) {
    if (observable instanceof DataSender) {
      final DataSender ds = (DataSender) observable;
      fMenu.set(4, menuDesc[4] + " (" + ds.getStatus() + ")", ClassBrowserHelper.imPlus);
    }
    else if (observable instanceof HTTPClient) {
      final HTTPClient ds = (HTTPClient) observable;
      fMenu.set(4, menuDesc[4] + " (" + ds.getStatus() + ")", ClassBrowserHelper.imPlus);
    }
  }

}
