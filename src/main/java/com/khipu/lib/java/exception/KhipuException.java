/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.exception;

import java.util.LinkedList;
import java.util.List;

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
