/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.response;

import java.util.List;

import com.khipu.lib.java.Payment;

public class KhipuEmailResponse implements KhipuResponse {
	private String _billId;
	private List<Payment> _payments;

	public KhipuEmailResponse(String id, List<Payment> payments) {
		_billId = id;
		_payments = payments;
	}

	public String getBillId() {
		return _billId;
	}

	public void setBillId(String billId) {
		_billId = billId;
	}

	public List<Payment> getPayments() {
		return _payments;
	}

	public void setPayments(List<Payment> payments) {
		_payments = payments;
	}
	
}
