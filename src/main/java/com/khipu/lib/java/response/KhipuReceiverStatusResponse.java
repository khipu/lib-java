/* Copyright (c) 2013, khipu SpA
 * All rights reserved.
 * Released under BSD LICENSE, please refer to LICENSE.txt
 */

package com.khipu.lib.java.response;

public class KhipuReceiverStatusResponse implements KhipuResponse {
	private boolean _readyToCollect;
	private String _type;

	public KhipuReceiverStatusResponse(boolean readyToCollect, String type) {
		_readyToCollect = readyToCollect;
		_type = type;
	}

	public boolean getReadyToCollect() {
		return _readyToCollect;
	}

	public boolean isReadyToCollect() {
		return _readyToCollect;
	}

	public void setReadyToCollect(boolean readyToCollect) {
		_readyToCollect = readyToCollect;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	@Override
	public String toString() {
		return "readyToCollect: " + _readyToCollect + ", type:  " + _type;
	}
}
