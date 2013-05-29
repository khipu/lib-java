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
import com.khipu.lib.java.response.KhipuSetRejectedByPayerResponse;

/**
 * Servicio para que el cobrador indique que el pagador a rechazado el pago.
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.1
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

	KhipuSetRejectedByPayer(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "setRejectedByPayer";
	}

	@Override
	public KhipuSetRejectedByPayerResponse execute() throws KhipuException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("payment_id", "" + getPaymentId());
		map.put("text", "" + getText());
		map.put("hash", sha1(getConcatenated()));
		try {
			post(map);
			return new KhipuSetRejectedByPayerResponse();
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
		concatenated += "&text=" + (_text != null ? _text : "");
		concatenated += "&secret=" + getSecret();
		return concatenated;
	}

	public void setPaymentId(String paymentId) {
		_paymentId = paymentId;
	}
}
