/** GPL >= 3.0
 * Based upon SecureMessenger
 * 
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002 Eugene Morozov
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
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.rms.Settings;
import net.eiroca.j2me.sm.data.Address;
import net.eiroca.j2me.sm.data.AddressStore;
import net.eiroca.j2me.sm.data.MessageHandler;
import net.eiroca.j2me.sm.data.SecureMessage;
import net.eiroca.j2me.sm.data.SecureMessageStore;
import net.eiroca.j2me.sm.data.UnknownMessage;
import net.eiroca.j2me.sm.data.UnknownStore;
import net.eiroca.j2me.sm.ui.AddressBookScreen;
import net.eiroca.j2me.sm.ui.AddressScreen;
import net.eiroca.j2me.sm.ui.InsertPINScreen;
import net.eiroca.j2me.sm.ui.MessageListScreen;
import net.eiroca.j2me.sm.ui.MessageScreen;
import net.eiroca.j2me.sm.ui.PINChangeScreen;
import net.eiroca.j2me.sm.ui.SendNewScreen;
import net.eiroca.j2me.sm.util.Store;
import net.eiroca.j2me.sm.util.StoreException;
import net.eiroca.j2me.sm.util.StoreObserver;
import net.eiroca.j2me.util.CipherDES;

/**
 * The Class SecureSMS.
 */
public class SecureSMS extends Application implements StoreObserver {

  /** The Constant MSG_SECUREMESSENGER. */
  public static final int MSG_SECUREMESSENGER = 0;
  
  /** The Constant MSG_OK. */
  public static final int MSG_OK = 1;
  
  /** The Constant MSG_SAVE. */
  public static final int MSG_SAVE = 2;
  
  /** The Constant MSG_SEND. */
  public static final int MSG_SEND = 3;
  
  /** The Constant MSG_REPLY. */
  public static final int MSG_REPLY = 4;
  
  /** The Constant MSG_DELETE. */
  public static final int MSG_DELETE = 5;
  
  /** The Constant MSG_ADD. */
  public static final int MSG_ADD = 6;
  
  /** The Constant MSG_CANCEL. */
  public static final int MSG_CANCEL = 7;
  
  /** The Constant MSG_EXIT. */
  public static final int MSG_EXIT = 8;
  
  /** The Constant MSG_INBOX. */
  public static final int MSG_INBOX = 9;
  
  /** The Constant MSG_SENDNEW. */
  public static final int MSG_SENDNEW = 10;
  
  /** The Constant MSG_SENTITEMS. */
  public static final int MSG_SENTITEMS = 11;
  
  /** The Constant MSG_ADDRESSBOOK. */
  public static final int MSG_ADDRESSBOOK = 12;
  
  /** The Constant MSG_MESSAGE. */
  public static final int MSG_MESSAGE = 13;
  
  /** The Constant MSG_ADDRESS. */
  public static final int MSG_ADDRESS = 14;
  
  /** The Constant MSG_NEWADDRESS. */
  public static final int MSG_NEWADDRESS = 15;
  
  /** The Constant MSG_NEWMESSAGE. */
  public static final int MSG_NEWMESSAGE = 16;
  
  /** The Constant MSG_TEXT. */
  public static final int MSG_TEXT = 17;
  
  /** The Constant MSG_TO. */
  public static final int MSG_TO = 18;
  
  /** The Constant MSG_FROM. */
  public static final int MSG_FROM = 19;
  
  /** The Constant MSG_NUMBER. */
  public static final int MSG_NUMBER = 20;
  
  /** The Constant MSG_NAME. */
  public static final int MSG_NAME = 21;
  
  /** The Constant MSG_KEY. */
  public static final int MSG_KEY = 22;
  
  /** The Constant MSG_MESSAGESENT. */
  public static final int MSG_MESSAGESENT = 23;
  
  /** The Constant MSG_MESSAGEHASNOTBEENSENT. */
  public static final int MSG_MESSAGEHASNOTBEENSENT = 24;
  
  /** The Constant MSG_MESSAGEDELETED. */
  public static final int MSG_MESSAGEDELETED = 25;
  
  /** The Constant MSG_MESSAGEHASNOTBEENDELETED. */
  public static final int MSG_MESSAGEHASNOTBEENDELETED = 26;
  
  /** The Constant MSG_MESSAGERECEIVED. */
  public static final int MSG_MESSAGERECEIVED = 27;
  
  /** The Constant MSG_ADDRESSSAVED. */
  public static final int MSG_ADDRESSSAVED = 28;
  
  /** The Constant MSG_ADDRESSHASNOTBEENSAVED. */
  public static final int MSG_ADDRESSHASNOTBEENSAVED = 29;
  
  /** The Constant MSG_ADDRESSDELETED. */
  public static final int MSG_ADDRESSDELETED = 30;
  
  /** The Constant MSG_ADDRESSHASNOTBEENDELETED. */
  public static final int MSG_ADDRESSHASNOTBEENDELETED = 31;
  
  /** The Constant MSG_ERROR. */
  public static final int MSG_ERROR = 32;
  
  /** The Constant MSG_INFO. */
  public static final int MSG_INFO = 33;
  
  /** The Constant MSG_MESSAGESTOREERROR. */
  public static final int MSG_MESSAGESTOREERROR = 34;
  
  /** The Constant MSG_ADDRESSSTOREERROR. */
  public static final int MSG_ADDRESSSTOREERROR = 35;
  
  /** The Constant MSG_CANNOTSTART. */
  public static final int MSG_CANNOTSTART = 36;
  
  /** The Constant MSG_BACK. */
  public static final int MSG_BACK = 37;
  
  /** The Constant MSG_MENUABOUT. */
  public static final int MSG_MENUABOUT = 38;
  
  /** The Constant MSG_CHANGEPIN. */
  public static final int MSG_CHANGEPIN = 39;
  
  /** The Constant MSG_INSERTPINTEXT. */
  public static final int MSG_INSERTPINTEXT = 40;
  
  /** The Constant MSG_PIN. */
  public static final int MSG_PIN = 41;
  
  /** The Constant MSG_WRONGPIN. */
  public static final int MSG_WRONGPIN = 42;
  
  /** The Constant MSG_INSERTPIN. */
  public static final int MSG_INSERTPIN = 43;
  
  /** The Constant MSG_INVALINDPIN. */
  public static final int MSG_INVALINDPIN = 44;
  
  /** The Constant MSG_CLEANUP. */
  public static final int MSG_CLEANUP = 45;
  
  /** The Constant MSG_CLEANUP1. */
  public static final int MSG_CLEANUP1 = 46;
  
  /** The Constant MSG_CLEANUP2. */
  public static final int MSG_CLEANUP2 = 47;
  
  /** The Constant MSG_CLEANUP3. */
  public static final int MSG_CLEANUP3 = 48;
  
  /** The Constant MSG_CONFIRM. */
  public static final int MSG_CONFIRM = 49;
  
  /** The Constant MSG_ARESURE. */
  public static final int MSG_ARESURE = 50;
  
  /** The Constant MSG_YES. */
  public static final int MSG_YES = 51;
  
  /** The Constant MSG_NO. */
  public static final int MSG_NO = 52;
  
  /** The Constant MSG_NUMPREFIX. */
  public static final int MSG_NUMPREFIX = 53;
  
  /** The Constant MSG_INVALID. */
  public static final int MSG_INVALID = 54;
  
  /** The Constant MSG_MESSAGEIVALID. */
  public static final int MSG_MESSAGEIVALID = 55;
  
  /** The Constant MSG_ADDRESSBOOKEMPTY. */
  public static final int MSG_ADDRESSBOOKEMPTY = 56;

  /** The Constant ME_MAINMENU. */
  public static final int ME_MAINMENU = 0;
  
  /** The Constant ME_CLEANUP. */
  public static final int ME_CLEANUP = 1;

  /** The Constant AC_SHOWABOUT. */
  public static final int AC_SHOWABOUT = 1;
  
  /** The Constant AC_SHOWINBOX. */
  public static final int AC_SHOWINBOX = 2;
  
  /** The Constant AC_SHOWSENDNEW. */
  public static final int AC_SHOWSENDNEW = 3;
  
  /** The Constant AC_SHOWSENTITEMS. */
  public static final int AC_SHOWSENTITEMS = 4;
  
  /** The Constant AC_SHOWADDRESSBOOK. */
  public static final int AC_SHOWADDRESSBOOK = 5;
  
  /** The Constant AC_SENDNEW. */
  public static final int AC_SENDNEW = 6;
  
  /** The Constant AC_ADDRESSBOOKEDT. */
  public static final int AC_ADDRESSBOOKEDT = 7;
  
  /** The Constant AC_ADDRESSBOOKADD. */
  public static final int AC_ADDRESSBOOKADD = 8;
  
  /** The Constant AC_ADDRESSBOOKDEL. */
  public static final int AC_ADDRESSBOOKDEL = 9;
  
  /** The Constant AC_ADDRESSBOOKSAV. */
  public static final int AC_ADDRESSBOOKSAV = 10;
  
  /** The Constant AC_INBOXVIEW. */
  public static final int AC_INBOXVIEW = 11;
  
  /** The Constant AC_INBOXDELETE. */
  public static final int AC_INBOXDELETE = 12;
  
  /** The Constant AC_INBOXREPLY. */
  public static final int AC_INBOXREPLY = 13;
  
  /** The Constant AC_SENTVIEW. */
  public static final int AC_SENTVIEW = 14;
  
  /** The Constant AC_SENTDELETE. */
  public static final int AC_SENTDELETE = 15;
  
  /** The Constant AC_SHOWPINCHANGE. */
  public static final int AC_SHOWPINCHANGE = 16;
  
  /** The Constant AC_PINSAVE. */
  public static final int AC_PINSAVE = 17;
  
  /** The Constant AC_PINDELETE. */
  public static final int AC_PINDELETE = 18;
  
  /** The Constant AC_PINOK. */
  public static final int AC_PINOK = 19;
  
  /** The Constant AC_CLEANUP. */
  public static final int AC_CLEANUP = 20;
  
  /** The Constant AC_CLEANUP1. */
  public static final int AC_CLEANUP1 = 21;
  
  /** The Constant AC_CLEANUP2. */
  public static final int AC_CLEANUP2 = 22;
  
  /** The Constant AC_CLEANUP3. */
  public static final int AC_CLEANUP3 = 23;
  
  /** The Constant AC_YES. */
  public static final int AC_YES = 24;
  
  /** The Constant AC_NO. */
  public static final int AC_NO = 25;
  
  /** The Constant AC_INVALID. */
  public static final int AC_INVALID = 26;

  // System-independent store names - this will stay constant across the
  // releases to archive the backward compatibility
  /** The Constant ADDRESS_BOOK_STORE_NAME. */
  private static final String ADDRESS_BOOK_STORE_NAME = "ab";
  
  /** The Constant INBOX_STORE_NAME. */
  private static final String INBOX_STORE_NAME = "ib";
  
  /** The Constant OUTBOX_STORE_NAME. */
  private static final String OUTBOX_STORE_NAME = "ob";
  
  /** The Constant UNKNOWN_STORE_NAME. */
  private static final String UNKNOWN_STORE_NAME = "uk";

  /** The c adrdel. */
  public static Command cADRDEL;
  
  /** The c adrsav. */
  public static Command cADRSAV;
  
  /** The c inboxdel. */
  public static Command cINBOXDEL;
  
  /** The c inboxreply. */
  public static Command cINBOXREPLY;
  
  /** The c sentdel. */
  public static Command cSENTDEL;
  
  /** The c sendnew. */
  public static Command cSENDNEW;
  
  /** The c adradd. */
  public static Command cADRADD;
  
  /** The c pinsav. */
  public static Command cPINSAV;
  
  /** The c pindel. */
  public static Command cPINDEL;
  
  /** The c pinok. */
  public static Command cPINOK;
  
  /** The c yes. */
  public static Command cYES;
  
  /** The c no. */
  public static Command cNO;
  
  /** The c invalid. */
  public static Command cINVALID;

  // Messenger objects - stores and handlers
  /** The inbox. */
  private SecureMessageStore inbox;
  
  /** The sent items. */
  private SecureMessageStore sentItems;
  
  /** The address book. */
  private AddressStore addressBook;
  
  /** The unknown. */
  private UnknownStore unknown;
  
  /** The handler. */
  public MessageHandler handler;

  // Temporary message and address objects
  /** The message. */
  private SecureMessage message;
  
  /** The address. */
  private Address address;

  // Messenger screens
  /** The sc menu. */
  private List scMenu;
  
  /** The sc menu clean up. */
  private List scMenuCleanUp;
  
  /** The sc send new. */
  private SendNewScreen scSendNew;
  
  /** The sc address. */
  private AddressScreen scAddress;
  
  /** The sc address book. */
  private AddressBookScreen scAddressBook;
  
  /** The sc message. */
  private MessageScreen scMessage;
  
  /** The sc inbox. */
  private MessageListScreen scInbox;
  
  /** The sc sent items. */
  private MessageListScreen scSentItems;
  
  /** The sc pin change. */
  private PINChangeScreen scPINChange;
  
  /** The sc insert pin. */
  private InsertPINScreen scInsertPIN;

  /** The ST s_ pin. */
  private static String STS_PIN = "PIN";

  /** The Constant RES_MESSAGES. */
  private static final String RES_MESSAGES = "messages.txt";
  
  /** The Constant RES_ABOUT. */
  private static final String RES_ABOUT = "about.txt";

  /** The menu clean up. */
  short[][] menuCleanUp;

  /**
   * Instantiates a new secure sms.
   */
  public SecureSMS() {
    super();
    BaseApp.resPrefix = "se";
    Application.messages = BaseApp.readStrings(RES_MESSAGES);
    Application.cOK = Application.newCommand(SecureSMS.MSG_OK, Command.OK, 30, 0);
    Application.cBACK = Application.newCommand(SecureSMS.MSG_BACK, Command.BACK, 20, Application.AC_BACK);
    Application.cEXIT = Application.newCommand(SecureSMS.MSG_EXIT, Command.EXIT, 10, Application.AC_EXIT);
    SecureSMS.cADRADD = Application.newCommand(SecureSMS.MSG_ADD, Command.OK, 2, SecureSMS.AC_ADDRESSBOOKADD);
    SecureSMS.cADRDEL = Application.newCommand(SecureSMS.MSG_DELETE, Command.OK, 2, SecureSMS.AC_ADDRESSBOOKDEL);
    SecureSMS.cADRSAV = Application.newCommand(SecureSMS.MSG_SAVE, Command.OK, 2, SecureSMS.AC_ADDRESSBOOKSAV);
    SecureSMS.cINBOXDEL = Application.newCommand(SecureSMS.MSG_DELETE, Command.OK, 2, SecureSMS.AC_INBOXDELETE);
    SecureSMS.cINBOXREPLY = Application.newCommand(SecureSMS.MSG_REPLY, Command.OK, 2, SecureSMS.AC_INBOXREPLY);
    SecureSMS.cSENTDEL = Application.newCommand(SecureSMS.MSG_DELETE, Command.OK, 2, SecureSMS.AC_SENTDELETE);
    SecureSMS.cSENDNEW = Application.newCommand(SecureSMS.MSG_SEND, Command.OK, 2, SecureSMS.AC_SENDNEW);
    SecureSMS.cPINSAV = Application.newCommand(SecureSMS.MSG_SAVE, Command.OK, 2, SecureSMS.AC_PINSAVE);
    SecureSMS.cPINDEL = Application.newCommand(SecureSMS.MSG_DELETE, Command.OK, 2, SecureSMS.AC_PINDELETE);
    SecureSMS.cPINOK = Application.newCommand(SecureSMS.MSG_OK, Command.OK, 2, SecureSMS.AC_PINOK);
    SecureSMS.cYES = Application.newCommand(SecureSMS.MSG_YES, Command.OK, 2, SecureSMS.AC_YES);
    SecureSMS.cNO = Application.newCommand(SecureSMS.MSG_NO, Command.BACK, 1, SecureSMS.AC_NO);
    SecureSMS.cINVALID = Application.newCommand(SecureSMS.MSG_INVALID, Command.OK, 1, SecureSMS.AC_INVALID);
    Application.menu = new short[][] {
        {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_INBOX, SecureSMS.AC_SHOWINBOX, -1
        }, {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_SENDNEW, SecureSMS.AC_SHOWSENDNEW, -1
        }, {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_SENTITEMS, SecureSMS.AC_SHOWSENTITEMS, -1
        }, {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_ADDRESSBOOK, SecureSMS.AC_SHOWADDRESSBOOK, -1
        }, {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_CHANGEPIN, SecureSMS.AC_SHOWPINCHANGE, -1
        }, {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_CLEANUP, SecureSMS.AC_CLEANUP, -1
        }, {
            SecureSMS.ME_MAINMENU, SecureSMS.MSG_MENUABOUT, SecureSMS.AC_SHOWABOUT, -1
        }, {
            SecureSMS.ME_CLEANUP, SecureSMS.MSG_CLEANUP1, SecureSMS.AC_CLEANUP1, -1
        }, {
            SecureSMS.ME_CLEANUP, SecureSMS.MSG_CLEANUP2, SecureSMS.AC_CLEANUP2, -1
        }, {
            SecureSMS.ME_CLEANUP, SecureSMS.MSG_CLEANUP3, SecureSMS.AC_CLEANUP3, -1
        }
    };
    Settings.load();
  }

  /**
   * Start the MIDlet.
   */
  public void init() {
    super.init();
    try {
      // Initialize everything
      // Initialize the address book and two message stores
      addressBook = new AddressStore(SecureSMS.ADDRESS_BOOK_STORE_NAME);
      inbox = new SecureMessageStore(SecureSMS.INBOX_STORE_NAME);
      sentItems = new SecureMessageStore(SecureSMS.OUTBOX_STORE_NAME);
      unknown = new UnknownStore(SecureSMS.UNKNOWN_STORE_NAME);
      handler = new MessageHandler();
      handler.inboxStore = inbox;
      handler.outboxStore = sentItems;
      handler.addressBookStore = addressBook;
      handler.unknownStore = unknown;
      handler.chiper = new CipherDES();
      handler.init();
      inbox.registerObserver(this);
      addressBook.registerObserver(handler);
      scMenu = Application.getMenu(Application.messages[SecureSMS.MSG_SECUREMESSENGER], SecureSMS.ME_MAINMENU, -1, Application.cEXIT);
      final String pin = Settings.get(SecureSMS.STS_PIN);
      if (pin != null) {
        scInsertPIN = new InsertPINScreen(SecureSMS.MSG_INSERTPIN);
        BaseApp.setDisplay(scInsertPIN);
      }
      else {
        Application.show(null, scMenu, true);
      }
    }
    catch (final StoreException sme) {
      // Exit without any messages
      try {
        destroyApp(true);
      }
      catch (final Exception e) {
      }
    }
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.app.Application#done()
   */
  protected void done() {
    try {
      handler.done();
    }
    catch (final StoreException e) {
    }
    super.done();
  }

  /** The next action. */
  private int nextAction = SecureSMS.AC_NO;

  // Implementation of the command listener interface
  /* (non-Javadoc)
   * @see net.eiroca.j2me.app.Application#handleAction(int, javax.microedition.lcdui.Displayable, javax.microedition.lcdui.Command)
   */
  public boolean handleAction(int action, final Displayable d, final Command cmd) {
    boolean confirmed = false;
    if (action == SecureSMS.AC_YES) {
      confirmed = true;
      action = nextAction;
    }
    if (action == SecureSMS.AC_NO) {
      Application.back(null);
      return true;
    }
    nextAction = action;
    int errMsg = SecureSMS.MSG_MESSAGESTOREERROR;
    Displayable back = scMenu;
    boolean processed = true;
    try {
      long id;
      switch (action) {
        case AC_SHOWINBOX:
          scInbox = new MessageListScreen(SecureSMS.MSG_INBOX, SecureSMS.cINBOXDEL, SecureSMS.cINBOXREPLY, SecureSMS.cINVALID);
          Application.registerList(scInbox, SecureSMS.AC_INBOXVIEW);
          scInbox.updateMessageList(this, inbox);
          Application.show(null, scInbox, true);
          break;
        case AC_SHOWSENDNEW:
          errMsg = SecureSMS.MSG_ADDRESSBOOKEMPTY;
          back = scMenu;
          message = new SecureMessage(null, null, "", 0);
          scSendNew = new SendNewScreen();
          final boolean valid = scSendNew.updateMessage(message, addressBook);
          if (valid) {
            Application.show(null, scSendNew, true);
          }
          else {
            throw new Exception();
          }
          break;
        case AC_SHOWSENTITEMS:
          scSentItems = new MessageListScreen(SecureSMS.MSG_SENTITEMS, SecureSMS.cSENTDEL, null, null);
          Application.registerList(scSentItems, SecureSMS.AC_SENTVIEW);
          scSentItems.updateMessageList(this, sentItems);
          Application.show(null, scSentItems, true);
          break;
        case AC_SHOWADDRESSBOOK:
          scAddressBook = new AddressBookScreen();
          scAddressBook.updateAddressList(addressBook);
          Application.registerList(scAddressBook, SecureSMS.AC_ADDRESSBOOKEDT);
          Application.show(null, scAddressBook, true);
          break;
        case AC_SHOWABOUT:
          Application.show(null, Application.getTextForm(SecureSMS.MSG_MENUABOUT, RES_ABOUT), true);
          break;
        case AC_SENDNEW:
          errMsg = SecureSMS.MSG_MESSAGEHASNOTBEENSENT;
          back = scSendNew;
          id = scSendNew.getSelectedAddressId();
          message.number = addressBook.getById(id).number;
          message.text = scSendNew.getMessageText();
          handler.send(message);
          Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_MESSAGESENT, null, AlertType.INFO, scMenu, Alert.FOREVER);
          break;
        case AC_ADDRESSBOOKEDT:
          errMsg = SecureSMS.MSG_ADDRESSSTOREERROR;
          back = scAddressBook;
          id = scAddressBook.getSelectedAddressId();
          address = addressBook.getById(id);
          scAddress = new AddressScreen(SecureSMS.MSG_ADDRESS, false);
          scAddress.fromAddress(address);
          Application.show(null, scAddress, false);
          break;
        case AC_ADDRESSBOOKADD:
          errMsg = SecureSMS.MSG_ADDRESSSTOREERROR;
          back = scAddressBook;
          address = new Address("", "", "");
          scAddress = new AddressScreen(SecureSMS.MSG_NEWADDRESS, true);
          scAddress.fromAddress(address);
          Application.show(null, scAddress, false);
          break;
        case AC_ADDRESSBOOKDEL:
          errMsg = SecureSMS.MSG_ADDRESSSTOREERROR;
          back = scAddressBook;
          id = scAddressBook.getSelectedAddressId();
          addressBook.remove(id);
          scAddressBook.updateAddressList(addressBook);
          Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_ADDRESSDELETED, null, AlertType.INFO, scAddressBook, Alert.FOREVER);
          break;
        case AC_ADDRESSBOOKSAV:
          errMsg = SecureSMS.MSG_ADDRESSSTOREERROR;
          back = scAddressBook;
          scAddress.toAddress(address, Application.messages[SecureSMS.MSG_NUMPREFIX]);
          addressBook.store(address);
          scAddressBook.updateAddressList(addressBook);
          Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_ADDRESSSAVED, null, AlertType.INFO, scAddressBook, Alert.FOREVER);
          break;
        case AC_INBOXVIEW:
          back = scInbox;
          id = scInbox.getSelectedMessageDate();
          message = inbox.getById(id);
          scMessage = new MessageScreen(SecureSMS.MSG_MESSAGE, SecureSMS.MSG_FROM, SecureSMS.MSG_TEXT, SecureSMS.cINBOXREPLY, SecureSMS.cINBOXDEL);
          scMessage.updateMessage(this, message);
          Application.show(null, scMessage, false);
          break;
        case AC_INBOXDELETE:
          errMsg = SecureSMS.MSG_MESSAGEHASNOTBEENDELETED;
          back = scInbox;
          id = scInbox.getSelectedMessageDate();
          inbox.remove(id);
          scInbox.updateMessageList(this, inbox);
          Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_MESSAGEDELETED, null, AlertType.INFO, scInbox, Alert.FOREVER);
          break;
        case AC_INVALID:
          back = scInbox;
          id = scInbox.getSelectedMessageDate();
          message = inbox.getById(id);
          SecureMessage message = inbox.remove(id);
          unknown.store(new UnknownMessage(message));
          scInbox.updateMessageList(this, inbox);
          Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_MESSAGEIVALID, null, AlertType.INFO, scInbox, Alert.FOREVER);
          break;
        case AC_INBOXREPLY:
          back = scInbox;
          id = scInbox.getSelectedMessageDate();
          message = inbox.getById(id);
          scSendNew = new SendNewScreen();
          scSendNew.updateMessage(message, addressBook);
          Application.show(null, scSendNew, false);
          break;
        case AC_SENTVIEW:
          back = scSentItems;
          id = scSentItems.getSelectedMessageDate();
          message = sentItems.getById(id);
          scMessage = new MessageScreen(SecureSMS.MSG_MESSAGE, SecureSMS.MSG_TO, SecureSMS.MSG_TEXT, SecureSMS.cSENTDEL, null);
          scMessage.updateMessage(this, message);
          Application.show(null, scMessage, false);
          break;
        case AC_SENTDELETE:
          errMsg = SecureSMS.MSG_MESSAGEHASNOTBEENDELETED;
          back = scSentItems;
          id = scSentItems.getSelectedMessageDate();
          sentItems.remove(id);
          scSentItems.updateMessageList(this, sentItems);
          Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_MESSAGEDELETED, null, AlertType.INFO, scSentItems, Alert.FOREVER);
          break;
        case AC_SHOWPINCHANGE:
          scPINChange = new PINChangeScreen(SecureSMS.MSG_CHANGEPIN);
          scPINChange.setPIN(Settings.get(SecureSMS.STS_PIN));
          Application.show(null, scPINChange, true);
          break;
        case AC_PINSAVE:
          final String newPIN = scPINChange.getPIN();
          if (newPIN == null) {
            Application.showAlert(SecureSMS.MSG_ERROR, SecureSMS.MSG_WRONGPIN, null, AlertType.ERROR, scPINChange, Alert.FOREVER);
          }
          else {
            Settings.put(SecureSMS.STS_PIN, newPIN);
            Settings.save();
            Application.back(null);
          }
          break;
        case AC_PINDELETE:
          Settings.put(SecureSMS.STS_PIN, null);
          Settings.save();
          Application.back(null);
          break;
        case AC_PINOK:
          final String pin = Settings.get(SecureSMS.STS_PIN);
          final String pinIns = scInsertPIN.getPIN();
          System.out.println("QUI " + pinIns + "=" + pin);
          if (pinIns.equals(pin)) {
            Application.show(null, scMenu, true);
          }
          else {
            scInsertPIN.err.setText(Application.messages[SecureSMS.MSG_INVALINDPIN]);
          }
          break;
        case AC_CLEANUP:
          scMenuCleanUp = Application.getMenu(Application.messages[SecureSMS.MSG_CLEANUP], SecureSMS.ME_CLEANUP, -1, Application.cBACK);
          Application.show(null, scMenuCleanUp, true);
          break;
        case AC_CLEANUP1:
          if (confirmed) {
            inbox.cleanup();
            unknown.cleanup();
            Application.back(null, scMenu, false);
          }
          else {
            confirm(SecureSMS.MSG_CONFIRM, SecureSMS.MSG_ARESURE, SecureSMS.cYES, SecureSMS.cNO);
          }
          break;
        case AC_CLEANUP2:
          if (confirmed) {
            sentItems.cleanup();
            Application.back(null, scMenu, false);
          }
          else {
            confirm(SecureSMS.MSG_CONFIRM, SecureSMS.MSG_ARESURE, SecureSMS.cYES, SecureSMS.cNO);
          }
          break;
        case AC_CLEANUP3:
          if (confirmed) {
            addressBook.cleanup();
            Application.back(null, scMenu, false);
          }
          else {
            confirm(SecureSMS.MSG_CONFIRM, SecureSMS.MSG_ARESURE, SecureSMS.cYES, SecureSMS.cNO);
          }
          break;
        default:
          processed = false;
          break;
      }
    }
    catch (final Throwable th) {
      th.printStackTrace();
      // set the alert type and next displayable and show the alert
      Application.showAlert(SecureSMS.MSG_ERROR, errMsg, null, AlertType.ERROR, back, Alert.FOREVER);
    }
    return processed;
  }

  // -----------------------------------------------------------------------
  // Implementation of the StoreObserver interface
  /* (non-Javadoc)
   * @see net.eiroca.j2me.sm.util.StoreObserver#actionDone(int, java.lang.Object, net.eiroca.j2me.sm.util.Store)
   */
  public void actionDone(final int action, final Object obj, final Store store) {
    if (action == StoreObserver.ADD) {
      try {
        // Note: As we register to listen on the incoming message store we should
        // not check the store reference
        // Check if we need to update the current inbox view
        // Note: display.getCurrent() may return null.
        if ((scInbox != null) && (BaseApp.getDisplay() == scInbox)) {
          scInbox.updateMessageList(this, inbox);
        }
        // Show the alert
        // Left the alertNext as is. In case of overlapping Alerts the
        // screen to be shown next remains correct.
        Application.showAlert(SecureSMS.MSG_INFO, SecureSMS.MSG_MESSAGERECEIVED, null, AlertType.INFO, null, Alert.FOREVER);
      }
      catch (final Throwable th) {
        // Ignored
      }
    }
  }

}
