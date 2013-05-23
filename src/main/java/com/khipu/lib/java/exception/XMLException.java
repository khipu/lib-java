/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.exception;

public class XMLException extends Exception {
	private static final long serialVersionUID = 1L;
	private String _xml;

	public XMLException(String xml) {
		_xml = xml;
	}

	public String getXml() {
		return _xml;
	}

	public void setXml(String xml) {
		_xml = xml;
	}

}
