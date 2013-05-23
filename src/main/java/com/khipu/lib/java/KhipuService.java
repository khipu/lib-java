/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.exception.XMLException;
import com.khipu.lib.java.response.KhipuResponse;

public abstract class KhipuService {

	private long _receiverId;
	private String _secret;

	public KhipuService(long receiverId, String secret) {
		_receiverId = receiverId;
		_secret = secret;
	}

	public abstract String getMethodEndpoint();

	public abstract KhipuResponse execute() throws KhipuException, IOException;

	public long getReceiverId() {
		return _receiverId;
	}

	public String getSecret() {
		return _secret;
	}

	public void setSecret(String secret) {
		_secret = secret;
	}

	public static String sha1(String string) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	protected String post(Map<String, String> data) throws ParseException, XMLException {

		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpResponse response;
		try {
			HttpPost httpost = new HttpPost(Khipu.API_URL + getMethodEndpoint());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String key : data.keySet()) {
				nvps.add(new BasicNameValuePair(key, data.get(key)));
			}
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
			throw new XMLException(EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
