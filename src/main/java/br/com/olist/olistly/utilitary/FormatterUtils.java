package br.com.olist.olistly.utilitary;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class FormatterUtils {
	private static final Pattern removeAccentuationPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

	public static String keepNumbers(String value) {
		return value.replaceAll("[^0-9]", "");
	}

	public static String removeAccentuation(String value) {
		String nfdNormalizedString = Normalizer.normalize(value, Normalizer.Form.NFD);
        return removeAccentuationPattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes)
			sb.append(String.format("%02X", b));

		return sb.toString();
	}

}
