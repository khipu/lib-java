/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.response;

public class KhipuVerifyPaymentNotificationResponse implements KhipuResponse {

	private boolean _verified;

	public KhipuVerifyPaymentNotificationResponse(boolean verified) {
		_verified = verified;
	}

	public boolean isVerified() {
		return _verified;
	}

	public void setVerified(boolean verified) {
		_verified = verified;
	}

}
