/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuReceiverBanksResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Servicio para obtener el listado de bancos para pagar a un cobrador..
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2013-12-02
 */
public class KhipuReceiverBanks extends KhipuService {

	public KhipuReceiverBanks(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	String getMethodEndpoint() {
		return "receiverBanks";
	}

	@Override
	public KhipuReceiverBanksResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			Map<String, Object> info = new ObjectMapper().readValue(post(map), Map.class);
			List<Bank> banks = new LinkedList<Bank>();
			List<Map<String, Object>> banksInfo = (List<Map<String, Object>>) info.get("banks");
			for (Map<String, Object> bankInfo : banksInfo) {
				banks.add(new Bank((String) bankInfo.get("id"), (String) bankInfo.get("name"), (String) bankInfo.get("message"), (Integer) bankInfo.get("min-amount")));
			}
			return new KhipuReceiverBanksResponse(banks);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException xmlException) {
			throw Khipu.getErrorsException(xmlException.getJSON());
		}
		return null;
	}

	private String getConcatenated() {
		return "receiver_id=" + getReceiverId();
	}
}
