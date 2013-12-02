/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuPaymentStatusResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para verificar el estado de un pago en khipu.
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.2
 * @since 2013-05-24
 */
public class KhipuPaymentStatus extends KhipuService {
	private String _id;
	private String _paymentId;

	KhipuPaymentStatus(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "paymentStatus";
	}

	@Override
	public KhipuPaymentStatusResponse execute() throws KhipuException, IOException {

		Map<String, String> map = new HashMap<String, String>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("payment_id", _paymentId);
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			return new ObjectMapper().readValue(post(map), KhipuPaymentStatusResponse.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			throw Khipu.getErrorsException(e.getJSON());
		}
		return null;
	}

	private String getConcatenated() {
		StringBuilder concatenated = new StringBuilder();
		concatenated.append("receiver_id=" + getReceiverId());
		concatenated.append("&payment_id=" + _paymentId);
		return concatenated.toString();
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getPaymentId() {
		return _paymentId;
	}

	public void setPaymentId(String paymentId) {
		_paymentId = paymentId;
	}

}
