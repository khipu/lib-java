/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuUrlResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KhipuCreatePaymentURL extends KhipuService {


	private String _subject;
	private String _body;
	private String _email;
	private Date _expiresDate;
	private String _bankId;
	private String _transactionId;
	private String _pictureUrl;
	private String _notifyUrl;
	private String _returnUrl;
	private String _cancelUrl;
	private String _custom;
	private int _amount;

	public KhipuCreatePaymentURL(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	public String getMethodEndpoint() {
		return "createPaymentURL";
	}

	public void setSubject(String subject) {
		_subject = subject;
	}

	public void setBody(String body) {
		_body = body;
	}

	public void setEmail(String email) {
		_email = email;
	}

	public void setBankId(String bankId) {
		_bankId = bankId;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}
	public void setExpiresDate(Date expiresDate) {
		_expiresDate = expiresDate;
	}

	public void setTransactionId(String transactionId) {
		_transactionId = transactionId;
	}

	public void setPictureUrl(String pictureUrl) {
		_pictureUrl = pictureUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		_notifyUrl = notifyUrl;
	}

	public void setReturnUrl(String returnUrl) {
		_returnUrl = returnUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		_cancelUrl = cancelUrl;
	}

	public void setCustom(String custom) {
		_custom = custom;
	}

	private String getConcatenated() {
		StringBuilder concatenated = new StringBuilder();
		concatenated.append("receiver_id=").append(getReceiverId());
		concatenated.append("&subject=").append(_subject);
		concatenated.append("&body=").append(_body != null ? _body : "");
		concatenated.append("&amount=").append(_amount);
		concatenated.append("&payer_email=").append(_email);
		concatenated.append("&bank_id=").append(_bankId != null ? _bankId : "");
		concatenated.append("&expires_date=").append(_expiresDate != null ? _expiresDate.getTime() / 1000 : "");
		concatenated.append("&transaction_id=").append(_transactionId != null ? _transactionId : "");
		concatenated.append("&custom=").append(_custom != null ? _custom : "");
		concatenated.append("&notify_url=").append(_notifyUrl != null ? _notifyUrl : "");
		concatenated.append("&return_url=").append(_returnUrl != null ? _returnUrl : "");
		concatenated.append("&cancel_url=").append(_cancelUrl != null ? _cancelUrl : "");
		concatenated.append("&picture_url=").append(_pictureUrl != null ? _pictureUrl : "");
		return concatenated.toString();
	}

	@Override
	public KhipuUrlResponse execute() throws KhipuException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("subject", _subject);
		map.put("body", _body != null ? _body : "");
		map.put("amount", "" + _amount);
		map.put("payer_email", "" + _email);
		map.put("expires_date", "" + (_expiresDate != null ? _expiresDate.getTime() / 1000 : ""));
		map.put("bank_id", _bankId != null ? _bankId : "");
		map.put("transaction_id", _transactionId != null ? _transactionId : "");
		map.put("custom", _custom != null ? _custom : "");
		map.put("notify_url", _notifyUrl != null ? _notifyUrl : "");
		map.put("return_url", _returnUrl != null ? _returnUrl : "");
		map.put("cancel_url", _cancelUrl != null ? _cancelUrl : "");
		map.put("picture_url", _pictureUrl != null ? _pictureUrl : "");
		map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
		try {
			Map<String, String> info = new ObjectMapper().readValue(post(map), Map.class);
			return new KhipuUrlResponse(info.get("id"), info.get("url"), info.get("mobile-url"));
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
}
