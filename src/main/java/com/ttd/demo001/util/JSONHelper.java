package com.ttd.demo001.util;

import org.json.simple.JSONObject;

public class JSONHelper {

	@SuppressWarnings("unchecked")
	public static JSONObject createJSONString(String paramNameString, String paramString) {

		JSONObject headerJson = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		String[] paramNameArray = paramNameString.split(",");

		String[] paramArray = paramString.split(",");

		for (int i = 0; i < paramArray.length; i++) {
			if ("InvoiceNumber".equals(paramNameArray[i]))
				headerJson.put(paramArray[i], jsonObject);
			else
				jsonObject.put(paramNameArray[i], paramArray[i]);
		}

		return headerJson;
	}
}
