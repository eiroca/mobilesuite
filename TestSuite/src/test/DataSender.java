package test;

import net.eiroca.j2me.util.HTTPClient;

public class DataSender extends HTTPClient {

  Suite suite;
  String version;

  public DataSender(final Suite suite, final String version) {
    this.suite = suite;
    this.version = version;
    usePost = true;
    //useMultipart = true;
  }

  public void submit(final String url) {
    addParameter("TestSuite", version);
    suite.writeData(this);
    //addAttach(suite);
    super.submit(url);
  }

}
