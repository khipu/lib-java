/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuEmailResponse;
import com.khipu.lib.java.response.KhipuInstantReverseResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Servicio para reversar pagos en khipu.
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2015-05-20
 */
public class KhipuInstantReverse extends KhipuService {

	private String _notificationId;

	public KhipuInstantReverse(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	public String getMethodEndpoint() {
		return "instantReverse";
	}

	public void setNotificationId(String subject) {
		_notificationId = subject;
	}

	private String getConcatenated() {
		return "receiver_id=" + getReceiverId() + "&notification_id=" + _notificationId;
	}

	@Override
	public KhipuInstantReverseResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("notification_id", _notificationId);
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			post(map);
			return new KhipuInstantReverseResponse();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException xmlException) {
			throw Khipu.getErrorsException(xmlException.getJSON());
		}
		return null;
	}
}
