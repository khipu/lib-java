/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuReceiverStatusResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para verificar el estado de una cuenta de cobro.
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2013-05-24
 */
public class KhipuReceiverStatus extends KhipuService {

	KhipuReceiverStatus(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "receiverStatus";
	}

	@Override
	public KhipuReceiverStatusResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			Map<String, Object> status = new ObjectMapper().readValue(post(map), Map.class);
			return new KhipuReceiverStatusResponse((Boolean) status.get("ready_to_collect"), (String) status.get("type"));
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
		return "receiver_id=" + getReceiverId();
	}
}
