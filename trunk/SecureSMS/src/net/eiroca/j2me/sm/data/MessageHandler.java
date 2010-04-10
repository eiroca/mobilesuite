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
package net.eiroca.j2me.sm.data;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.sm.util.Store;
import net.eiroca.j2me.sm.util.StoreException;
import net.eiroca.j2me.sm.util.StoreObserver;
import net.eiroca.j2me.util.CipherDES;

/**
 * Implementation of the MessageHandler. This implementation is Nokia Series 60 specific, but may be compatible with other phones supporting WMA.
 */
public class MessageHandler implements MessageListener, StoreObserver {

  /** The connection. */
  MessageConnection connection;

  /** The Constant port. */
  private static final int port = 9087;

  /** The chiper. */
  public CipherDES chiper;
  
  /** The inbox store. */
  public SecureMessageStore inboxStore;
  
  /** The outbox store. */
  public SecureMessageStore outboxStore;
  
  /** The address book store. */
  public AddressStore addressBookStore;
  
  /** The unknown store. */
  public UnknownStore unknownStore;

  /**
   * The constructor made the package-visible for security reasons.
   */
  public MessageHandler() {
    //
  }

  /**
   * Inits the.
   * 
   * @throws StoreException the store exception
   */
  public void init() throws StoreException {
    final String connectionString = "sms://:" + MessageHandler.port;
    try {
      connection = (MessageConnection) Connector.open(connectionString);
      connection.setMessageListener(this);
    }
    catch (final IOException ioe) {
      throw new MessageHandlerException(MessageHandlerException.ERR_OPENCONNECTION);
    }
  }

  /**
   * Done.
   * 
   * @throws StoreException the store exception
   */
  public void done() throws StoreException {
    try {
      connection.setMessageListener(null);
      connection.close();
    }
    catch (final IOException ioe) {
      throw new MessageHandlerException(MessageHandlerException.ERR_CLOSECONNECTION);
    }
  }

  /**
   * Send.
   * 
   * @param msg the msg
   * @throws StoreException the store exception
   */
  public synchronized void send(final SecureMessage msg) throws StoreException {
    MessageSender sender;
    // Use the key supplied to transcode the message and send it
    // Encode the message using the key
    byte[] key = null;
    final Address address = addressBookStore.getByNumber(msg.number, false);
    if (address != null) {
      key = address.getKeyData();
    }
    if (key == null) { throw new MessageHandlerException(MessageHandlerException.ERR_INVALIDKEY); }
    byte[] data = Store.encodeData(msg.text);
    data = chiper.encode(data, key);
    // data = PackerHelper.compress(data);
    // Find the connection to use and send the message
    // Create listener for each address in the AddressStore
    final String wmaAddress = new StringBuffer("sms://").append(address.number).append(":").append(MessageHandler.port).toString();
    final BinaryMessage wmaMessage = (BinaryMessage) connection.newMessage(MessageConnection.BINARY_MESSAGE);
    wmaMessage.setAddress(wmaAddress);
    wmaMessage.setPayloadData(data);
    sender = new MessageSender(this, wmaMessage);
    sender.start();
    int count = 99;
    while ((sender.getStatus() == 0) & (--count >= 0)) {
      try {
        Thread.sleep(100);
      }
      catch (final InterruptedException e) {
      }
    }
    if (sender.getStatus() > 0) {
      outboxStore.store(msg);
    }
    else {
      throw new MessageHandlerException(MessageHandlerException.ERR_SENDMESSAGE);
    }
  }

  /**
   * Adds the message.
   * 
   * @param aDate the a date
   * @param address the address
   * @param data the data
   * @throws StoreException the store exception
   */
  public synchronized void addMessage(final Long aDate, final Address address, byte[] data) throws StoreException {
    final byte[] key = address.getKeyData();
    data = chiper.decode(data, key);
    // data = PackerHelper.decompress(data);
    final String msg = Store.decodeData(data);
    // Create new message
    final SecureMessage message = new SecureMessage(aDate, address.number, msg, 0);
    inboxStore.store(message);
  }

  /**
   * Receive.
   */
  public synchronized void receive() {
    try {
      // Store the message in the message store
      // Note: use fixed factory class name as we know for sure
      // which factory should be used.
      final Message wmaMessage = connection.receive();
      if (!(wmaMessage instanceof BinaryMessage)) {
        // It is safe to skip non-binary message, return.
        return;
      }
      // WMA address is in format "sms://<number>:<port>", so parse it here to remove prefix and port number
      final String wmaAddress = wmaMessage.getAddress();
      final String number = BaseApp.normalizeTelNum(wmaAddress.substring(6, wmaAddress.indexOf(":", 6)));
      Address address = addressBookStore.getByNumber(number, false);
      if (address == null) {
        address = new Address("Unkwnon", number, "");
        addressBookStore.store(address);
        final UnknownMessage mes = new UnknownMessage(number, ((BinaryMessage) wmaMessage).getPayloadData());
        unknownStore.store(mes);
      }
      else {
        addMessage(null, address, ((BinaryMessage) wmaMessage).getPayloadData());
      }
    }
    catch (final IOException ioe) {
    }
    catch (final StoreException sme) {
    }
  }

  /**
   * Address name.
   * 
   * @param message the message
   * @return the string
   */
  public String addressName(final SecureMessage message) {
    String number = message.number;
    Address address = null;
    try {
      address = addressBookStore.getByNumber(number, false);
    }
    catch (final StoreException e) {
    }
    if (address != null) {
      number = address.name;
    }
    return number;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.sm.util.StoreObserver#actionDone(int, java.lang.Object, net.eiroca.j2me.sm.util.Store)
   */
  public void actionDone(final int action, final Object obj, final Store store) {
    if (action == StoreObserver.ADD) {
      final Address address = (Address) obj;
      final byte[] key = address.getKeyData();
      final String number = address.number;
      if ((key != null) && (number != null)) {
        unknownStore.recoverMessages(this, address);
      }
    }
  }

  /**
   * Hook for incoming message notification.
   * 
   * @param messageConnection MessageConnection on which the message was received.
   */
  public void notifyIncomingMessage(final MessageConnection messageConnection) {
    // To avoid spending too much memory we use same instance of Runnable (this) to start all threads.
    new MessageReceiver(this).start();
  }

}
