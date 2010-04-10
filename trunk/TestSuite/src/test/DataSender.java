/** GPL >= 3.0
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
 */
package test;

import java.util.Vector;
import net.eiroca.j2me.observable.Observable;
import net.eiroca.j2me.observable.Observer;
import net.eiroca.j2me.observable.ObserverManager;
import net.eiroca.j2me.util.HTTPClient;

/**
 * The Class DataSender.
 */
public class DataSender implements Observable, Observer, Runnable {

  /** The suite. */
  Suite suite;
  
  /** The status. */
  String status;
  
  /** The url. */
  String url;
  
  /** The size. */
  int size = 0;

  /** The manager. */
  private final ObserverManager manager = new ObserverManager();

  /**
   * Instantiates a new data sender.
   * 
   * @param suite the suite
   */
  public DataSender(final Suite suite) {
    this.suite = suite;
  }

  /* (non-Javadoc)
   * @see java.lang.Runnable#run()
   */
  public void run() {
    final HTTPClient client = new HTTPClient();
    client.userAgent = "TestSuite DataSender";
    client.addObserver(this);
    final Vector tests = suite.getTests();
    if (size > 0) {
      client.mode = HTTPClient.MODE_POST;
      int part = 0;
      int i = 0;
      int siz = 0;
      while (i < tests.size()) {
        final TestResult inf = (TestResult) tests.elementAt(i);
        final String v = (inf.val == null ? "" : inf.val.toString());
        final String k = inf.key.toString();
        client.addParameter(k, v);
        siz = siz + k.length() + v.length();
        if (siz > size) {
          client.addParameter("_P", Integer.toString(part));
          client.submit(url, false, false);
          siz = 0;
          if (client.getStatus() >= 400) {
            break;
          }
          part++;
          client.clear();
        }
        i++;
      }
      if (siz > 0) {
        client.addParameter("_P", Integer.toString(part));
        client.submit(url, false, false);
      }
    }
    else {
      client.mode = HTTPClient.MODE_MULTIPART;
      client.addAttach(suite);
      client.submit(url, false, false);
    }
  }

  /**
   * Submit.
   * 
   * @param url the url
   * @param size the size
   */
  public void submit(final String url, final int size) {
    this.url = url;
    this.size = size;
    new Thread(this).start();
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.observable.Observable#getObserverManager()
   */
  public ObserverManager getObserverManager() {
    return manager;
  }

  /**
   * Adds the observer.
   * 
   * @param observer the observer
   */
  public void addObserver(final Observer observer) {
    manager.addObserver(observer);
  }

  /**
   * Removes the observer.
   * 
   * @param observer the observer
   */
  public void removeObserver(final Observer observer) {
    manager.removeObserver(observer);
  }

  /**
   * Gets the status.
   * 
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status.
   * 
   * @param status the new status
   */
  public void setStatus(final String status) {
    this.status = status;
    manager.notifyObservers(this);
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.observable.Observer#changed(net.eiroca.j2me.observable.Observable)
   */
  public void changed(final Observable observable) {
    final HTTPClient client = (HTTPClient) observable;
    final int stCod = client.getStatus();
    if (stCod == 999) {
      setStatus("ERR=" + client.getResult());
    }
    else if (stCod >= 400) {
      setStatus("HTTP=" + client.getStatus());
    }
  }

}
