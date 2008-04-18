package test;

import java.util.Vector;
import net.eiroca.j2me.observable.Observable;
import net.eiroca.j2me.observable.Observer;
import net.eiroca.j2me.observable.ObserverManager;
import net.eiroca.j2me.util.HTTPClient;

public class DataSender implements Observable, Observer {

  private static final int SIZE = 800;
  Suite suite;
  String version;
  int status;
  private final ObserverManager manager = new ObserverManager();

  public DataSender(final Suite suite, final String version) {
    this.suite = suite;
    this.version = version;
  }

  public void submit(final String url) {
    HTTPClient client = new HTTPClient();
    client.addObserver(this);
    Vector tests = suite.getTests();
    if (SIZE > 0) {
      client.mode = HTTPClient.MODE_POST;
      int part = 0;
      int i = 0;
      int siz = 0;
      setStatus(part);
      while (i < tests.size()) {
        final TestResult inf = (TestResult) tests.elementAt(i);
        final String v = (inf.val == null ? "" : inf.val.toString());
        final String k = inf.key.toString();
        client.addParameter(k, v);
        siz = siz + k.length() + v.length();
        System.out.println("K=" + k + " V=" + v + " S=" + siz);
        if (siz > SIZE) {
          client.addParameter("TestSuite", version);
          client.addParameter("PART", Integer.toString(part));
          client.submit(url);
          part++;
          setStatus(part);
          siz = 0;
          try {
            Thread.sleep(3000);
          }
          catch (InterruptedException e) {
          }
          client = new HTTPClient();
          client.mode = HTTPClient.MODE_POST;
        }
        i++;
      }
      if (siz > 0) {
        client.addParameter("TestSuite", version);
        client.addParameter("PART", Integer.toString(part));
        client.submit(url);
        setStatus(part);
      }
    }
    else {
      client.mode = HTTPClient.MODE_MULTIPART;
      client.addParameter("TestSuite", version);
      client.addAttach(suite);
      client.submit(url);
    }
  }

  public ObserverManager getObserverManager() {
    return manager;
  }

  public void addObserver(final Observer observer) {
    manager.addObserver(observer);
  }

  public void removeObserver(final Observer observer) {
    manager.removeObserver(observer);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(final int status) {
    this.status = status;
    manager.notifyObservers(this);
  }

  public void changed(Observable observable) {
    manager.notifyObservers(observable);
  }

}
