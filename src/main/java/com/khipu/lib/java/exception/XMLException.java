/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.exception;

/**
 * Excepcion que contiene un XML de error proveniente de khipu
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.1
 * @since 2013-05-24
 */
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
