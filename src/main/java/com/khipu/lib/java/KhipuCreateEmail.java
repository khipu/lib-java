/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.exception.XMLException;
import com.khipu.lib.java.response.KhipuEmailResponse;

public class KhipuCreateEmail extends KhipuService {

	String _subject;
	String _body;
	String _transactionId;
	String _pictureUrl;
	String _notifyUrl;
	String _returnUrl;
	String _cancelUrl;
	boolean _payDirectly;
	boolean _sendEmails;
	String _custom;
	Date _expiresDate;

	List<Map<String, String>> _destinataries = new LinkedList<Map<String, String>>();

	public KhipuCreateEmail(long receiverId, String secret) {
		super(receiverId, secret);
	}

	@Override
	public String getMethodEndpoint() {
		return "createEmail";
	}

	public void setSubject(String subject) {
		_subject = subject;
	}

	public void setBody(String body) {
		_body = body;
	}

	public void setTransactionId(String transactionId) {
		_transactionId = transactionId;
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

	public void setPayDirectly(boolean payDirectly) {
		_payDirectly = payDirectly;
	}

	public void setSendEmails(boolean sendEmails) {
		_sendEmails = sendEmails;
	}

	public void setExpiresDate(Date expiresDate) {
		_expiresDate = expiresDate;
	}

	public void setCustom(String custom) {
		_custom = custom;
	}

	public void setDestinataries(List<Map<String, String>> destinataries) {
		_destinataries = destinataries;
	}

	private String getConcatenated() {
		StringBuilder concatenated = new StringBuilder();
		concatenated .append("receiver_id=" + getReceiverId());
		concatenated .append("&subject=" + _subject);
		concatenated .append("&body=" + (_body != null ? _body : ""));
		concatenated .append("&destinataries=" + getDestinatariesJSON());
		concatenated .append("&pay_directly=" + _payDirectly);
		concatenated .append("&send_emails=" + _sendEmails);
		concatenated .append("&expires_date=" + (_expiresDate != null ? _expiresDate.getTime() : ""));
		concatenated .append("&transaction_id=" + (_transactionId != null ? _transactionId : ""));
		concatenated .append("&custom=" + (_custom != null ? _custom : ""));
		concatenated .append("&notify_url=" + (_notifyUrl != null ? _notifyUrl : ""));
		concatenated .append("&return_url=" + (_returnUrl != null ? _returnUrl : ""));
		concatenated .append("&cancel_url=" + (_cancelUrl != null ? _cancelUrl : ""));
		concatenated .append("&picture_url=" + (_pictureUrl != null ? _pictureUrl : ""));
		concatenated .append("&secret=" + getSecret());
		return concatenated.toString();
	}

	public void addDestinatary(String name, String email, double amount) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("email", email);
		map.put("amount", "" + amount);
		_destinataries.add(map);
	}

	private String getDestinatariesJSON() {
		StringWriter writer = new StringWriter();
		try {
			new ObjectMapper().writeValue(writer, _destinataries);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	@Override
	public KhipuEmailResponse execute() throws KhipuException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("receiver_id", "" + getReceiverId());
		map.put("subject", _subject);
		map.put("body", _body != null ? _body : "");
		map.put("destinataries", getDestinatariesJSON());
		map.put("expires_date", _expiresDate != null ? "" + _expiresDate.getTime() : "");
		map.put("hash", sha1(getConcatenated()));
		map.put("pay_directly", "" + _payDirectly);
		map.put("send_emails", "" + _sendEmails);
		map.put("transaction_id", _transactionId != null ? _transactionId : "");
		map.put("custom", _custom != null ? _custom : "");
		map.put("notify_url", _notifyUrl != null ? _notifyUrl : "");
		map.put("return_url", _returnUrl != null ? _returnUrl : "");
		map.put("cancel_url", _cancelUrl != null ? _cancelUrl : "");
		map.put("picture_url", _pictureUrl != null ? _pictureUrl : "");
		try {
			Map<String, Object> info = new ObjectMapper().readValue(post(map), Map.class);
			List<Payment> payments = new LinkedList<Payment>();
			Map<String, String> links = (Map<String, String>) info.get("links");
			for (String email : links.keySet()) {
				payments.add(new Payment(email, getPaymentId(links.get(email)), links.get(email)));
			}
			return new KhipuEmailResponse((String) info.get("id"), payments);
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

	private String getPaymentId(String string) {
		if (string == null || !string.contains("/")) {
			return string;
		}
		return string.substring(string.lastIndexOf("/") + 1);
	}

	public void setPictureUrl(String pictureUrl) {
		_pictureUrl = pictureUrl;
	}
}
