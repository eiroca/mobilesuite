/** GPL >= 3.0
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
package net.eiroca.j2me.sm.ui;

import SecureSMS;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.sm.data.SecureMessage;
import net.eiroca.j2me.sm.data.SecureMessageStore;
import net.eiroca.j2me.sm.util.Store;
import net.eiroca.j2me.sm.util.StoreException;

/**
 * The message store screen.
 */
public class MessageListScreen extends List {

  /** The message dates. */
  protected long[] messageDates;
  
  /** The del. */
  private final Command del;
  
  /** The reply. */
  private final Command reply;
  
  /** The invalid. */
  private final Command invalid;

  /**
   * Creates new MessageStoreScreen.
   * 
   * @param title the title
   * @param del the del
   * @param reply the reply
   * @param invalid the invalid
   */
  public MessageListScreen(final int title, final Command del, final Command reply, final Command invalid) {
    super(Application.messages[title], Choice.IMPLICIT);
    // Add commands
    this.del = del;
    this.reply = reply;
    this.invalid = invalid;
    Application.setup(this, Application.cBACK, null);
  }

  /**
   * Returns the Id of the currently selected message.
   * 
   * @return the selected message date
   */
  public long getSelectedMessageDate() {
    final int selectedIndex = getSelectedIndex();
    if ((messageDates != null) && (selectedIndex >= 0) && (selectedIndex < messageDates.length)) {
      // Note: We add addresses in the reverse order (always to the first
      // position in list)
      return messageDates[messageDates.length - 1 - selectedIndex];
    }
    // This should actually never happen
    return -1;
  }

  /**
   * Updates the content of the screen.
   * 
   * @param app the app
   * @param messageStore the message store
   * @throws StoreException the store exception
   */
  public void updateMessageList(final SecureSMS app, final SecureMessageStore messageStore) throws StoreException {
    // Get the id of the message currently selected
    final long date = getSelectedMessageDate();
    // Remove the DELETE command
    if (del != null) {
      removeCommand(del);
    }
    if (reply != null) {
      removeCommand(reply);
    }
    if (invalid != null) {
      removeCommand(invalid);
    }
    // Delete all messages to avoid synchronization problems
    deleteAll();
    // Get the list of the Dates
    messageDates = messageStore.listIds(Store.naturalOrder);
    final int messageIdsLength = messageDates.length;
    if (messageIdsLength > 0) {
      // Create new list and populate it with the message "identifiers" - MessageTimestamp/Name
      SecureMessage message;
      final Calendar c = Calendar.getInstance();
      StringBuffer sb;
      for (int i = 0; i < messageDates.length; i++) {
        // Prepare the item titles
        message = messageStore.getById(messageDates[i]);
        c.setTime(new Date(message.date));
        final String name = app.handler.addressName(message);
        sb = new StringBuffer(32);
        app.formatDate(c, sb).append(' ').append(name);
        // Add the element to the list
        insert(0, sb.toString(), null);
        // Automatically select the message that has been selected the last time user left the list
        if (message.date == date) {
          setSelectedIndex(i, true);
        }
      }
      // Add the DELETE command if at least one message has been found
      if (del != null) {
        addCommand(del);
      }
      if (reply != null) {
        addCommand(reply);
      }
      if (invalid != null) {
        addCommand(invalid);
      }
    }
  }

}
