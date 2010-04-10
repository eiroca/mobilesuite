/** GPL >= 3.0 + MIT
 * Based upon Mobile Device Tools written by Andrew Scott
 * 
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
import test.DataSender;
import test.Suite;
import test.benchmark.MathSuite;
import test.benchmark.PrecisionSuite;
import test.inspector.APIsInspector;
import test.inspector.CanvasInspector;
import test.inspector.Graphic3DInspector;
import test.inspector.LocalDeviceInspector;
import test.inspector.MultimediaInspector;
import test.inspector.PrivacyPropertyInspector;
import test.inspector.PropertyInspector;
import test.inspector.SystemInspector;
import classbrowser.ClassBrowserHelper;

/**
 * The Class TestSuite.
 */
public final class TestSuite extends Application implements Observer {

  /** The Constant MSG_APPLICATION. */
  public static final int MSG_APPLICATION = 0;
  
  /** The Constant MSG_OK. */
  public static final int MSG_OK = 1;
  
  /** The Constant MSG_BACK. */
  public static final int MSG_BACK = 2;
  
  /** The Constant MSG_EXIT. */
  public static final int MSG_EXIT = 3;
  
  /** The Constant MSG_PREV. */
  public static final int MSG_PREV = 4;
  
  /** The Constant MSG_ABOUT. */
  public static final int MSG_ABOUT = 5;

  /** The Constant AC_SHOWINSPECTOR. */
  private static final int AC_SHOWINSPECTOR = 1;
  
  /** The Constant AC_SHOWBENCHMARK. */
  private static final int AC_SHOWBENCHMARK = 2;
  
  /** The Constant AC_SHOWCLASSBROWSER. */
  private static final int AC_SHOWCLASSBROWSER = 3;
  
  /** The Constant AC_SHOWKEYSTATE. */
  private static final int AC_SHOWKEYSTATE = 4;
  
  /** The Constant AC_SHOWPOSTDATA. */
  private static final int AC_SHOWPOSTDATA = 5;
  
  /** The Constant AC_DOABOUT. */
  private static final int AC_DOABOUT = 6;
  
  /** The Constant AC_OK. */
  private static final int AC_OK = 7;
  
  /** The Constant AC_PREV. */
  private static final int AC_PREV = 8;
  
  /** The Constant AC_DOINSPECTOR. */
  private static final int AC_DOINSPECTOR = 9;
  
  /** The Constant AC_DOBENCHMARK. */
  private static final int AC_DOBENCHMARK = 10;
  
  /** The Constant AC_OPENCLASS. */
  private static final int AC_OPENCLASS = 11;

  /** The Constant COUNT. */
  static final int COUNT = 15;

  /** The INSPECTO r_ cat. */
  private final String[] INSPECTOR_CAT = new String[] {
      PropertyInspector.CATEGORY, APIsInspector.CATEGORY, CanvasInspector.CATEGORY, SystemInspector.CATEGORY, MultimediaInspector.CATEGORY, LocalDeviceInspector.CATEGORY, Graphic3DInspector.CATEGORY,
      PrivacyPropertyInspector.CATEGORY
  };
  
  /** The BENCHMAR k_ cat. */
  private final String[] BENCHMARK_CAT = new String[] {
      PrecisionSuite.CATEGORY, MathSuite.CATEGORY
  };

  /** The c prev. */
  private Command cPREV;
  
  /** The menu. */
  private List fMenu;

  /** The full path name to the level of class hierarchy being shown. */
  private String cbPackagePath;
  
  /** The suite. */
  private Suite suite;
  
  /** The classes. */
  private String[] classes;

  /** The url. */
  private String url;
  
  /** The menu desc. */
  private String[] menuDesc;

  /**
   * Instantiates a new test suite.
   */
  public TestSuite() {
    super();
    BaseApp.resPrefix = "te";
  }

  /** The about. */
  private Displayable fAbout;
  
  /** The key state. */
  private Canvas fKeyState;
  
  /** The post data. */
  private Form fPostData;
  
  /** The class browser. */
  private List fClassBrowser;
  
  /** The menu inspector. */
  private List fMenuInspector;
  
  /** The menu benchmark. */
  private List fMenuBenchmark;

  /* (non-Javadoc)
   * @see net.eiroca.j2me.app.Application#handleAction(int, javax.microedition.lcdui.Displayable, javax.microedition.lcdui.Command)
   */
  public boolean handleAction(final int action, final Displayable d, final Command cmd) {
    boolean processed = true;
    int i;
    Form fSpec;
    switch (action) {
      case AC_OK:
        processed = false;
        if (d == fPostData) {
          processed = true;
          final DataSender ds = new DataSender(suite);
          ds.addObserver(this);
          ds.submit(url, 400);
          Application.back(null);
        }
        break;
      case AC_SHOWINSPECTOR:
        if (fMenuInspector == null) {
          fMenuInspector = new List("Inspectors", Choice.IMPLICIT, INSPECTOR_CAT, null);
          Application.setup(fMenuInspector, Application.cBACK, null);
          Application.registerList(fMenuInspector, TestSuite.AC_DOINSPECTOR);
        }
        Application.show(null, fMenuInspector, true);
        break;
      case AC_SHOWBENCHMARK:
        if (fMenuBenchmark == null) {
          fMenuBenchmark = new List("Benchmark", Choice.IMPLICIT, BENCHMARK_CAT, null);
          Application.setup(fMenuBenchmark, Application.cBACK, null);
          Application.registerList(fMenuBenchmark, TestSuite.AC_DOBENCHMARK);
        }
        Application.show(null, fMenuBenchmark, true);
        break;
      case AC_SHOWCLASSBROWSER:
        if (fClassBrowser == null) {
          ClassBrowserHelper.imPlus = BaseApp.createImage("Plus.png");
          ClassBrowserHelper.imDash = BaseApp.createImage("Dash.png");
          classes = BaseApp.readStrings("classes.txt");
          fClassBrowser = new List("", Choice.IMPLICIT);
          cbPackagePath = "";
          loadClasses();
          Application.setup(fClassBrowser, cPREV, Application.cBACK);
          Application.registerList(fClassBrowser, TestSuite.AC_OPENCLASS);
        }
        Application.show(null, fClassBrowser, true);
        break;
      case AC_SHOWKEYSTATE:
        if (fKeyState == null) {
          fKeyState = new KeyStateCanvas();
          Application.setup(fKeyState, Application.cBACK, null);
        }
        Application.show(null, fKeyState, true);
        break;
      case AC_SHOWPOSTDATA:
        if (fPostData == null) {
          fPostData = getPostDataForm();
        }
        Application.show(null, fPostData, true);
        break;
      case AC_DOABOUT:
        if (fAbout == null) {
          fAbout = Application.getTextForm(TestSuite.MSG_ABOUT, "about.txt");
        }
        Application.show(null, fAbout, true);
        break;
      case AC_DOINSPECTOR:
        i = fMenuInspector.getSelectedIndex();
        fSpec = new Form("Inspector: " + INSPECTOR_CAT[i]);
        Application.setup(fSpec, Application.cBACK, null);
        if (suite != null) {
          suite.export(fSpec, INSPECTOR_CAT[i]);
        }
        Application.show(null, fSpec, true);
        break;
      case AC_DOBENCHMARK:
        i = fMenuBenchmark.getSelectedIndex();
        fSpec = new Form("Benchmark: " + BENCHMARK_CAT[i]);
        Application.setup(fSpec, Application.cBACK, null);
        if (suite != null) {
          suite.benchmark(fSpec, BENCHMARK_CAT[i]);
        }
        Application.show(null, fSpec, true);
        break;
      case AC_OPENCLASS:
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
        break;
      case AC_PREV:
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
        break;
      default:
        processed = false;
        break;
    }
    return processed;
  }

  /**
   * init.
   */
  protected void init() {
    super.init();
    try {
      url = readAppProperty("POSTURL", "http://www.eiroca.net/services/testsuite/store.php");
      Application.messages = BaseApp.readStrings("messages.txt");
      final Image[] icons = new Image[6];
      icons[0] = BaseApp.createImage("Specs.png");
      icons[1] = BaseApp.createImage("Specs.png");
      icons[2] = BaseApp.createImage("ClassBrowser.png");
      icons[3] = BaseApp.createImage("Keys.png");
      icons[4] = BaseApp.createImage("Dash.png");
      icons[5] = BaseApp.createImage("icon.png");
      suite = new Suite();
      suite.run();
      menuDesc = new String[] {
          "Inspectors", "Benchmark", "Class browser", "Keys", "Post data", "About"
      };
      fMenu = new List("Main Menu", Choice.IMPLICIT, menuDesc, icons);
      Application.cOK = Application.newCommand(TestSuite.MSG_OK, Command.OK, 30, TestSuite.AC_OK);
      Application.cBACK = Application.newCommand(TestSuite.MSG_BACK, Command.BACK, 20, Application.AC_BACK);
      Application.cEXIT = Application.newCommand(TestSuite.MSG_EXIT, Command.EXIT, 10, Application.AC_EXIT);
      cPREV = Application.newCommand(TestSuite.MSG_PREV, Command.SCREEN, 1, TestSuite.AC_PREV);
      Application.registerListItem(fMenu, 0, TestSuite.AC_SHOWINSPECTOR);
      Application.registerListItem(fMenu, 1, TestSuite.AC_SHOWBENCHMARK);
      Application.registerListItem(fMenu, 2, TestSuite.AC_SHOWCLASSBROWSER);
      Application.registerListItem(fMenu, 3, TestSuite.AC_SHOWKEYSTATE);
      Application.registerListItem(fMenu, 4, TestSuite.AC_SHOWPOSTDATA);
      Application.registerListItem(fMenu, 5, TestSuite.AC_DOABOUT);
      Application.setup(fMenu, Application.cEXIT, null);
    }
    catch (final Exception e) {
      fMenu.setTitle(e.getMessage());
    }
    Application.show(null, fMenu, true);
  }

  /**
   * Gets the post data form.
   * 
   * @return the post data form
   */
  private Form getPostDataForm() {
    final Form f = new Form("Post Data");
    f.append("Post data to server " + url);
    Application.setup(f, Application.cBACK, Application.cOK);
    return f;
  }

  /**
   * Load classes.
   */
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

  /* (non-Javadoc)
   * @see net.eiroca.j2me.observable.Observer#changed(net.eiroca.j2me.observable.Observable)
   */
  public void changed(final Observable observable) {
    final DataSender ds = (DataSender) observable;
    System.out.println(" " + ds.getStatus());
    fMenu.set(4, menuDesc[4] + " (" + ds.getStatus() + ")", ClassBrowserHelper.imPlus);
  }

}
