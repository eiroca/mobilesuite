import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Newsreader extends MIDlet implements CommandListener {

  static final String URL = "http://www.newsforge.com/newsforge.xml";
  static final String TITLE = "NewsForge";

  Vector descriptions = new Vector();
  List newsList = new List(Newsreader.TITLE, Choice.IMPLICIT);
  TextBox textBox = new TextBox("", "", 256, TextField.ANY);
  Display display;

  Command backCmd = new Command("Back", Command.BACK, 0);

  class ReadThread extends Thread {

    public void run() {
      try {
        final HttpConnection httpConnection = (HttpConnection) Connector.open(Newsreader.URL);
        final KXmlParser parser = new KXmlParser();
        parser.setInput(new InputStreamReader(httpConnection.openInputStream()));
        // parser.relaxed = true;
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "backslash");
        while (parser.nextTag() != XmlPullParser.END_TAG) {
          readStory(parser);
        }
        parser.require(XmlPullParser.END_TAG, null, "backslash");
        parser.next();
        parser.require(XmlPullParser.END_DOCUMENT, null, null);
      }
      catch (final Exception e) {
        e.printStackTrace();
        descriptions.addElement(e.toString());
        newsList.append("Error", null);
      }
    }

    /** Read a story and append it to the list */

    void readStory(final KXmlParser parser) throws IOException, XmlPullParserException {
      parser.require(XmlPullParser.START_TAG, null, "story");
      String title = null;
      String description = null;
      while (parser.nextTag() != XmlPullParser.END_TAG) {
        parser.require(XmlPullParser.START_TAG, null, null);
        final String name = parser.getName();
        final String text = parser.nextText();
        System.out.println("<" + name + ">" + text);
        if (name.equals("title")) {
          title = text;
        }
        else if (name.equals("description")) {
          description = text;
        }
        parser.require(XmlPullParser.END_TAG, null, name);
      }
      parser.require(XmlPullParser.END_TAG, null, "story");
      if (title != null) {
        descriptions.addElement("" + description);
        newsList.append(title, null);
      }
    }
  }

  public void startApp() {
    display = Display.getDisplay(this);
    display.setCurrent(newsList);
    newsList.setCommandListener(this);
    textBox.setCommandListener(this);
    textBox.addCommand(backCmd);
    new ReadThread().start();
  }

  public void pauseApp() {
    //
  }

  public void commandAction(final Command c, final Displayable d) {
    if (c == List.SELECT_COMMAND) {
      final String text = (String) descriptions.elementAt(newsList.getSelectedIndex());
      if (textBox.getMaxSize() < text.length()) {
        textBox.setMaxSize(text.length());
      }
      textBox.setString(text);
      display.setCurrent(textBox);
    }
    else if (c == backCmd) {
      display.setCurrent(newsList);
    }
  }

  public void destroyApp(final boolean really) {
    //
  }

}