/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.exception.XMLException;
import com.khipu.lib.java.response.KhipuPaymentStatusResponse;

public class KhipuPaymentStatus extends KhipuService {
	private String _id;
	private String _paymentId;

	public KhipuPaymentStatus(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	public String getMethodEndpoint() {
		return "paymentStatus";
	}

	@Override
	public KhipuPaymentStatusResponse execute() throws KhipuException, IOException {

		Map<String, String> map = new HashMap<String, String>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("payment_id", _paymentId);
		map.put("hash", sha1(getConcatenated()));
		try {
			return new ObjectMapper().readValue(post(map), KhipuPaymentStatusResponse.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (XMLException xmlException) {
			throw Khipu.getErrorsException(xmlException.getXml());
		}
		return null;
	}

	private String getConcatenated() {
		StringBuilder concatenated = new StringBuilder();
		concatenated.append("receiver_id=" + getReceiverId());
		concatenated.append("&payment_id=" + _paymentId);
		concatenated.append("&secret=" + getSecret());
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
