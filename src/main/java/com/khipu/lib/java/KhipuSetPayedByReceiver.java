/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuSetPayedByReceiverResponse;
import org.apache.http.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para que el cobrador modificar el estado de un pago a realizado.
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2013-05-24
 */
public class KhipuSetPayedByReceiver extends KhipuService {

	private String _paymentId;

	public String getPaymentId() {
		return _paymentId;
	}

	public KhipuSetPayedByReceiver(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "setPayedByReceiver";
	}

	@Override
	public KhipuSetPayedByReceiverResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("payment_id", "" + getPaymentId());
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			post(map);
			return new KhipuSetPayedByReceiverResponse();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException xmlException) {
			throw Khipu.getErrorsException(xmlException.getJSON());
		}
		return null;
	}

	private String getConcatenated() {
		String concatenated = "";
		concatenated += "receiver_id=" + getReceiverId();
		concatenated += "&payment_id=" + getPaymentId();
		return concatenated;
	}

	public void setPaymentId(String paymentId) {
		_paymentId = paymentId;
	}
}
