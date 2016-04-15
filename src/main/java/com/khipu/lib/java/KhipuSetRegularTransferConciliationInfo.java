package com.khipu.lib.java;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.khipu.lib.java.exception.JSONException;
import com.khipu.lib.java.exception.KhipuException;
import com.khipu.lib.java.response.KhipuSetRegularTransferConciliationInfoResponse;

/**
 * Servicio para enviar a conciliar un pago con transferencia normal y obtener datos para la transferencia.
 * @author Eli Lopez (eli.lopez@openpay.mx)
 * @version 1.3
 * @since 2016-04-15
 */
public class KhipuSetRegularTransferConciliationInfo extends KhipuService {

    private String _paymentId;

    private String _bankId;

    private String _payerEmail;

    private String _payerPersonalIdentifier;

    public KhipuSetRegularTransferConciliationInfo(long receiverId, String secret) {
        super(receiverId, secret);
    }

    @Override
    String getMethodEndpoint() {
        return "setRegularTransferConciliationInfo";
    }

    @Override
    public KhipuSetRegularTransferConciliationInfoResponse execute() throws KhipuException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("receiver_id", "" + getReceiverId());
        map.put("payment_id", getPaymentId());
        map.put("bank_id", _bankId != null ? _bankId : "");
        map.put("payer_email", _payerEmail != null ? _payerEmail : "");
        map.put("payer_personal_identifier", _payerPersonalIdentifier != null ? _payerPersonalIdentifier : "");
        map.put("hash", HmacSHA256(getSecret(), getConcatenated()));
        try {
            Map<String, Object> info = new ObjectMapper().readValue(post(map), Map.class);
            System.out.println(info);
            KhipuSetRegularTransferConciliationInfoResponse response = new KhipuSetRegularTransferConciliationInfoResponse();
            response.setPaymentId((String) info.get("payment-id"));
            response.setCurrencyCode((String) info.get("currency-code"));
            response.setAmount((Double) info.get("amount"));
            response.setCountryCode((String) info.get("country-code"));
            response.setDestinationBankName((String) info.get("destination-bank-name"));
            response.setDestinationBankUrl((String) info.get("destination-bank-url"));
            response.setAccountIdentifier((String) info.get("account-identifier"));
            response.setAccountType((String) info.get("account-type"));
            response.setAccountHolderPersonalIdentifier((String) info.get("account-holder-personal-identifier"));
            response.setAccountHolderPersonalName((String) info.get("account-holder-personal-name"));
            response.setAccountHolderEmail((String) info.get("account-holder-email"));
            Integer deadline = (Integer) info.get("deadline");
            response.setDeadline(deadline == null ? null : new Date(deadline * 1000L));
            return response;
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

    private String getConcatenated() {
        StringBuilder concatenated = new StringBuilder();
        concatenated.append("receiver_id=").append(getReceiverId());
        concatenated.append("&payment_id=").append(_paymentId);
        concatenated.append("&bank_id=").append(_bankId != null ? _bankId : "");
        concatenated.append("&payer_email=").append(_payerEmail != null ? _payerEmail : "");
        concatenated.append("&payer_personal_identifier=")
                .append(_payerPersonalIdentifier != null ? _payerPersonalIdentifier : "");
        return concatenated.toString();
    }

    public String getPaymentId() {
        return this._paymentId;
    }

    public void setPaymentId(String _paymentId) {
        this._paymentId = _paymentId;
    }

    public String getBankId() {
        return this._bankId;
    }

    public void setBankId(String _bankId) {
        this._bankId = _bankId;
    }

    public String getPayerEmail() {
        return this._payerEmail;
    }

    public void setPayerEmail(String _payerEmail) {
        this._payerEmail = _payerEmail;
    }

    public String getPayerPersonalIdentifier() {
        return this._payerPersonalIdentifier;
    }

    public void setPayerPersonalIdentifier(String _payerPersonalIdentifier) {
        this._payerPersonalIdentifier = _payerPersonalIdentifier;
    }

}
