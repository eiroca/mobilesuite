/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 */
package net.eiroca.log4j2me;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * @author Witmate
 */
public class StringTokenizer {

  protected boolean m_boolReturnToken = false;
  protected int m_nCurrentPoint = 0;
  protected String m_strParsed = "";
  protected Vector m_vTokenizer = new Vector();

  public StringTokenizer() {
    //
  }

  public StringTokenizer(final String strParsed, final boolean boolReturnToken) {
    if (strParsed != null) {
      m_strParsed = strParsed;
    }
    else {
      m_strParsed = "";
    }
    m_boolReturnToken = boolReturnToken;
  }

  public StringTokenizer(final String strParsed, final String strAToken) {
    if (null != strParsed) {
      m_strParsed = strParsed;
    }
    else {
      m_strParsed = "";
    }
    if (null != strAToken) {
      m_vTokenizer.addElement(strAToken);
    }
  }

  public int addTokenizer(final String strAToken) {
    m_vTokenizer.addElement(strAToken);
    return m_vTokenizer.size();
  }

  public boolean hasMoreTokens() {
    return (m_nCurrentPoint < ((null != m_strParsed) ? m_strParsed.length() : 0));
  }

  public String nextToken() throws NoSuchElementException {
    if (!hasMoreTokens()) { throw new NoSuchElementException("There are not more token."); }
    String strRtn = m_strParsed.substring(m_nCurrentPoint);
    String strToken = "";
    String strTookToken = "";
    int nPnt = Integer.MAX_VALUE;
    int nTemp;
    for (int i = 0; m_vTokenizer.size() > i; i++) {
      strToken = (String) m_vTokenizer.elementAt(i);
      nTemp = m_strParsed.indexOf(strToken, m_nCurrentPoint);
      if ((nPnt > nTemp) && (-1 < nTemp)) {
        nPnt = nTemp;
        strTookToken = strToken;
        strRtn = m_strParsed.substring(m_nCurrentPoint, nPnt);
      }
    }
    if (nPnt < Integer.MAX_VALUE) {
      m_nCurrentPoint = nPnt + strTookToken.length();
    }
    else {
      m_nCurrentPoint = m_strParsed.length();
    }
    if (m_boolReturnToken && (nPnt < Integer.MAX_VALUE)) {
      strRtn += strTookToken;
    }
    return strRtn;
  }

}