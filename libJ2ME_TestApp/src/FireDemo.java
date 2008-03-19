import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public final class FireDemo extends MIDlet implements Runnable {

  FireCanvas canvas;

  public void startApp() {
    Display display = Display.getDisplay(this);
    canvas = new FireCanvas(this, Display.getDisplay(this));
    display.setCurrent(canvas);
    // Starts painting thread
    canvas.start();
    // Starts frame computing thread
    new Thread(this).start();
  }

  public void pauseApp() {
    //
  }

  public void destroyApp(boolean unconditional) {
    //
  }

  public void run() {
    while (true) {
      canvas.computePixels();
      try {
        Thread.sleep(10);
      }
      catch (Exception e) {
        //
      }
    }
  }

}
