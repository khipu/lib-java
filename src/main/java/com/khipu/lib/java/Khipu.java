/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.khipu.lib.java.exception.KhipuException;

public class Khipu {
	private static final String AMAZON_IMAGES_URL = "https://s3.amazonaws.com/static.khipu.com/buttons/";
	public static String CREATE_PAYMENT_PAGE_ENDPOINT = "createPaymentPage";
	public static final String API_URL = "https://khipu.com/api/1.1/";
	public static String BUTTON_50x25 = "50x25.png";
	public static String BUTTON_100x25 = "100x25.png";
	public static String BUTTON_100x50 = "100x50.png";
	public static String BUTTON_150x25 = "150x25.png";
	public static String BUTTON_150x50 = "150x50.png";
	public static String BUTTON_150x75 = "150x75.png";
	public static String BUTTON_150x75_B = "150x75-B.png";
	public static String BUTTON_200x50 = "200x50.png";
	public static String BUTTON_200x75 = "200x75.png";

	public static KhipuCreateEmail getCreateEmail(long receiverId, String secret) {
		return new KhipuCreateEmail(receiverId, secret);
	}

	public static KhipuPaymentStatus getPaymentStatus(int receiverId, String secret) {
		return new KhipuPaymentStatus(receiverId, secret);
	}

	public static KhipuReceiverStatus getReceiverStatus(int receiverId, String secret) {
		return new KhipuReceiverStatus(receiverId, secret);
	}

	public static KhipuSetBillExpired getSetBillExpired(int receiverId, String secret) {
		return new KhipuSetBillExpired(receiverId, secret);
	}

	public static KhipuSetPayedByReceiver getSetPayedByReceiver(int receiverId, String secret) {
		return new KhipuSetPayedByReceiver(receiverId, secret);
	}

	public static KhipuSetRejectedByPayer getSetRejectedByPayer(int receiverId, String secret) {
		return new KhipuSetRejectedByPayer(receiverId, secret);
	}

	public static KhipuException getErrorsException(String xml) {
		KhipuException exception = new KhipuException();
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xml));
			Document document = documentBuilder.parse(inputSource);
			document.getDocumentElement().normalize();
			NodeList errors = document.getElementsByTagName("Error");
			for (int temp = 0; temp < errors.getLength(); temp++) {
				exception.addError(errors.item(temp).getTextContent());
			}
		} catch (ParserConfigurationException parserConfigurationException) {
			parserConfigurationException.printStackTrace();
		} catch (SAXException saxException) {
			saxException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return exception;
	}

	public static String getPaymentButton(int receiverId, String secret, String email, String subject, String body, double amount, String notifyUrl, String returnUrl, String cancelUrl, String pictureUrl, String custom, String transactionId, String button) {
		StringBuilder builder = new StringBuilder();
		builder.append("<form action=\"" + API_URL + CREATE_PAYMENT_PAGE_ENDPOINT + "\" method=\"post\">\n");
		builder.append("<input type=\"hidden\" name=\"receiver_id\" value=\"" + receiverId + "\">\n");
		builder.append("<input type=\"hidden\" name=\"subject\" value=\"" + (subject != null ? subject : "") + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"body\" value=\"" + (body != null ? body : "") + "\">\n");
		builder.append("<input type=\"hidden\" name=\"amount\" value=\"" + amount + "\">\n");
		builder.append("<input type=\"hidden\" name=\"notify_url\" value=\"" + (notifyUrl != null ? notifyUrl : "") + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"return_url\" value=\"" + (returnUrl != null ? returnUrl : "") + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"cancel_url\" value=\"" + (cancelUrl != null ? cancelUrl : "") + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"custom\" value=\"" + (custom != null ? custom : "") + "\">\n");
		builder.append("<input type=\"hidden\" name=\"transaction_id\" value=\"" + (transactionId != null ? transactionId : "") + "\">\n");
		builder.append("<input type=\"hidden\" name=\"payer_email\" value=\"" + (email != null ? email : "") + "\">\n");
		builder.append("<input type=\"hidden\" name=\"picture_url\" value=\"" + (pictureUrl != null ? pictureUrl : "") + "\">\n");
		builder.append("<input type=\"hidden\" name=\"hash\" value=\"" + KhipuService.sha1(getConcatenated(receiverId, secret, email, subject, body, amount, notifyUrl, returnUrl, cancelUrl, pictureUrl, custom, transactionId)) + "\">\n");
		builder.append("<input type=\"image\" name=\"submit\" src=\"" + AMAZON_IMAGES_URL + button + "\"/>");
		builder.append("</form>");
		return builder.toString();
	}

	private static String getConcatenated(int receiverId, String secret, String email, String subject, String body, double amount, String notifyUrl, String returnUrl, String cancelUrl, String pictureUrl, String custom, String transactionId) {
		StringBuilder builder = new StringBuilder();
		builder.append("receiver_id=" + receiverId);
		builder.append("&subject=" + (subject != null ? subject : ""));
		builder.append("&body=" + (body != null ? body : ""));
		builder.append("&amount=" + amount);
		builder.append("&payer_email=" + (email != null ? email : ""));
		builder.append("&transaction_id=" + (transactionId != null ? transactionId : ""));
		builder.append("&custom=" + (custom != null ? custom : ""));
		builder.append("&notify_url=" + (notifyUrl != null ? notifyUrl : ""));
		builder.append("&return_url=" + (returnUrl != null ? returnUrl : ""));
		builder.append("&cancel_url=" + (cancelUrl != null ? cancelUrl : ""));
		builder.append("&picture_url=" + (pictureUrl != null ? pictureUrl : ""));
		builder.append("&secret=" + secret);
		return builder.toString();
	}

	public static KhipuVerifyPaymentNotification getVerifyPaymentNotification(int receiverId) {
		return new KhipuVerifyPaymentNotification(receiverId);
	}
}
