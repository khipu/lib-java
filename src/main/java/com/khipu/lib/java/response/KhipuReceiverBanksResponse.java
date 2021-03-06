/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.response;

import com.khipu.lib.java.Bank;

import java.util.List;

/**
 * Objeto con la respuesta los bancos disponibles para pagar a un cobrador.
 *
 * @author Alejandro Vera (alejandro.vera@khipu.com)
 * @version 1.3
 * @since 2013-12-02
 */
public class KhipuReceiverBanksResponse implements KhipuResponse {

	private List<Bank> _banks;

	public KhipuReceiverBanksResponse(List<Bank> banks) {
		_banks = banks;
	}

	/**
	 * Establece los bancos disponibles para un pago al cobrador.
	 *
	 * @param banks listado de bancos
	 */
	public void setBanks(List<Bank> banks) {
		_banks = banks;
	}

	/**
	 * Obtiene los bancos disponibles para hacer un pago al cobrador.
	 *
	 * @return el listado de bancos.
	 * @since 2013-12-02
	 */
	public List<Bank> getBanks() {
		return _banks;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (_banks != null) {
			for (Bank bank : _banks) {
				builder.append("id: ").append(bank.getId()).append(" name: ").append(bank.getName()).append(" minAmount: ").append(bank.getMinAmount()).append("\n");
			}
		}
		return builder.toString();
	}
}
