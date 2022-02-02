package br.com.olist.olistly.utilitary;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

public class ValidatorUtils {
	private static final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
	private static final UrlValidator urlValidator = new UrlValidator();
	private static final Long LONG_ONE = Long.valueOf(1);
	private static final Integer INTEGER_ONE = Integer.valueOf(1);

	public static boolean isNullOrLessThanOne(Long value) {
		return Objects.isNull(value) || LONG_ONE > value;
	}

	public static boolean isNullOrLessThanOne(Integer value) {
		return Objects.isNull(value) || INTEGER_ONE > value;
	}

	public static boolean isNullOrEmpty(String value) {
		return Objects.isNull(value) || value.isEmpty();
	}

	public static boolean isValidEmail(String email) {
		if (isNullOrEmpty(email))
			return false;

		Matcher matcher = emailPattern.matcher(email);
	    return matcher.matches();
	}

	public static boolean isValidUrl(String url) {
		if (isNullOrEmpty(url))
			return false;

		return urlValidator.isValid(url.toLowerCase());
	}

	public static boolean isOnlyNumbers(String value) {
		return isNullOrEmpty(value) ? false : value.matches("^[0-9]+$");
	}

	public static boolean isOnlyCharactersAndNumbers(String value) {
		return isNullOrEmpty(value) ? false : value.matches("^[a-zA-Z0-9]+$");
	}
}
