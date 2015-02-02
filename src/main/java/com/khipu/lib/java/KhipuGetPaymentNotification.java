/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuGetPaymentNotificationResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio obtener la información de notificación instantanea hecha por khipu.
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2013-05-24
 */
public class KhipuGetPaymentNotification extends KhipuService {

	private String _notificationToken;

	KhipuGetPaymentNotification(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "getPaymentNotification";
	}

	private String getConcatenated() {
		StringBuilder concatenated = new StringBuilder();
		concatenated.append("receiver_id=").append(getReceiverId());
		concatenated.append("&notification_token=").append(_notificationToken);
		return concatenated.toString();
	}

	@Override
	public KhipuGetPaymentNotificationResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("notification_token", _notificationToken);
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			Map<String, Object> info = new ObjectMapper().readValue(post(map), Map.class);
			String notification_token = (String) info.get("notification_token");
			String receiver_id = "" + info.get("receiver_id");
			String subject = (String) info.get("subject");
			String amount = (String) info.get("amount");
			String custom = (String) info.get("custom");
			String transaction_id = (String) info.get("transaction_id");
			String payment_id = (String) info.get("payment_id");
			String currency = (String) info.get("currency");
			String payer_email = (String) info.get("payer_email");
			return new KhipuGetPaymentNotificationResponse(
					notification_token
					, receiver_id
					, subject
					, amount
					, custom
					, transaction_id
					, payment_id
					, currency
					, payer_email);
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

	public void setNotificationToken(String notificationToken) {
		_notificationToken = notificationToken;
	}

	public String getNotificationToken() {
		return _notificationToken;
	}

}
