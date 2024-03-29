/* kXML
 *
 * The contents of this file are subject to the Enhydra Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License
 * on the Enhydra web site ( http://www.enhydra.org/ ).
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific terms governing rights and limitations
 * under the License.
 *
 * The Initial Developer of kXML is Stefan Haustein. Copyright (C)
 * 2000, 2001 Stefan Haustein, D-46045 Oberhausen (Rhld.),
 * Germany. All Rights Reserved.
 *
 * Contributor(s): Paul Palaszewski, Wilhelm Fitzpatrick,
 *                 Eric Foster-Johnson, Hans-Harald Schulz
 *
 * */

package org.kxml.kdom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;
import org.kxml.Xml;
import org.kxml.XmlIO;
import org.kxml.io.AbstractXmlWriter;
import org.kxml.io.XmlWriter;
import org.kxml.parser.AbstractXmlParser;
import org.kxml.parser.ParseEvent;

/**
 * A common base class for Document and Element, also used for storing XML fragments.
 */

public class Node implements XmlIO {

  protected Vector children;
  protected StringBuffer types;

  /**
   * inserts the given child object of the given type at the given index.
   */

  public void addChild(final int index, final int type, final Object child) {

    if (child == null) { throw new NullPointerException(); }

    if (children == null) {
      children = new Vector();
      types = new StringBuffer();
    }

    if (type == Xml.ELEMENT) {
      if (!(child instanceof Element)) { throw new RuntimeException("Element obj expected)"); }

      ((Element) child).setParent(this);
    }
    else if (!(child instanceof String)) { throw new RuntimeException("String expected"); }

    children.insertElementAt(child, index);
    types.insert(index, (char) type);
  }

  /** convenience method for addChild (getChildCount (), child) */

  public void addChild(final int type, final Object child) {
    addChild(getChildCount(), type, child);
  }

  /**
   * Builds a default element with the given properties. Elements should always be created using this method instead of the constructor in order to enable construction of specialized subclasses by
   * deriving custom Document classes. Please note: For no namespace, please use Xml.NO_NAMESPACE, null is not a legal value. Currently, null is converted to Xml.NO_NAMESPACE, but future versions may
   * throw an exception.
   */

  public Element createElement(final String namespace, final String name) {

    final Element e = new Element();
    e.namespace = namespace == null ? Xml.NO_NAMESPACE : namespace;
    e.name = name;
    return e;
  }

  /**
   * Returns the child object at the given index. For child elements, an Element object is returned. For all other child types, a String is returned.
   */

  public Object getChild(final int index) {
    return children.elementAt(index);
  }

  /** Returns the number of child objects */

  public int getChildCount() {
    return children == null ? 0 : children.size();
  }

  /**
   * returns the element at the given index. If the node at the given index is a text node, null is returned
   */

  public Element getElement(final int index) {
    final Object child = getChild(index);
    return (child instanceof Element) ? (Element) child : null;
  }

  /** Convenience method for getElement (getNamespace (), name). */

  public Element getElement(final String name) {
    return getElement(getNamespace(), name);
  }

  /**
   * Returns the element with the given namespace and name. If the element is not found, or more than one matching elements are found, an exception is thrown.
   */

  public Element getElement(final String namespace, final String name) {

    final int i = indexOf(namespace, name, 0);
    final int j = indexOf(namespace, name, i + 1);

    if ((i == -1) || (j != -1)) { throw new RuntimeException("Element {" + namespace + "}" + name + (i == -1 ? " not found in " : " more than once in ") + getName()); }

    return getElement(i);
  }

  /** returns "#document-fragment". For elements, the element name is returned */

  public String getName() {
    return "#document-fragment";
  }

  /**
   * Returns the namespace of the current element. For Node and Document, Xml.NO_NAMESPACE is returned.
   */

  public String getNamespace() {
    return Xml.NO_NAMESPACE;
  }

  /**
   * returns the text content if the element has text-only content. Throws an exception for mixed content
   */

  public String getText() {

    final StringBuffer buf = new StringBuffer();
    final int len = getChildCount();

    for (int i = 0; i < len; i++) {
      if ((getType(i) & (Xml.TEXT | Xml.WHITESPACE)) != 0) {
        buf.append(getText(i));
      }
      else if (getType(i) == Xml.ELEMENT) { throw new RuntimeException("not text-only content!"); }
    }

    return buf.toString();
  }

  /**
   * Returns the text node with the given index or null if the node with the given index is not a text node.
   */

  public String getText(final int index) {
    return (getType(index) & (Xml.TEXT | Xml.WHITESPACE)) != 0 ? (String) getChild(index) : null;
  }

  /**
   * Returns the type of the child at the given index. Possible types are ELEMENT, TEXT, COMMENT, and PROCESSING_INSTRUCTION
   */

  public int getType(final int index) {
    return types.charAt(index);
  }

  /**
   * Convenience method for indexOf (getNamespace (), name, startIndex).
   */

  public int indexOf(final String name, final int startIndex) {
    return indexOf(getNamespace(), name, startIndex);
  }

  /**
   * Performs search for an element with the given namespace and name, starting at the given start index. A null namespace matches any namespace, please use Xml.NO_NAMESPACE for no namespace). returns
   * -1 if no matching element was found.
   */

  public int indexOf(final String namespace, final String name, final int startIndex) {

    final int len = getChildCount();

    for (int i = startIndex; i < len; i++) {

      final Element child = getElement(i);

      if ((child != null) && name.equals(child.getName()) && ((namespace == null) || namespace.equals(child.getNamespace()))) { return i; }
    }
    return -1;
  }

  /**
   * Recursively builds the child elements from the given parser until an end tag or end document is found. The end tag is not consumed.
   */

  public void parse(final AbstractXmlParser parser) throws IOException {

    boolean leave = false;

    do {
      final ParseEvent event = parser.peek();

      switch (event.getType()) {

        case Xml.START_TAG: {
          final Element child = createElement(event.getNamespace(), event.getName());
          child.setAttributes(event.getAttributes());
          addChild(Xml.ELEMENT, child);

          // order is important here since
          // setparent may perform some init code!

          child.parse(parser);
          break;
        }

        case Xml.END_DOCUMENT:
        case Xml.END_TAG:
          leave = true;
          break;

        default:
          addChild(event.getType(), event.getText());
          parser.read();
      }
    }
    while (!leave);
  }

  /** Removes the child object at the given index */

  public void removeChild(final int idx) {
    children.removeElementAt(idx);

    /** * Modification by HHS - start ** */
    // types.deleteCharAt (index);
    /***/
    final int n = types.length() - 1;

    for (int i = idx; i < n; i++) {
      types.setCharAt(i, types.charAt(i + 1));
    }

    types.setLength(n);

    /** * Modification by HHS - end ** */
  }

  /**
   * returns a valid XML representation of this Element including attributes and children.
   */

  public String toString() {
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      final XmlWriter xw = new XmlWriter(new OutputStreamWriter(bos));
      write(xw);
      xw.close();
      return new String(bos.toByteArray());
    }
    catch (final IOException e) {
      throw new RuntimeException(e.toString());
    }
  }

  /**
   * Writes this node to the given XmlWriter. For node and document, this method is identical to writeChildren, except that the stream is flushed automatically.
   */

  public void write(final AbstractXmlWriter writer) throws IOException {
    writeChildren(writer);
    writer.flush();
  }

  /** Writes the children of this node to the given XmlWriter. */

  public void writeChildren(final AbstractXmlWriter writer) throws IOException {
    if (children == null) { return; }

    final int len = children.size();

    for (int i = 0; i < len; i++) {
      final int type = getType(i);
      final Object child = children.elementAt(i);
      switch (type) {
        case Xml.ELEMENT:
          ((Element) child).write(writer);
          break;

        case Xml.TEXT:
        case Xml.WHITESPACE:
          writer.write((String) child);
          break;

        default:
          writer.writeLegacy(type, (String) child);
      }
    }
  }
}
