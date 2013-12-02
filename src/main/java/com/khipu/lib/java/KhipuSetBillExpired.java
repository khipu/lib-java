/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuSetBillExpiredResponse;
import org.apache.http.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para expirar un cobro.
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.2
 * @since 2013-05-24
 */
public class KhipuSetBillExpired extends KhipuService {

	private String _text;
	private String _billId;

	KhipuSetBillExpired(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "setBillExpired";
	}

	@Override
	public KhipuSetBillExpiredResponse execute() throws KhipuException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("bill_id", _billId);
		map.put("text", _text);
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			post(map);
			return new KhipuSetBillExpiredResponse();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException xmlException) {
			throw Khipu.getErrorsException(xmlException.getJSON());
		}
		return null;
	}

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	public String getBillId() {
		return _billId;
	}

	public void setBillId(String billId) {
		_billId = billId;
	}

	private String getConcatenated() {
		String concatenated = "";
		concatenated += "receiver_id=" + getReceiverId();
		concatenated += "&bill_id=" + _billId;
		concatenated += "&text=" + (_text != null ? _text : "");
		return concatenated;
	}
}
