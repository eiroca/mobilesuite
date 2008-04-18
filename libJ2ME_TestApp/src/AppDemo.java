import java.util.Date;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.rms.RMSTable;
import net.eiroca.j2me.rms.Settings;
import net.eiroca.log4j2me.Configurator;
import net.eiroca.log4j2me.FormAppender;
import net.eiroca.log4j2me.Properties;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * @author Ang Kok Chai
 * @version 1.0
 */
public final class AppDemo extends Application implements RecordListener {

  static Category log = Category.getInstance("ME");

  static final int COUNT = 15;

  private TextBox valueA = new TextBox("ValueA", null, 100, TextField.ANY);
  private TextBox valueB = new TextBox("ValueB", null, 100, TextField.EMAILADDR);
  private TextBox valueC = new TextBox("ValueC", null, 100, TextField.HYPERLINK);
  private TextBox valueD = new TextBox("ValueD", null, 100, TextField.DECIMAL);
  private TextBox valueE = new TextBox("ValueE", null, 100, TextField.INITIAL_CAPS_SENTENCE);
  private TextBox valueF = new TextBox("ValueF", null, 100, TextField.PHONENUMBER);
  private Command CSAVE = new Command("Save", Command.OK, 0);
  private Command CEXIT = new Command("Exit", Command.CANCEL, 0);
  private Command CBACK = new Command("Back", Command.BACK, 0);
  private Command CCLEAR = new Command("Clear", Command.SCREEN, 0);
  private List menu;
  private List list00;
  private List list10;
  private List list11;
  private List list12;
  private List list20;
  private List list21;
  private Form logForm;

  private final static int AC_SHOWPROPA = 1;
  private final static int AC_SHOWPROPB = 2;
  private final static int AC_SHOWPROPC = 3;
  private final static int AC_SHOWPROPD = 4;
  private final static int AC_SHOWPROPE = 5;
  private final static int AC_SHOWPROPF = 6;
  private final static int AC_SHOWRMS = 7;
  private final static int AC_LIST00 = 8;
  private final static int AC_LIST10 = 9;
  private final static int AC_LIST20 = 10;
  private final static int AC_LIST11 = 11;
  private final static int AC_LIST12 = 12;
  private final static int AC_LIST21 = 13;
  private final static int AC_FORM01 = 14;
  private final static int AC_FORM02 = 15;
  private final static int AC_LOGFORM = 16;
  private final static int AC_SAVE = 17;
  private final static int AC_CLEAR = 18;

  public AppDemo() {
    super();
    Properties props = new Properties();
    System.out.println("MHello start to config log4j2me.");
    Configurator.load(props, "logJ2ME.conf");
    Configurator.load(props, this);
    Vector vFormAppenders = PropertyConfigurator.configure(props);
    System.out.println("MHello finished configuration of log4j2me.");
    for (int i = 0; vFormAppenders.size() > i; i++) {
      String title = ((FormAppender) vFormAppenders.elementAt(i)).getTitle();
      if ((null != title) && (title.equals("mylog"))) {
        logForm = ((FormAppender) vFormAppenders.elementAt(i)).getForm();
      }
    }
    registerCommand(CBACK, AC_BACK);
    registerCommand(CEXIT, AC_EXIT);
    setRecordListener(this);
    log.debug("init()");
    init();
  }

  public void done() {
    closeRecordStores();
    super.done();
  }

  /**
   * @param recordStore
   * @param recordId
   */
  public void recordAdded(RecordStore recordStore, int recordId) {
    log.warn("User has added properties");
  }

  /**
   * @param recordStore
   * @param recordId
   */
  public void recordChanged(RecordStore recordStore, int recordId) {
    log.warn("User has saved properties");
  }

  /**
   * @param recordStore
   * @param recordId
   */
  public void recordDeleted(RecordStore recordStore, int recordId) {
    log.warn("Record deleted");
  }

  public void changed(final int event, final Displayable previous, final Displayable next) {
    if (event == EV_AFTERCHANGE) {
      if (previous != null && next != null) {
        log.warn("From: " + previous.getTitle() + " To: " + next.getTitle());
      }
    }
  }

  public boolean handleAction(int action, Displayable d, Command cmd) {
    boolean processed = true;
    switch (action) {
      case AC_SHOWPROPA:
        valueA.setString(Settings.get("ValueA"));
        show(null, valueA, true);
        break;
      case AC_SHOWPROPB:
        valueB.setString(Settings.get("ValueB"));
        show(null, valueB, true);
        break;
      case AC_SHOWPROPC:
        valueC.setString(Settings.get("ValueC"));
        show(null, valueC, true);
        break;
      case AC_SHOWPROPD:
        valueD.setString(Settings.get("ValueD"));
        show(null, valueD, true);
        break;
      case AC_SHOWPROPE:
        valueE.setString(Settings.get("ValueE"));
        show(null, valueE, true);
        break;
      case AC_SHOWPROPF:
        valueF.setString(Settings.get("ValueF"));
        show(null, valueF, true);
        break;
      case AC_SHOWRMS:
        show(null, initForm03(), true);
        break;
      case AC_LIST00:
        BaseApp.show(null, list00, true);
        break;
      case AC_LIST10:
        BaseApp.show(null, list10, true);
        break;
      case AC_LIST20:
        BaseApp.show(null, list20, true);
        break;
      case AC_LIST11:
        BaseApp.show(null, list11, true);
        break;
      case AC_LIST12:
        BaseApp.show(null, list12, true);
        break;
      case AC_LIST21:
        BaseApp.show(null, list21, true);
        break;
      case AC_FORM01:
        BaseApp.show(null, initForm01(), true);
        break;
      case AC_FORM02:
        BaseApp.show(null, initForm02(), true);
        break;
      case AC_LOGFORM:
        BaseApp.show(null, logForm, true);
        break;
      case AC_SAVE:
        if (d == valueA) {
          Settings.put("ValueA", valueA.getString());
        }
        else if (d == valueB) {
          Settings.put("ValueB", valueB.getString());
        }
        else if (d == valueC) {
          Settings.put("ValueC", valueC.getString());
        }
        else if (d == valueD) {
          Settings.put("ValueD", valueD.getString());
        }
        else if (d == valueE) {
          Settings.put("ValueE", valueE.getString());
        }
        else if (d == valueF) {
          Settings.put("ValueF", valueF.getString());
        }
        Settings.save();
        back(null);
        break;
      case AC_CLEAR:
        logForm.deleteAll();
        break;
      default:
        processed = false;
        break;
    }
    return processed;
  }

  /**
   * init
   */
  protected void init() {
    super.init();
    setup(logForm, CBACK, CCLEAR);
    Image[] icons = splitImages("/icons.png", 5, 16, 16);
    menu = new List("Main Menu", List.IMPLICIT, new String[] {
        "Settings", "Scale Demo", "IsoDate test", "RMS table (can be slow)", "show Log"
    }, icons);
    list00 = new List("Props 0.0", List.IMPLICIT, new String[] {
        "1.0", "2.0"
    }, null);
    list10 = new List("Props 1.0", List.IMPLICIT, new String[] {
        "1.1", "1.2"
    }, null);
    list11 = new List("Props 1.1", List.IMPLICIT, new String[] {
        "ValueA", "ValueB", "ValueC"
    }, null);
    list12 = new List("Props 1.2", List.IMPLICIT, new String[] {
        "ValueD", "ValueE"
    }, null);
    list20 = new List("Props 2.0", List.IMPLICIT, new String[] {
      "2.1"
    }, null);
    list21 = new List("Props 2.1", List.IMPLICIT, new String[] {
      "ValueF"
    }, null);
    registerCommand(CSAVE, AC_SAVE);
    registerCommand(CCLEAR, AC_CLEAR);
    setup(menu, CEXIT, null);
    setup(list00, CBACK, null);
    setup(list10, CBACK, null);
    setup(list11, CBACK, null);
    setup(list12, CBACK, null);
    setup(list20, CBACK, null);
    setup(list21, CBACK, null);
    setup(valueA, CSAVE, CBACK);
    setup(valueB, CSAVE, CBACK);
    setup(valueC, CSAVE, CBACK);
    setup(valueD, CSAVE, CBACK);
    setup(valueE, CSAVE, CBACK);
    setup(valueF, CSAVE, CBACK);
    registerListItem(menu, 0, AC_LIST00);
    registerListItem(menu, 1, AC_FORM01);
    registerListItem(menu, 2, AC_FORM02);
    registerListItem(menu, 3, AC_SHOWRMS);
    registerListItem(menu, 4, AC_LOGFORM);
    registerListItem(list00, 0, AC_LIST10);
    registerListItem(list00, 1, AC_LIST20);
    registerListItem(list10, 0, AC_LIST11);
    registerListItem(list10, 1, AC_LIST12);
    registerListItem(list20, 0, AC_LIST21);
    registerListItem(list11, 0, AC_SHOWPROPA);
    registerListItem(list11, 1, AC_SHOWPROPB);
    registerListItem(list11, 2, AC_SHOWPROPC);
    registerListItem(list12, 0, AC_SHOWPROPD);
    registerListItem(list12, 1, AC_SHOWPROPE);
    registerListItem(list21, 0, AC_SHOWPROPF);
    if (Settings.size() == 0) {
      Settings.put("ValueA", "");
      Settings.put("ValueB", "");
      Settings.put("ValueC", "");
      Settings.put("ValueD", "");
      Settings.put("ValueE", "");
      Settings.put("ValueF", "");
      Settings.save();
    }
    log.debug("startApp()");
    Settings.load();
    show(null, menu, true);
  }

  protected void read(Form form, int i, RMSTable ri) throws RecordStoreException {
    i = i - 1;
    if (i >= COUNT) {
      i = COUNT - 1;
    }
    String sKey = "Key " + i;
    String data = ri.get(sKey);
    form.append(data + "=" + i + BaseApp.CR);
  }

  private Form form01;

  private Form initForm01() {
    if (form01 == null) {
      form01 = new Form("ScaleDemo");
      Image img = createImage("/icon.png");
      form01.append(Image.createImage(img)); // immutable vers.
      Image big = Application.scaleImage(img, 30, 30);
      form01.append(Image.createImage(big)); // immutable vers.
      Image small = Application.scaleImage(img, 5, 5);
      form01.append(Image.createImage(small)); // immutable vers.
      setup(form01, CBACK, null);
    }
    return form01;
  }

  private Form form02;

  private Form initForm02() {
    if (form02 == null) {
      form02 = new Form("Iso Date");
      Date date = new Date();
      String id = dateToString(date, DATE_TIME);
      form02.append("ISO Date Now:    " + id + BaseApp.CR);
      Date date2 = stringToDate(id, DATE_TIME);
      String id2 = dateToString(date2, DATE_TIME);
      form02.append("After Roundtrip: " + id2 + BaseApp.CR);
      form02.append("Equals:          " + date.equals(date2) + BaseApp.CR);
      setup(form02, CBACK, null);
    }
    return form02;
  }

  private Form form03;

  private Form initForm03() {
    if (form03 == null) {
      form03 = new Form("RMS Index");
      RMSTable ri = null;
      try {
        ri = new RMSTable("idxtest_9");
        form03.append("Sequential write" + BaseApp.CR);
        for (int element = 0; element < COUNT; element++) {
          String sKey = "Key " + element;
          String data = "Element " + element;
          ri.put(sKey, data);
        }
        form03.append("Random read" + BaseApp.CR);
        read(form03, 1, ri);
        read(form03, COUNT / 3, ri);
        read(form03, COUNT / 2, ri);
        read(form03, COUNT - 4, ri);
        read(form03, COUNT - 3, ri);
        read(form03, COUNT - 2, ri);
        read(form03, COUNT, ri);
        ri.close();
      }
      catch (RecordStoreException e) {
        e.printStackTrace();
      }
      setup(form03, CBACK, null);
    }
    return form03;
  }

}