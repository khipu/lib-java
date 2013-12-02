/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuEmailResponse;
import org.apache.http.ParseException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Servicio para injectar pagos en khipu. Estos pagos pueden ser opcionalmente
 * enviados por correo electrónico
 * 
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.2
 * @since 2013-05-24
 */
public class KhipuCreateEmail extends KhipuService {

    private String _subject;
    private String _body;
    private String _transactionId;
    private String _pictureUrl;
    private String _notifyUrl;
    private String _returnUrl;
    private String _cancelUrl;
    private boolean _payDirectly;
    private boolean _sendEmails;
    private String _custom;
    private Date _expiresDate;

    private List<Map<String, String>> _destinataries = new LinkedList<Map<String, String>>();

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
        concatenated.append("receiver_id=").append(getReceiverId());
        concatenated.append("&subject=").append(_subject);
        concatenated.append("&body=").append(_body != null ? _body : "");
        concatenated.append("&destinataries=").append(getDestinatariesJSON());
        concatenated.append("&pay_directly=").append(_payDirectly);
        concatenated.append("&send_emails=").append(_sendEmails);
        concatenated.append("&expires_date=").append(_expiresDate != null ? _expiresDate.getTime() : "");
        concatenated.append("&transaction_id=").append(_transactionId != null ? _transactionId : "");
        concatenated.append("&custom=").append(_custom != null ? _custom : "");
        concatenated.append("&notify_url=").append(_notifyUrl != null ? _notifyUrl : "");
        concatenated.append("&return_url=").append(_returnUrl != null ? _returnUrl : "");
        concatenated.append("&cancel_url=").append(_cancelUrl != null ? _cancelUrl : "");
        concatenated.append("&picture_url=").append(_pictureUrl != null ? _pictureUrl : "");
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
        map.put("pay_directly", "" + _payDirectly);
        map.put("send_emails", "" + _sendEmails);
        map.put("transaction_id", _transactionId != null ? _transactionId : "");
        map.put("custom", _custom != null ? _custom : "");
        map.put("notify_url", _notifyUrl != null ? _notifyUrl : "");
        map.put("return_url", _returnUrl != null ? _returnUrl : "");
        map.put("cancel_url", _cancelUrl != null ? _cancelUrl : "");
        map.put("picture_url", _pictureUrl != null ? _pictureUrl : "");
        map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
        try {
            Map<String, Object> info = new ObjectMapper().readValue(post(map), Map.class);
            List<Payment> payments = new LinkedList<Payment>();
            List<Map<String, String>> paymentsInfo = (List<Map<String, String>>) info.get("payments");
            for (Map<String, String> paymentInfo : paymentsInfo) {
                payments.add(new Payment(paymentInfo.get("id"), paymentInfo.get("email"), paymentInfo.get("url")));
            }
            return new KhipuEmailResponse((String) info.get("id"), payments);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException xmlException) {
            throw Khipu.getErrorsException(xmlException.getJSON());
        }
        return null;
	}

	public void setPictureUrl(String pictureUrl) {
		_pictureUrl = pictureUrl;
	}
}
