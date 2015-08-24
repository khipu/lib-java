/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuSetRejectedByPayerResponse;
import org.apache.http.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para que el cobrador indique que el pagador a rechazado el pago.
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2013-05-24
 */
public class KhipuSetRejectedByPayer extends KhipuService {

	private String _paymentId;
	private String _text;

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	public String getPaymentId() {
		return _paymentId;
	}

	public KhipuSetRejectedByPayer(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "setRejectedByPayer";
	}

	@Override
	public KhipuSetRejectedByPayerResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("payment_id", "" + getPaymentId());
		map.put("text", "" + getText());
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			post(map);
			return new KhipuSetRejectedByPayerResponse();
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
		concatenated += "&text=" + (_text != null ? _text : "");
		return concatenated;
	}

	public void setPaymentId(String paymentId) {
		_paymentId = paymentId;
	}
}
