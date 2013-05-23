/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

public class Payment {

	private String _email;

	private String _id;

	private String _link;

	public Payment(String email, String paymentId, String link) {
		_id = paymentId;
		_email = email;
		_link = link;
	}

	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getLink() {
		return _link;
	}

	public void setLink(String link) {
		_link = link;
	}
}
