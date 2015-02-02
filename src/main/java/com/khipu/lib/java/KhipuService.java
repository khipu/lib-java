/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuResponse;
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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class KhipuService {

	private long _receiverId;
	private String _secret;

	public KhipuService(long receiverId, String secret) {
		_receiverId = receiverId;
		_secret = secret;
	}

	abstract String getMethodEndpoint();

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


	protected String post(Map<String, Object> data) throws ParseException, JSONException {

		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpResponse response;
		try {
			HttpPost httpost = new HttpPost(Khipu.API_URL + getMethodEndpoint());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String key : data.keySet()) {
				nvps.add(new BasicNameValuePair(key, (String) data.get(key)));
			}
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
			throw new JSONException(EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String HmacSHA256(String secret, String data) {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKeySpec);
			byte[] digest = mac.doFinal(data.getBytes("UTF-8"));
			return byteArrayToString(digest);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key exception while converting to HMac SHA256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algorithm not supported");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding not supported");
		}
	}

	private static String byteArrayToString(byte[] data) {
		BigInteger bigInteger = new BigInteger(1, data);
		String hash = bigInteger.toString(16);
		while (hash.length() < 64) {
			hash = "0" + hash;
		}
		return hash;
	}
}
