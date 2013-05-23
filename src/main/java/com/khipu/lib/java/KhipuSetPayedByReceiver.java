/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ParseException;

import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.exception.XMLException;
import com.khipu.lib.java.response.KhipuSetPayedByReceiverResponse;

public class KhipuSetPayedByReceiver extends KhipuService {

	private String _paymentId;

	public String getPaymentId() {
		return _paymentId;
	}

	public KhipuSetPayedByReceiver(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	public String getMethodEndpoint() {
		return "setPayedByReceiver";
	}

	@Override
	public KhipuSetPayedByReceiverResponse execute() throws KhipuException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("payment_id", "" + getPaymentId());
		map.put("hash", sha1(getConcatenated()));
		try {
			post(map);
			return new KhipuSetPayedByReceiverResponse();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (XMLException xmlException) {
			throw Khipu.getErrorsException(xmlException.getXml());
		}
		return null;
	}

	private String getConcatenated() {
		String concatenated = "";
		concatenated += "receiver_id=" + getReceiverId();
		concatenated += "&payment_id=" + getPaymentId();
		concatenated += "&secret=" + getSecret();
		return concatenated;
	}

	public void setPaymentId(String paymentId) {
		_paymentId = paymentId;
	}
}
