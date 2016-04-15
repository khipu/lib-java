package com.khipu.lib.java.response;

import java.util.Date;

/**
 * Objeto que representa la respuesta con los datos para la transferencia.
 *
 * @author Eli Lopez (eli.lopez@openpay.mx)
 * @version 1.3
 * @since 2016-04-15
 */
public class KhipuSetRegularTransferConciliationInfoResponse implements KhipuResponse {

    private String _paymentId;

    private String _currencyCode;

    private Double _amount;

    private String _countryCode;

    private String _destinationBankName;

    private String _destinationBankUrl;

    private String _accountIdentifier;

    private String _accountType;

    private String _accountHolderPersonalIdentifier;

    private String _accountHolderPersonalName;

    private String _accountHolderEmail;

    private Date _deadline;

    public String getPaymentId() {
        return this._paymentId;
    }

    public void setPaymentId(String paymentId) {
        this._paymentId = paymentId;
    }

    public String getCurrencyCode() {
        return this._currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this._currencyCode = currencyCode;
    }

    public Double getAmount() {
        return this._amount;
    }

    public void setAmount(Double amount) {
        this._amount = amount;
    }

    public String getCountryCode() {
        return this._countryCode;
    }

    public void setCountryCode(String countryCode) {
        this._countryCode = countryCode;
    }

    public String getDestinationBankName() {
        return this._destinationBankName;
    }

    public void setDestinationBankName(String destinationBankName) {
        this._destinationBankName = destinationBankName;
    }

    public String getDestinationBankUrl() {
        return this._destinationBankUrl;
    }

    public void setDestinationBankUrl(String destinationBankUrl) {
        this._destinationBankUrl = destinationBankUrl;
    }

    public String getAccountIdentifier() {
        return this._accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this._accountIdentifier = accountIdentifier;
    }

    public String getAccountType() {
        return this._accountType;
    }

    public void setAccountType(String accountType) {
        this._accountType = accountType;
    }

    public String getAccountHolderPersonalIdentifier() {
        return this._accountHolderPersonalIdentifier;
    }

    public void setAccountHolderPersonalIdentifier(String accountHolderPersonalIdentifier) {
        this._accountHolderPersonalIdentifier = accountHolderPersonalIdentifier;
    }

    public String getAccountHolderPersonalName() {
        return this._accountHolderPersonalName;
    }

    public void setAccountHolderPersonalName(String accountHolderPersonalName) {
        this._accountHolderPersonalName = accountHolderPersonalName;
    }

    public String getAccountHolderEmail() {
        return this._accountHolderEmail;
    }

    public void setAccountHolderEmail(String accountHolderPersonalEmail) {
        this._accountHolderEmail = accountHolderPersonalEmail;
    }

    public Date getDeadline() {
        return this._deadline;
    }

    public void setDeadline(Date deadline) {
        this._deadline = deadline;
    }

    @Override
    public String toString() {
        return "paymentId: " + this._paymentId + " currencyCode: "
                + this._currencyCode + " amount: " + this._amount + " countryCode: " + this._countryCode
                + " destinationBankName: " + this._destinationBankName + " destinationBankUrl: "
                + this._destinationBankUrl + " accountIdentifier: " + this._accountIdentifier + " accountType: "
                + this._accountType + " accountHolderPersonalIdentifier: " + this._accountHolderPersonalIdentifier
                + " accountHolderPersonalName: " + this._accountHolderPersonalName + " accountHolderEmail: "
                + this._accountHolderEmail + " deadline: " + this._deadline;
    }

}
