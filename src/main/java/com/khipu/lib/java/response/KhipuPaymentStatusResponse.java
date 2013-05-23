/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.response;


public class KhipuPaymentStatusResponse  implements KhipuResponse {
	private String _status;
	private String _detail;

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public String getDetail() {
		return _detail;
	}

	public void setDetail(String detail) {
		_detail = detail;
	}
	@Override
	public String toString() {
		return "status: " + _status + ", detail: " + _detail;
	}
}
