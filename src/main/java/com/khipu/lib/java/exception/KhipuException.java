/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.exception;

import java.util.LinkedList;
import java.util.List;

/**
 * Excepción que se arroja en caso error en alguna petición a khipu
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.1
 * @since 2013-05-24
 */
public class KhipuException extends Exception {

	private static final long serialVersionUID = 1L;
	private List<String> _errors = new LinkedList<String>();

	public List<String> getErrors() {
		return _errors;
	}

	public void addError(String error) {
		_errors.add(error);
	}

}
