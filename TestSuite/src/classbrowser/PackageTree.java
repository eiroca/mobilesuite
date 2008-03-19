/** MIT LICENSE
 * Based upon Mobile Device Tools written by Andrew Scott
 *
 * Copyright (C) 2004 Andrew Scott
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package classbrowser;

/**
 * A static class that holds an array of strings describing the possible classes
 * that may be in a J2ME class hierarchy. The Midlet will check these classes to
 * see if they are present or not, and display them appropriately. The String
 * format is chosen to indicate the branches of the tree, as well as save some
 * space in the class file. Where a package is the same as the previous one in
 * the list, the text is not included and it is represented with a single '.'.
 */
public class PackageTree {

  public static String Name[] = {
      "com.nokia.mid.sound.Sound", "....Listener", "...ui.DeviceControl", "....DirectGraphics", "....DirectUtils", "....FullCanvas", ".siemens.mp.color_game.GameCanvas", "....Layer",
      "....LayerManager", "....Sprite", "....TiledManager", "...game.ExtendedImage", "....GraphicObject", "....GraphicObjectManager", "....Light", "....Melody", "....MelodyComposer", "....Sound",
      "....Sprite", "....TiledBackground", "....Vibrator", "...gsm.Call", "....PhoneBook", "....SMS", "...io.Connection", "....ConnectionListener", "....File", "...lcdui.Image", "...m55.Ledcontrol",
      "...media.control.ToneControl", ".....VolumeControl", "...media.Control", "....Controllable", "....Manager", "....MediaException", "....Player", "....PlayerListener", "....TimeBase",
      "...ui.Image", "...MIDlet", "...NotAllowedException", "java.io.ByteArrayInputStream", "..ByteArrayOutputStream", "..DataInput", "..DataInputStream", "..DataOutput", "..DataOutputStream",
      "..EOFException", "..InputStream", "..InputStreamReader", "..InterruptedIOException", "..IOException", "..OutputStream", "..OutputStreamWriter", "..PrintStream", "..Reader",
      "..UnsupportedEncodingException", "..UTFDataFormatException", "..Writer", ".lang.ref.Reference", "...WeakReference", "..ArithmeticException", "..ArrayIndexOutOfBoundsException",
      "..ArrayStoreException", "..Boolean", "..Byte", "..Character", "..Class", "..ClassCastException", "..ClassNotFoundException", "..Error", "..Exception", "..IllegalAccessException",
      "..IllegalArgumentException", "..IllegalMonitorStateException", "..IllegalStateException", "..IllegalThreadStateException", "..IndexOutOfBoundsException", "..InstantiationException",
      "..Integer", "..InterruptedException", "..Long", "..Math", "..NegativeArraySizeException", "..NullPointerException", "..Object", "..OutOfMemoryError", "..Runnable", "..Runtime",
      "..RuntimeException", "..SecurityException", "..Short", "..String", "..StringIndexOutOfBoundsException", "..StringBuffer", "..System", "..Thread", "..Throwable", "..VirtualMachineError",
      ".util.Calendar", "..Date", "..EmptyStackException", "..Enumeration", "..Hashtable", "..NoSuchElementException", "..Random", "..Stack", "..Timer", "..TimerTask", "..TimeZone", "..Vector",
      "javax.bluetooth.BluetoothConnectionException", "..BluetoothStateException", "..DataElement", "..DeviceClass", "..DiscoveryAgent", "..DiscoveryListener", "..L2CAPConnection",
      "..L2CAPConnectionNotifier", "..LocalDevice", "..RemoteDevice", "..ServiceRecord", "..ServiceRegistrationException", "..UUID", ".microedition.io.CommConnection", "...Connection",
      "...ConnectionNotFoundException", "...Connector", "...ContentConnection", "...Datagram", "...DatagramConnection", "...HttpConnection", "...HttpsConnection", "...InputConnection",
      "...OutputConnection", "...PushRegistry", "...SecureConnection", "...SecurtyInfo", "...ServerSocketConnection", "...SocketConnection", "...StreamConnection", "...StreamConnectionNotifier",
      "...UDPDatagramConnection", "..lcdui.game.GameCanvas", "....Layer", "....LayerManager", "....Sprite", "....TiledLayer", "...Alert", "...AlertType", "...Canvas", "...Choice", "...ChoiceGroup",
      "...Command", "...CommandListener", "...CustomItem", "...DateField", "...Display", "...Displayable", "...Font", "...Form", "...Gauge", "...Graphics", "...Image", "...ImageItem", "...Item",
      "...ItemCommandListener", "...ItemStateListener", "...List", "...Screen", "...Spacer", "...StringItem", "...TextBox", "...TextField", "...Ticker", "..location.AddressInfo", "...Coordinates",
      "...Criteria", "...Landmark", "...LandmarkException", "...LandmarkStore", "...Location", "...LocationException", "...LocationListener", "...LocationProvider", "...Orientation",
      "...ProximityListener", "...QualifiedCoordinates", "..m3g.AnimationController", "...AnimationTrack", "...Appearance", "...Background", "...Camera", "...CompositingMode", "...Fog",
      "...Graphics3D", "...Group", "...Image2D", "...IndexBuffer", "...KeyframeSequence", "...Light", "...Loader", "...Material", "...Mesh", "...MorphingMesh", "...Node", "...Object3D",
      "...PolygonMode", "...RayIntersection", "...SkinnedMesh", "...Sprite3D", "...Texture2D", "...Transform", "...Transformable", "...TriangleStripArray", "...VertexArray", "...VertexBuffer",
      "...World", "..media.control.FramePositioningControl", "....GUIControl", "....MetaDataControl", "....MIDIControl", "....PitchControl", "....RateControl", "....RecordControl",
      "....StopTimeControl", "....TempoControl", "....ToneControl", "....VideoControl", "....VolumeControl", "...Control", "...Controllable", "...Manager", "...MediaException", "...Player",
      "...PlayerListener", "...TimeBase", "..midlet.MIDlet", "...MIDletStateChangeException", "..pki.Certificate", "...CertificateException", "..rms.InvalidRecordIDException", "...RecordComparator",
      "...RecordEnumeration", "...RecordFilter", "...RecordListener", "...RecordStore", "...RecordStoreException", "...RecordStoreFullException", "...RecordStoreNotFoundException",
      "...RecordStoreNotOpenException", ".wireless.messaging.BinaryMessage", "...Message", "...MessageConnection", "...MessageListener", "...TextMessage"
  };
}
