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

/**
 * Esta clase sirve para generar instancias de servicios de Khipu
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.1
 * @since 2013-05-24
 */
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

	/**
	 * Entrega un servicio para injectar pagos por email en Khipu.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @return el servicio para inyectar pagos
	 * @see KhipuCreateEmail
	 * @since 2013-05-24
	 */
	public static KhipuCreateEmail getCreateEmail(long receiverId, String secret) {
		return new KhipuCreateEmail(receiverId, secret);
	}

	/**
	 * Entrega un servicio para verificar el estado de un pago.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @return el servicio para verificar estados
	 * @see KhipuPaymentStatus
	 * @since 2013-05-24
	 */
	public static KhipuPaymentStatus getPaymentStatus(int receiverId, String secret) {
		return new KhipuPaymentStatus(receiverId, secret);
	}

	/**
	 * Entrega un servicio para verificar el estado de un cobrador.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @return el servicio para verificar el estado
	 * @see KhipuReceiverStatus
	 * @since 2013-05-24
	 */
	public static KhipuReceiverStatus getReceiverStatus(int receiverId, String secret) {
		return new KhipuReceiverStatus(receiverId, secret);
	}

	/**
	 * Entrega un servicio para expirar cobros.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @return el servicio para expirar cobros
	 * @see KhipuSetBillExpired
	 * @since 2013-05-24
	 */
	public static KhipuSetBillExpired getSetBillExpired(int receiverId, String secret) {
		return new KhipuSetBillExpired(receiverId, secret);
	}

	/**
	 * Entrega un servicio para indicar que el pago fue hecho al cobrador por un
	 * medio distinto a khipu.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @return el servicio para modificar el estado de un pago
	 * @see KhipuSetPayedByReceiver
	 * @since 2013-05-24
	 */
	public static KhipuSetPayedByReceiver getSetPayedByReceiver(int receiverId, String secret) {
		return new KhipuSetPayedByReceiver(receiverId, secret);
	}

	/**
	 * Entrega un servicio para indicar que el pago rechazado por el pagador.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @return el servicio para modificar el estado de un pago
	 * @see KhipuSetRejectedByPayer
	 * @since 2013-05-24
	 */
	public static KhipuSetRejectedByPayer getSetRejectedByPayer(int receiverId, String secret) {
		return new KhipuSetRejectedByPayer(receiverId, secret);
	}

	static KhipuException getErrorsException(String xml) {
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

	/**
	 * Entrega un String que contiene un botón de pago que dirije a khipu.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @param secret
	 *            llave de cobrador
	 * @param email
	 *            correo del pagador. Este correo aparecerá pre-configurado en
	 *            la página de pago (opcional).
	 * @param subject
	 *            asunto del cobro. Con un máximo de 255 caracteres.
	 * @param body
	 *            la descripción del cobro (opcional).
	 * @param amount
	 *            el monto del cobro.
	 * @param notifyUrl
	 *            la dirección de tu web service que utilizará khipu para
	 *            notificarte de un pago realizado (opcional).
	 * @param returnUrl
	 *            la dirección URL a donde enviar al cliente una vez que el pago
	 *            sea realizado (opcional).
	 * @param cancelUrl
	 *            la dirección URL a donde enviar al cliente si se arrepiente de
	 *            hacer la transacción (opcional)
	 * @param pictureUrl
	 *            una dirección URL de una foto de tu producto o servicio para
	 *            mostrar en la página del cobro (opcional).
	 * @param custom
	 *            variable para enviar información personalizada de la
	 *            transacción (opcional).
	 * @param transactionId
	 *            variable disponible para enviar un identificador propio de la
	 *            transacción (opcional).
	 * @param button
	 *            imagen del botón de pago.
	 * @return el servicio para crear botones de pago
	 * @since 2013-05-24
	 */
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

	/**
	 * Entrega un servicio para verificar la autenticidad de una notificación
	 * instantanea de khipu.
	 * 
	 * @param receiverId
	 *            id de cobrador
	 * @return el servicio verificar la autenticidad de un pago.
	 * @see KhipuVerifyPaymentNotification
	 * @since 2013-05-24
	 */
	public static KhipuVerifyPaymentNotification getVerifyPaymentNotification(int receiverId) {
		return new KhipuVerifyPaymentNotification(receiverId);
	}
}
