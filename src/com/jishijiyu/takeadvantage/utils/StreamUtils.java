package com.jishijiyu.takeadvantage.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class StreamUtils {
	public static String parserInputStream(InputStream is) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);

		StringWriter sw = new StringWriter();
		String str = null;
		while ((str = reader.readLine()) != null) {
			sw.write(str);
		}
		is.close();
		return sw.toString();
	}
	
}