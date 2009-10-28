/** GPL >= 2.0
 * Based upon RSS Reader MIDlet
 * Copyright (C) 2004 Gösta Jonasson
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import net.eiroca.j2me.RSSReader.FeedUpdateThread;
import net.eiroca.j2me.RSSReader.ItemFilter;
import net.eiroca.j2me.RSSReader.LoadFeedsList;
import net.eiroca.j2me.RSSReader.RSSFeed;
import net.eiroca.j2me.RSSReader.RSSItem;
import net.eiroca.j2me.RSSReader.ShowNews;
import net.eiroca.j2me.RSSReader.SplashCanvas;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.Comparator;
import net.eiroca.j2me.app.Pair;
import net.eiroca.j2me.app.RMSInfo;

public class NewsReader extends Application implements Comparator {

  // Application properties (in JAD file)
  private static final String PROP_LOCALE = "microedition.locale";
  private static final String PROP_FEEDLISTURL = "RSS-FEEDLISTURL";
  private static final String PROP_USEHTML = "RSS-USEHTML";
  private static final String PROP_COLBKG = "RSS-COLBKG";
  private static final String PROP_COLBOR = "RSS-COLBOR";
  private static final String PROP_COLTIT = "RSS-COLTIT";
  private static final String PROP_COLTXT = "RSS-COLTXT";

  private static final String PRM_LOCALE = "?l=";
  private static final String STR_GOOGLE = "http://www.google.com/gwt/n?u=";
  private static final String STR_HTTP = "http://";
  private static final String STR_EMPTY = "";

  // Nome Applicazione
  public static final int MSG_APPLICATION = 0;
  // Comandi di default
  public static final int MSG_CM_OK = 1;
  public static final int MSG_CM_BACK = 2;
  public static final int MSG_CM_EXIT = 3;
  public static final int MSG_CM_YES = 4;
  public static final int MSG_CM_NO = 5;
  // Menu Principale
  public static final int MSG_ME_READFEEDS = 6;
  public static final int MSG_ME_ADDFEED = 7;
  public static final int MSG_ME_MEMORY = 8;
  public static final int MSG_ME_CLEANUP = 9;
  public static final int MSG_ME_ABOUT = 10;
  // Menu Add Feed
  public static final int MSG_ME_BROWSEFEEDS = 11;
  public static final int MSG_ME_ADDURL = 12;
  // Form Add Feed URL
  public static final int MSG_FM_ADDFEED = 13;
  public static final int MSG_FM_FEEDTITLE = 14;
  public static final int MSG_FM_FEEDURL = 15;
  public static final int MSG_FM_HTTP = 16;
  public static final int MSG_CM_ADDFEEDURL = 17;
  // Form Browse Feed List
  public static final int MSG_SOMEFEEDS = 18;
  public static final int MSG_CM_ADDFEEDLIST = 19;
  public static final int MSG_FEEDFILEERROR = 20;
  // Add Feed error messages
  public static final int MSG_ERR01_TIT = 21;
  public static final int MSG_ERR01_MSG = 22;
  public static final int MSG_ERR02_TIT = 23;
  public static final int MSG_ERR02_MSG = 24;
  public static final int MSG_ERR03_TIT = 25;
  public static final int MSG_ERR03_MSG = 26;
  // Menu Clean up
  public static final int MSG_ME_CLEANUP_ALL = 27;
  public static final int MSG_ME_CLEANUP_ITEMS = 28;
  public static final int MSG_ME_CLEANUP_1DOLD = 29;
  public static final int MSG_ME_CLEANUP_2DOLD = 30;
  public static final int MSG_ME_CLEANUP_1WOLD = 31;
  // Delete confirmations
  public static final int MSG_CONFIRM = 32;
  public static final int MSG_SUREDELETEALL = 33;
  public static final int MSG_SUREDELETEALLITEMS = 34;
  public static final int MSG_SUREDELETEITEMS = 35;
  public static final int MSG_DELETED = 36;
  // Feeds List
  public static final int MSG_FEEDS = 37;
  public static final int MSG_NEWREAD = 38;
  public static final int MSG_NEWSUPDATE = 39;
  public static final int MSG_NEWSINFO = 40;
  public static final int MSG_NEWSDELETE = 41;
  public static final int MSG_NEWSERASE = 42;
  public static final int MSG_ERRNOFEEDS = 43;
  public static final int MSG_CM_OPEN = 44;
  public static final int MSG_CM_STOP = 45;
  // News List
  public static final int MSG_CM_READ = 46;
  public static final int MSG_CM_GO = 47;
  // Delete messages
  public static final int MSG_DELETEALL = 48;
  public static final int MSG_DELETEITEMS = 49;
  // Database info messages
  public static final int MSG_DBINFO1 = 50;
  public static final int MSG_DBINFO2 = 51;
  public static final int MSG_DBINFO3 = 52;
  public static final int MSG_DBINFO4 = 53;
  public static final int MSG_DBINFO5 = 54;
  // Memory info messages
  public static final int MSG_MEMINFO1 = 55;
  public static final int MSG_MEMINFO2 = 56;
  public static final int MSG_MEMINFO3 = 57;
  // Load Feed List
  public static final int MSG_LOADFEEDLISTTITLE = 58;
  public static final int MSG_LOADFEEDLISTST00 = 59;
  public static final int MSG_LOADFEEDLISTST01 = 60;
  public static final int MSG_LOADFEEDLISTST02 = 61;
  public static final int MSG_LOADFEEDLISTST03 = 62;
  public static final int MSG_LOADFEEDLISTEND = 63;
  // Update Feed Items
  public static final int MSG_UPDATETITLE = 64;
  public static final int MSG_UPDATEST00 = 65;
  public static final int MSG_UPDATEST01 = 66;
  public static final int MSG_UPDATEST02 = 67;
  public static final int MSG_UPDATEST03 = 68;
  public static final int MSG_UPDATEST04 = 69;
  public static final int MSG_UPDATEST05 = 70;
  public static final int MSG_UPDATEST06 = 71;
  public static final int MSG_UPDATEST07 = 72;
  public static final int MSG_UPDATEOK01 = 73;
  public static final int MSG_UPDATEOK02 = 74;
  public static final int MSG_UPDATEOK03 = 75;
  public static final int MSG_UPDATEERR01 = 76;
  public static final int MSG_UPDATEERR02 = 77;
  public static final int MSG_UPDATEERR03 = 78;
  public static final int MSG_UPDATEERR04 = 79;
  // Feed Info
  public static final int MSG_FEEDINFO01 = 80;
  public static final int MSG_FEEDINFO02 = 81;
  public static final int MSG_FEEDINFO03 = 82;
  public static final int MSG_FEEDINFO04 = 83;
  public static final int MSG_FEEDINFO05 = 84;
  public static final int MSG_FEEDINFO06 = 85;
  public static final int MSG_FEEDINFO07 = 86;
  // Feed Item Info
  public static final int MSG_NOITEMS = 87;
  public static final int MSG_FEEDITEMINFO01 = 88;
  public static final int MSG_FEEDITEMINFO02 = 89;
  // Page scrolling
  public static final int MSG_PREV = 90;
  public static final int MSG_NEXT = 91;

  public static final int ME_MAINMENU = 0;
  public static final int ME_ADDFEED = 1;
  public static final int ME_CLEANUP = 2;
  public static final int ME_FEEDS = 3;

  public static final int AC_YES = 1000;
  public static final int AC_NO = 1001;
  public static final int AC_DOREADFEEDS = 2;
  public static final int AC_DOADDFEED = 3;
  public static final int AC_DOMEMORY = 4;
  public static final int AC_DOCLEANUP = 5;
  public static final int AC_DOABOUT = 6;
  public static final int AC_DOBROWSEFEED1 = 8;
  public static final int AC_DOBROWSEFEED2 = 9;
  public static final int AC_DOADDURL = 10;
  public static final int AC_ADDFEEDURL = 11;
  public static final int AC_ADDFEEDLIST = 12;
  public static final int AC_DOCLEANALL = 14;
  public static final int AC_DOCLEANITEMS = 15;
  public static final int AC_DOCLEAN1DOLD = 16;
  public static final int AC_DOCLEAN2DOLD = 17;
  public static final int AC_DOCLEAN1WOLD = 18;
  public static final int AC_OPEN = 19;
  public static final int AC_NEWSREAD = 21;
  public static final int AC_NEWSUPDATE = 22;
  public static final int AC_NEWSINFO = 23;
  public static final int AC_NEWSDELETE = 24;
  public static final int AC_NEWSERASE = 25;
  public static final int AC_READ = 26;
  public static final int AC_GO = 27;

  //
  private static final int CHECK_SPLASH_DONETIME = 1000;

  // External resources
  private static final String RES_MESSAGES = "messages.txt";
  private static final String RES_ABOUT = "about.txt";
  private static final String RES_FEEDSLIST = "feeds.xml";
  private static final String RES_IMAGE_STAR = "star.png";
  private static final String RES_IMAGE_READ = "read.png";
  private static final String RES_IMAGE_UNREAD = "unread.png";

  private static final int ITEMXPAGE = 20;

  // Table with feed names -> RMS feed id
  private Vector feedsTable;
  private final Vector itemsTable = new Vector();
  /* Mapping feed title -> feed url in the browse feeds list */
  public Vector browseFeedList = new Vector();
  private final Calendar parsecal;
  // Current Feed & Item
  private RSSFeed currentFeed;
  private RSSItem currentItem;

  public static String stgFeedListURL = null;
  public static boolean stgUseHTML = true;
  public static int stgColBckG = 0xFFFFCC;
  public static int stgColBord = 0xFFFFFF;
  public static int stgColTitl = 0xFF0000;
  public static int stgColText = 0x000000;

  private List sMainMenu;
  private List sAddFeedMenu;
  private List sCleanUpMenu;
  private List sAddFeedBrowse;
  private Form sAddFeedURL;
  private TextField iTitle;
  private TextField iURL;
  private List sFeeds;
  private List sFeedMenu;
  private Form sFeedInfo;
  private List sNewsList;
  private Displayable sNews;

  public static Command cADDFEEDURL;
  public static Command cADDFEEDLIST;
  public static Command cYES;
  public static Command cNO;
  public static Command cOPEN;
  public static Command cSTOP;
  public static Command cREAD;
  public static Command cGO;
  public static Command cOK2;

  private final Image iBullet;
  private final Image iRead;
  private final Image iUnread;

  int page;
  int nextIdx;
  boolean hasPrev;
  boolean hasNext;

  Font bold;
  Font normal;
  Object[] o;

  public NewsReader() {
    super();
    o = new Object[9];
    bold = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    normal = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    Application.messages = BaseApp.readStrings(NewsReader.RES_MESSAGES);
    Application.cOK = Application.newCommand(NewsReader.MSG_CM_OK, Command.OK, 30, Application.AC_NONE);
    Application.cBACK = Application.newCommand(NewsReader.MSG_CM_BACK, Command.BACK, 20, Application.AC_BACK);
    Application.cEXIT = Application.newCommand(NewsReader.MSG_CM_EXIT, Command.EXIT, 10, Application.AC_EXIT);
    NewsReader.cADDFEEDURL = Application.newCommand(NewsReader.MSG_CM_ADDFEEDURL, Command.OK, 31, NewsReader.AC_ADDFEEDURL);
    NewsReader.cADDFEEDLIST = Application.newCommand(NewsReader.MSG_CM_ADDFEEDLIST, Command.OK, 31, NewsReader.AC_ADDFEEDLIST);
    NewsReader.cOK2 = Application.newCommand(NewsReader.MSG_CM_OK, Command.OK, 1, NewsReader.AC_DOBROWSEFEED2);
    NewsReader.cYES = Application.newCommand(NewsReader.MSG_CM_YES, Command.OK, 1, NewsReader.AC_YES);
    NewsReader.cNO = Application.newCommand(NewsReader.MSG_CM_NO, Command.BACK, 2, NewsReader.AC_NO);
    NewsReader.cOPEN = Application.newCommand(NewsReader.MSG_CM_OPEN, Command.OK, 2, NewsReader.AC_OPEN);
    NewsReader.cREAD = Application.newCommand(NewsReader.MSG_CM_READ, Command.OK, 2, NewsReader.AC_READ);
    NewsReader.cGO = Application.newCommand(NewsReader.MSG_CM_GO, Command.OK, 2, NewsReader.AC_GO);
    NewsReader.cSTOP = new Command(Application.messages[NewsReader.MSG_CM_STOP], Command.OK, 2);
    iBullet = BaseApp.createImage(NewsReader.RES_IMAGE_STAR);
    iRead = BaseApp.createImage(NewsReader.RES_IMAGE_READ);
    iUnread = BaseApp.createImage(NewsReader.RES_IMAGE_UNREAD);
    Application.menu = new short[][] {
        {
            NewsReader.ME_MAINMENU, NewsReader.MSG_ME_READFEEDS, NewsReader.AC_DOREADFEEDS, -1
        }, {
            NewsReader.ME_MAINMENU, NewsReader.MSG_ME_ADDFEED, NewsReader.AC_DOADDFEED, -1
        }, {
            NewsReader.ME_MAINMENU, NewsReader.MSG_ME_MEMORY, NewsReader.AC_DOMEMORY, -1
        }, {
            NewsReader.ME_MAINMENU, NewsReader.MSG_ME_CLEANUP, NewsReader.AC_DOCLEANUP, -1
        }, {
            NewsReader.ME_MAINMENU, NewsReader.MSG_ME_ABOUT, NewsReader.AC_DOABOUT, -1
        }, {
            NewsReader.ME_ADDFEED, NewsReader.MSG_ME_BROWSEFEEDS, NewsReader.AC_DOBROWSEFEED1, -1
        }, {
            NewsReader.ME_ADDFEED, NewsReader.MSG_ME_ADDURL, NewsReader.AC_DOADDURL, -1
        }, {
            NewsReader.ME_CLEANUP, NewsReader.MSG_ME_CLEANUP_ALL, NewsReader.AC_DOCLEANALL, -1
        }, {
            NewsReader.ME_CLEANUP, NewsReader.MSG_ME_CLEANUP_ITEMS, NewsReader.AC_DOCLEANITEMS, -1
        }, {
            NewsReader.ME_CLEANUP, NewsReader.MSG_ME_CLEANUP_1DOLD, NewsReader.AC_DOCLEAN1DOLD, -1
        }, {
            NewsReader.ME_CLEANUP, NewsReader.MSG_ME_CLEANUP_2DOLD, NewsReader.AC_DOCLEAN2DOLD, -1
        }, {
            NewsReader.ME_CLEANUP, NewsReader.MSG_ME_CLEANUP_1WOLD, NewsReader.AC_DOCLEAN1WOLD, -1
        }, {
            NewsReader.ME_FEEDS, NewsReader.MSG_NEWREAD, NewsReader.AC_NEWSREAD, -1
        }, {
            NewsReader.ME_FEEDS, NewsReader.MSG_NEWSUPDATE, NewsReader.AC_NEWSUPDATE, -1
        }, {
            NewsReader.ME_FEEDS, NewsReader.MSG_NEWSINFO, NewsReader.AC_NEWSINFO, -1
        }, {
            NewsReader.ME_FEEDS, NewsReader.MSG_NEWSDELETE, NewsReader.AC_NEWSDELETE, -1
        }, {
            NewsReader.ME_FEEDS, NewsReader.MSG_NEWSERASE, NewsReader.AC_NEWSERASE, -1
        }
    };
    feedsTable = RSSFeed.getIndex(this);
    String tmp;
    tmp = getAppProperty(NewsReader.PROP_FEEDLISTURL);
    if (tmp == null) {
      tmp = NewsReader.RES_FEEDSLIST;
    }
    if (tmp.startsWith(NewsReader.STR_HTTP)) {
      final String locale = System.getProperty(NewsReader.PROP_LOCALE);
      if (locale != null) {
        tmp += NewsReader.PRM_LOCALE + locale;
      }
    }
    NewsReader.stgFeedListURL = tmp;
    tmp = getAppProperty(NewsReader.PROP_USEHTML);
    if ((tmp == null) || (tmp.startsWith("1"))) {
      NewsReader.stgUseHTML = true;
    }
    else {
      NewsReader.stgUseHTML = false;
    }
    NewsReader.stgColBckG = getInt(NewsReader.PROP_COLBKG, NewsReader.stgColBckG, 16);
    NewsReader.stgColBord = getInt(NewsReader.PROP_COLBOR, NewsReader.stgColBord, 16);
    NewsReader.stgColTitl = getInt(NewsReader.PROP_COLTIT, NewsReader.stgColTitl, 16);
    NewsReader.stgColText = getInt(NewsReader.PROP_COLTXT, NewsReader.stgColText, 16);
    parsecal = Calendar.getInstance();
  }

  public void done() {
    super.done();
    RSSItem.rs_close(true);
    RSSFeed.rs_close(true);
  }

  public int getInt(final String prop, final int def, final int radix) {
    final String tmp = getAppProperty(prop);
    int val = def;
    if (tmp != null) {
      try {
        val = Integer.parseInt(tmp, radix);
      }
      catch (final Exception e) {
      }
    }
    return val;
  }

  /**
   * Start the MIDlet.
   */
  public void init() {
    super.init();
    final SplashCanvas splashc = new SplashCanvas();
    BaseApp.setDisplay(splashc);
    new Thread(splashc).start();
    /* Wait and check for the splash screen to finish */
    while (!splashc.isDone()) {
      try {
        Thread.sleep(NewsReader.CHECK_SPLASH_DONETIME);
      }
      catch (final Exception e) {
        //
      }
    }
    Application.show(null, getMainMenu(), true);
  }

  // Implementation of the command listener interface
  private int nextAction = NewsReader.AC_NO;

  public boolean handleAction(int action, final Displayable d, final Command c) {
    Alert msg;
    boolean confirmed = false;
    if (action == NewsReader.AC_YES) {
      confirmed = true;
      action = nextAction;
    }
    if (action == NewsReader.AC_NO) {
      Application.back(null);
      return true;
    }
    String deltext = null;
    nextAction = action;
    int idx;
    int ni;
    int nf;
    switch (action) {
      case AC_EXIT:
        return false;
      case AC_BACK:
        final Displayable dd = BaseApp.getDisplay();
        if (dd == sNews) {
          sNewsList = getNewsList(true);
          Application.show(null, sNewsList, false);
        }
        else if (dd == sNewsList) {
          Application.back(null, sFeedMenu, false);
        }
        else {
          return false;
        }
        break;
      case AC_DOREADFEEDS:
        sFeeds = getSelectFeed();
        Application.show(null, sFeeds, true);
        break;
      case AC_DOADDFEED:
        Application.show(null, getAddFeedMenu(), true);
        break;
      case AC_DOMEMORY:
        Application.show(null, getMemoryForm(), true);
        break;
      case AC_DOCLEANUP:
        Application.show(null, getCleanUpMenu(), true);
        break;
      case AC_DOABOUT:
        Application.show(null, Application.getTextForm(NewsReader.MSG_ME_ABOUT, NewsReader.RES_ABOUT), true);
        break;
      case AC_DOBROWSEFEED1:
        getAddFeedBrowseList();
        boolean ready = true;
        if (browseFeedList.size() == 0) {
          if (NewsReader.stgFeedListURL.startsWith(NewsReader.STR_HTTP)) {
            ready = false;
          }
          new LoadFeedsList(this, NewsReader.stgFeedListURL, !ready, sAddFeedBrowse);
        }
        if (ready) {
          Application.show(null, sAddFeedBrowse, true);
        }
        break;
      case AC_DOBROWSEFEED2:
        Application.show(null, sAddFeedBrowse, true);
        break;
      case AC_DOADDURL:
        Application.show(null, getAddFeedURL(), true);
        break;
      case AC_ADDFEEDURL:
        msg = addFeedURL();
        Application.back(msg, sAddFeedMenu, false);
        break;
      case AC_ADDFEEDLIST:
        msg = addFeedList();
        Application.back(msg, sAddFeedMenu, false);
        break;
      case AC_DOCLEANALL:
        if (confirmed) {
          ni = RSSItem.deleteAll();
          nf = RSSFeed.deleteAll();
          o[0] = new Integer(nf);
          o[1] = new Integer(ni);
          deltext = Application.format(NewsReader.MSG_DELETEALL, o);
        }
        else {
          confirm(NewsReader.MSG_CONFIRM, NewsReader.MSG_SUREDELETEALL, NewsReader.cYES, NewsReader.cNO);
        }
        break;
      case AC_DOCLEANITEMS:
        if (confirmed) {
          ni = RSSItem.deleteAll();
          o[0] = new Integer(ni);
          deltext = Application.format(NewsReader.MSG_DELETEITEMS, o);
        }
        else {
          confirm(NewsReader.MSG_CONFIRM, NewsReader.MSG_SUREDELETEALLITEMS, NewsReader.cYES, NewsReader.cNO);
        }
        break;
      case AC_DOCLEAN1DOLD:
        if (confirmed) {
          ni = RSSItem.deleteItems(new ItemFilter(System.currentTimeMillis() - NewsReader.DAYMILLIS));
          o[0] = new Integer(ni);
          deltext = Application.format(NewsReader.MSG_DELETEITEMS, o);
        }
        else {
          confirm(NewsReader.MSG_CONFIRM, NewsReader.MSG_SUREDELETEITEMS, NewsReader.cYES, NewsReader.cNO);
        }
        break;
      case AC_DOCLEAN2DOLD:
        if (confirmed) {
          ni = RSSItem.deleteItems(new ItemFilter(System.currentTimeMillis() - (2 * NewsReader.DAYMILLIS)));
          o[0] = new Integer(ni);
          deltext = Application.format(NewsReader.MSG_DELETEITEMS, o);
        }
        else {
          confirm(NewsReader.MSG_CONFIRM, NewsReader.MSG_SUREDELETEITEMS, NewsReader.cYES, NewsReader.cNO);
        }
        break;
      case AC_DOCLEAN1WOLD:
        if (confirmed) {
          ni = RSSItem.deleteItems(new ItemFilter(System.currentTimeMillis() - (7 * NewsReader.DAYMILLIS)));
          o[0] = new Integer(ni);
          deltext = Application.format(NewsReader.MSG_DELETEITEMS, o);
        }
        else {
          confirm(NewsReader.MSG_CONFIRM, NewsReader.MSG_SUREDELETEITEMS, NewsReader.cYES, NewsReader.cNO);
        }
        break;
      case AC_OPEN:
        idx = sFeeds.getSelectedIndex();
        final Pair p = (Pair) feedsTable.elementAt(idx);
        final int feedid = ((Integer) p.value).intValue();
        // Load the feed from the RMS database
        if ((currentFeed == null) || (currentFeed.feedID != feedid)) {
          currentFeed = RSSFeed.load(feedid);
        }
        page = 0;
        Application.show(null, getFeedMenu(), true);
        break;
      case AC_NEWSREAD:
        sNewsList = getNewsList(false);
        Application.show(null, sNewsList, false);
        break;
      case AC_NEWSUPDATE:
        new FeedUpdateThread(currentFeed, this);
        break;
      case AC_NEWSINFO:
        Application.show(null, getFeedInfo(), true);
        break;
      case AC_NEWSDELETE:
        // Delete all the items for this feed
        ni = RSSItem.deleteAll(currentFeed.feedID);
        o[0] = new Integer(ni);
        final String deletetext = Application.format(NewsReader.MSG_DELETEITEMS, o);
        final Alert newalert = new Alert(Application.messages[NewsReader.MSG_NEWSDELETE], deletetext, null, AlertType.INFO);
        newalert.setTimeout(Alert.FOREVER);
        /* Reload the feed */
        currentFeed = RSSFeed.load(currentFeed.feedID);
        Application.back(newalert, getFeedMenu(), false);
        break;
      case AC_NEWSERASE:
        // Delete this feed
        RSSFeed.deleteFeed(currentFeed.feedID);
        // Need to update after delete
        Application.back(null, sMainMenu, false);
        break;
      case AC_READ:
        idx = sNewsList.getSelectedIndex();
        int pos;
        if (idx == nextIdx) {
          page++;
          sNewsList = getNewsList(false);
          Application.show(null, sNewsList, false);
          break;
        }
        pos = idx;
        if (hasPrev) {
          if (idx == 0) {
            page--;
            sNewsList = getNewsList(false);
            Application.show(null, sNewsList, false);
            break;
          }
          else {
            pos--;
          }
        }
        currentItem = (RSSItem) itemsTable.elementAt(pos);
        if (currentItem != null) {
          currentFeed.read(currentItem);
          if (NewsReader.stgUseHTML) {
            sNews = getNewsDataHTML();
          }
          else {
            sNews = getNewsDataTEXT();
          }
          Application.show(null, sNews, true);
          sNewsList.set(idx, currentItem.title, iRead);
          sNewsList.setFont(idx, normal);
        }
        else {
          Application.back(null, sMainMenu, false);
        }
        break;
      case AC_GO:
        try {
          String link = currentItem.link;
          if (currentFeed.type.startsWith(RSSFeed.WEB_ITEM)) {
            link = NewsReader.STR_GOOGLE + BaseApp.URLEncode(link);
          }
          platformRequest(link);
        }
        catch (final ConnectionNotFoundException e) {
          //
        }
        break;
    }
    if (deltext != null) {
      final Alert newalert = new Alert(Application.messages[NewsReader.MSG_DELETED]);
      newalert.setTimeout(Alert.FOREVER);
      newalert.setType(AlertType.INFO);
      newalert.setString(deltext);
      Application.back(newalert, sMainMenu, false);
    }
    return true;
  }

  public void changed(final int event, final Displayable previous, final Displayable next) {
    if (event == Application.EV_BEFORECHANGE) {
      if (next == sFeedMenu) {
        // Delete all options in the list
        sFeedMenu.delete(0);
        sFeedMenu.setTitle(currentFeed.title);
        final int nr = currentFeed.nr;
        final int nn = currentFeed.nn;
        o[0] = new Integer(nr);
        o[1] = new Integer(nn);
        sFeedMenu.insert(0, Application.format(NewsReader.MSG_NEWREAD, o), null);
        if (nr > 0) {
          sFeedMenu.setSelectedIndex(0, true);
        }
        else {
          sFeedMenu.setSelectedIndex(1, true);
        }
      }
      else if (next == sFeedMenu) {
        sFeedMenu.setTitle(currentFeed.title);
      }
      else if (next == sAddFeedBrowse) {
        sAddFeedBrowse.deleteAll();
        if (browseFeedList.size() == 0) {
          sAddFeedBrowse.append(Application.messages[NewsReader.MSG_FEEDFILEERROR], null);
          sAddFeedBrowse.removeCommand(NewsReader.cADDFEEDLIST);
        }
        else {
          RSSFeed feed;
          for (int i = 0; i < browseFeedList.size(); i++) {
            feed = (RSSFeed) browseFeedList.elementAt(i);
            sAddFeedBrowse.append(feed.title, null);
          }
          sAddFeedBrowse.addCommand(NewsReader.cADDFEEDLIST);
        }
      }
    }
  }

  public final static long SECMILLIS = 1000;
  public final static long MINMILLIS = NewsReader.SECMILLIS * 60;
  public final static long HOURMILLIS = NewsReader.MINMILLIS * 60;
  public final static long DAYMILLIS = NewsReader.HOURMILLIS * 24;

  /**
   * Returns the time between the given times. For example: 3d 12h 5m 34s ago
   * @param start Start time in milliseconds.
   * @param stop Stop time in milliseconds.
   * @return the time between start and stop as a string
   */
  public static String timeDiff(long start, final long stop) {
    start = stop - start;
    String diff = NewsReader.STR_EMPTY;
    if (start >= NewsReader.DAYMILLIS) {
      diff += (start / NewsReader.DAYMILLIS) + "d ";
    }
    if (start >= NewsReader.HOURMILLIS) {
      diff += (start % NewsReader.DAYMILLIS) / NewsReader.HOURMILLIS + "h ";
    }
    if (start >= NewsReader.MINMILLIS) {
      diff += (start % NewsReader.HOURMILLIS) / NewsReader.MINMILLIS + "m ";
    }
    if (start >= NewsReader.SECMILLIS) {
      diff += (start % NewsReader.MINMILLIS) / NewsReader.SECMILLIS + "s ";
    }
    diff += "ago";
    return diff;
  }

  private Alert addFeedList() {
    int addedfeeds = 0;
    RSSFeed feed;
    // Reload the feed titles
    feedsTable = RSSFeed.getIndex(this);
    final int feeds = sAddFeedBrowse.size();
    // Find the selected feeds and add them if the title doesn't exist.
    final Pair p = new Pair();
    for (int i = 0; i < feeds; i++) {
      if (sAddFeedBrowse.isSelected(i)) {
        sAddFeedBrowse.setSelectedIndex(i, false);
        p.name = sAddFeedBrowse.getString(i);
        if ((feedsTable == null) || (BaseApp.find(feedsTable, p, this) == -1)) {
          feed = (RSSFeed) browseFeedList.elementAt(i);
          RSSFeed.save(feed, false);
          addedfeeds++;
        }
      }
    }
    return buildAlert(addedfeeds);
  }

  private Alert addFeedURL() {
    RSSFeed newfeed;
    int addedfeeds = 0;
    // Reload the feed titles
    feedsTable = RSSFeed.getIndex(this);
    final String newtitle = iTitle.getString().trim();
    final String newurl = iURL.getString().trim();
    final Pair p = new Pair();
    if ((newtitle.length() > 0) && (newurl.length() > 7)) {
      p.name = newtitle;
      if ((feedsTable == null) || (BaseApp.find(feedsTable, p, this) == -1)) {
        newfeed = new RSSFeed();
        newfeed.title = newtitle;
        newfeed.URL = newurl;
        // Save the feed to the RMS
        RSSFeed.save(newfeed, false);
        addedfeeds++;
        // Clear the Form
        iTitle.setString(NewsReader.STR_EMPTY);
        iURL.setString(Application.messages[NewsReader.MSG_FM_HTTP]);
      }
    }
    return buildAlert(addedfeeds);
  }

  private Alert buildAlert(final int addedFeeds) {
    Alert addalert;
    if (addedFeeds > 0) {
      o[0] = new Integer(addedFeeds);
      addalert = new Alert(Application.messages[NewsReader.MSG_ERR01_TIT], Application.format(NewsReader.MSG_ERR01_MSG, o), null, AlertType.INFO);
    }
    else if (addedFeeds == -1) {
      addalert = new Alert(Application.messages[NewsReader.MSG_ERR02_TIT], Application.messages[NewsReader.MSG_ERR02_MSG], null, AlertType.ERROR);
    }
    else {
      addalert = new Alert(Application.messages[NewsReader.MSG_ERR03_TIT], Application.messages[NewsReader.MSG_ERR03_MSG], null, AlertType.ERROR);
    }
    addalert.setTimeout(Alert.FOREVER);
    return addalert;
  }

  private List getMainMenu() {
    if (sMainMenu == null) {
      sMainMenu = Application.getMenu(Application.messages[NewsReader.MSG_APPLICATION], NewsReader.ME_MAINMENU, -1, Application.cEXIT);
    }
    return sMainMenu;
  }

  private List getAddFeedMenu() {
    if (sAddFeedMenu == null) {
      sAddFeedMenu = Application.getMenu(Application.messages[NewsReader.MSG_ME_ADDFEED], NewsReader.ME_ADDFEED, -1, Application.cBACK);
    }
    return sAddFeedMenu;
  }

  private Form getAddFeedURL() {
    if (sAddFeedURL == null) {
      iTitle = new TextField(Application.messages[NewsReader.MSG_FM_FEEDTITLE], NewsReader.STR_EMPTY, 64, TextField.ANY);
      iURL = new TextField(Application.messages[NewsReader.MSG_FM_FEEDURL], Application.messages[NewsReader.MSG_FM_HTTP], 256, TextField.URL);
      sAddFeedURL = new Form(Application.messages[NewsReader.MSG_FM_ADDFEED]);
      Application.setup(sAddFeedURL, Application.cBACK, NewsReader.cADDFEEDURL);
      sAddFeedURL.append(iTitle);
      sAddFeedURL.append(iURL);
    }
    return sAddFeedURL;
  }

  /**
   * Returns a list with feeds read from a file. It also fills the feedtable (Hashtable) with the feed titles->feed urls.
   * @return the list of feed titles
   */
  private List getAddFeedBrowseList() {
    sAddFeedBrowse = new List(Application.messages[NewsReader.MSG_SOMEFEEDS], Choice.MULTIPLE);
    Application.setup(sAddFeedBrowse, Application.cBACK, null);
    return sAddFeedBrowse;
  }

  private List getCleanUpMenu() {
    if (sCleanUpMenu == null) {
      sCleanUpMenu = Application.getMenu(Application.messages[NewsReader.MSG_ME_CLEANUP], NewsReader.ME_CLEANUP, -1, Application.cBACK);
    }
    return sCleanUpMenu;
  }

  public Form getMemoryForm() {
    System.gc();
    final Form sMemory = new Form(Application.messages[NewsReader.MSG_ME_MEMORY]);
    final long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
    final long free = Runtime.getRuntime().freeMemory() / 1024;
    final RMSInfo info = new RMSInfo();
    final RMSInfo infoFeed = RSSFeed.getInfo();
    if (infoFeed != null) {
      RMSInfo infoItems;
      info.used = infoFeed.used;
      info.avail = info.avail;
      info.numRec = 0;
      final int[] ids = RSSFeed.getIDs();
      if (ids != null) {
        for (int i = 0; i < ids.length; i++) {
          infoItems = RSSItem.getInfo(ids[i]);
          info.used = info.used + infoItems.used;
          info.avail = Math.min(info.avail, infoItems.avail);
          info.numRec = info.numRec + infoItems.numRec;
        }
      }
    }
    StringBuffer sb;
    if (infoFeed != null) {
      sb = new StringBuffer(300);
      sb.append(Application.messages[NewsReader.MSG_DBINFO1]);
      sb.append(BaseApp.CR);
      o[0] = new Integer(infoFeed.numRec);
      sb.append(Application.format(NewsReader.MSG_DBINFO2, o));
      sb.append(BaseApp.CR);
      o[0] = new Integer(info.numRec);
      sb.append(Application.format(NewsReader.MSG_DBINFO3, o));
      sb.append(BaseApp.CR);
      o[0] = new Integer(info.used / 1024);
      sb.append(Application.format(NewsReader.MSG_DBINFO4, o));
      sb.append(BaseApp.CR);
      o[0] = new Integer(info.avail / 1024);
      sb.append(Application.format(NewsReader.MSG_DBINFO5, o));
      sb.append(BaseApp.CR);
      sMemory.append(sb.toString());
    }
    sb = new StringBuffer(300);
    sb.append(Application.messages[NewsReader.MSG_MEMINFO1]);
    sb.append(BaseApp.CR);
    o[0] = new Integer((int) used);
    sb.append(Application.format(NewsReader.MSG_MEMINFO2, o));
    sb.append(BaseApp.CR);
    o[0] = new Integer((int) free);
    sb.append(Application.format(NewsReader.MSG_MEMINFO3, o));
    sb.append(BaseApp.CR);
    sMemory.append(sb.toString());
    Application.setup(sMemory, Application.cBACK, null);
    return sMemory;
  }

  /**
   * Refreshes the feed list and the feed title Hashtable.
   */
  private List getSelectFeed() {
    /* Perhaps check if the database have changed or not! */
    /* Reset the List */
    final List sSelectFeed = new List(Application.messages[NewsReader.MSG_FEEDS], Choice.IMPLICIT);
    /* Update the hashtable with all the feeds */
    feedsTable = RSSFeed.getIndex(this);
    if ((feedsTable == null) || feedsTable.isEmpty()) {
      sSelectFeed.setSelectCommand(null);
      sSelectFeed.append(Application.messages[NewsReader.MSG_ERRNOFEEDS], null);
    }
    else {
      sSelectFeed.setSelectCommand(NewsReader.cOPEN);
      for (int i = 0; i < feedsTable.size(); i++) {
        final Pair p = (Pair) feedsTable.elementAt(i);
        sSelectFeed.append(p.name, iBullet);
      }
    }
    Application.setup(sSelectFeed, Application.cBACK, null);
    return sSelectFeed;
  }

  /**
   * Returns a list with feed options. The list is always refreshed when this method is called, so that the number of new items are correct.
   * @return the refreshed list of feed options
   */
  protected List getFeedMenu() {
    if (sFeedMenu == null) {
      sFeedMenu = Application.getMenu(NewsReader.STR_EMPTY, NewsReader.ME_FEEDS, -1, Application.cBACK);
    }
    return sFeedMenu;
  }

  /**
   * Returns a form with the current feed info. The form is always refreshed when this method is called.
   * @return the refreshed form with info about the current feed
   */
  private Form getFeedInfo() {
    if (sFeedInfo == null) {
      sFeedInfo = new Form(currentFeed.title);
      Application.setup(sFeedInfo, Application.cBACK, null);
    }
    else {
      sFeedInfo.setTitle(currentFeed.title);
    }
    sFeedInfo.deleteAll();
    final StringBuffer feedinfo = new StringBuffer();
    o[0] = currentFeed.description;
    feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO01, o));
    feedinfo.append(BaseApp.CR);
    if ((currentFeed.lastUpdateTime > 0)) {
      o[0] = NewsReader.timeDiff(currentFeed.lastUpdateTime, System.currentTimeMillis());
      feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO02, o));
      feedinfo.append(BaseApp.CR);
    }
    o[0] = new Integer(currentFeed.minsBetweenUpdates);
    feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO03, o));
    feedinfo.append(BaseApp.CR);
    o[0] = new Integer(currentFeed.getItemsCount());
    feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO04, o));
    feedinfo.append(BaseApp.CR);
    if (!currentFeed.lastBuildDate.equals(NewsReader.STR_EMPTY)) {
      o[0] = currentFeed.lastBuildDate;
      feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO05, o));
      feedinfo.append(BaseApp.CR);
    }
    if ((currentFeed.lastFeedLen > 0)) {
      o[0] = new Integer((int) (currentFeed.lastFeedLen / 1024));
      feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO06, o).toString());
      feedinfo.append(BaseApp.CR);
    }
    o[0] = currentFeed.URL;
    feedinfo.append(Application.format(NewsReader.MSG_FEEDINFO07, o));
    sFeedInfo.append(feedinfo.toString());
    return sFeedInfo;
  }

  private List getNewsList(final boolean keepPos) {
    int oldPos = -1;
    if (sNewsList != null) {
      oldPos = sNewsList.getSelectedIndex();
    }
    sNewsList = new List(currentFeed.title, Choice.IMPLICIT);
    Application.setup(sNewsList, Application.cBACK, null);
    // This might take a little while when feeds have many items Reset the List
    sNewsList.removeCommand(NewsReader.cREAD);
    // Add content to the list
    if ((currentFeed == null) || (currentFeed.getItemsCount() == 0)) {
      sNewsList.append(Application.messages[NewsReader.MSG_NOITEMS], null);
    }
    else {
      final int nrofitems = currentFeed.getItemsCount();
      final int start = page * NewsReader.ITEMXPAGE;
      int n;
      if ((start + NewsReader.ITEMXPAGE) >= nrofitems) {
        n = nrofitems - start;
        hasNext = false;
      }
      else {
        hasNext = true;
        n = NewsReader.ITEMXPAGE;
      }
      int base = 0;
      if (page > 0) {
        base = 1;
        hasPrev = true;
      }
      else {
        hasPrev = false;
      }
      itemsTable.removeAllElements();
      sNewsList.setSelectCommand(NewsReader.cREAD);
      RSSItem tmpitem;
      Image img;
      // Add all the items to the itemlist and to the table
      if (hasPrev) {
        sNewsList.append(Application.messages[NewsReader.MSG_PREV], null);
      }
      for (int i = 0; i < n; i++) {
        tmpitem = currentFeed.getItem(start + i);
        itemsTable.addElement(tmpitem);
        if (tmpitem.isRead) {
          img = iRead;
        }
        else {
          img = iUnread;
        }
        sNewsList.append(tmpitem.title, img);
      }
      if (hasNext) {
        nextIdx = sNewsList.append(Application.messages[NewsReader.MSG_NEXT], null);
      }
      else {
        nextIdx = -1;
      }
      Font f;
      for (int i = 0; i < n; i++) {
        tmpitem = currentFeed.getItem(start + i);
        if (tmpitem.isRead) {
          f = normal;
        }
        else {
          f = bold;
        }
        sNewsList.setFont(base + i, f);
      }
    }
    if ((oldPos >= 0) && (keepPos)) {
      sNewsList.setSelectedIndex(oldPos, true);
    }
    return sNewsList;
  }

  /**
   * Returns a form with the given item info. The form is always refreshed when this method is called.
   * @param item the item that should be displayed
   * @return the refreshed form with info about the current item
   */
  public Displayable getNewsDataHTML() {
    return new ShowNews(this, currentFeed, currentItem);
  }

  public Displayable getNewsDataTEXT() {
    Form itemform;
    itemform = new Form(currentItem.title);
    if (!currentItem.description.equals(NewsReader.STR_EMPTY)) {
      itemform.append(currentItem.description + BaseApp.CR);
    }
    else {
      // Print the title if no description exists
      itemform.append(currentItem.title + BaseApp.CR);
    }
    if ((currentItem.image != null) && (!currentItem.image.equals(NewsReader.STR_EMPTY))) {
      // Handle the Image
    }
    // Print the published date if it exists
    if (!currentItem.pubDate.equals(NewsReader.STR_EMPTY)) {
      o[0] = currentItem.pubDate;
      itemform.append(Application.format(NewsReader.MSG_FEEDITEMINFO01, o));
      itemform.append(BaseApp.sCR);
    }
    if ((currentItem.link != null) && (!currentItem.link.equals(NewsReader.STR_EMPTY))) {
      itemform.addCommand(NewsReader.cGO);
    }
    else {
      itemform.removeCommand(NewsReader.cGO);
    }
    Application.setup(itemform, Application.cBACK, null);
    // Print the parsed date
    parsecal.setTime(new Date(currentItem.parseTime));
    o[0] = formatDate(parsecal, new StringBuffer()).toString();
    o[1] = formatTime(parsecal, false, new StringBuffer()).toString();
    o[2] = NewsReader.timeDiff(currentItem.parseTime, System.currentTimeMillis());
    itemform.append(Application.format(NewsReader.MSG_FEEDITEMINFO02, o));
    return itemform;
  }

  public int compare(final Object o1, final Object o2) {
    return ((Pair) o1).name.compareTo(((Pair) o2).name);
  }

}
