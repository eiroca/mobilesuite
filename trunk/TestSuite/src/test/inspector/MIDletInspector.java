package test.inspector;

import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

public class MIDletInspector extends AbstractProcessor {

  public static final String CATEGORY = "Properties";
  public static final String PREFIX = null;

  public MIDletInspector() {
    super(MIDletInspector.CATEGORY, MIDletInspector.PREFIX);
  }

  public void execute() {
    String ver = BaseApp.midlet.readAppProperty("MIDlet-Version", "1.0.0");
    addResult("TestSuite", ver);
  }

}
