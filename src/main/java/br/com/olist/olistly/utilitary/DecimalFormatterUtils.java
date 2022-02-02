package br.com.olist.olistly.utilitary;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalFormatterUtils {
	private static final DecimalFormatSymbols decimalFormatSymbolsUSA = new DecimalFormatSymbols(Locale.ENGLISH);
	private static final DecimalFormatSymbols decimalFormatSymbolsBrazilian = new DecimalFormatSymbols(Locale.ITALIAN);

	private static final DecimalFormat twoDecimalUSAFormat = new DecimalFormat("#,###,###,##0.00", decimalFormatSymbolsUSA);
	private static final DecimalFormat twoDecimalBrazilianFormat = new DecimalFormat("#,###,###,##0.00", decimalFormatSymbolsBrazilian);
	private static final DecimalFormat twoDecimalUSAFormatWithoutThousandSeparator = new DecimalFormat("#0.00", decimalFormatSymbolsUSA);
	private static final DecimalFormat twoDecimalBrazilianFormatWithoutThousandSeparator = new DecimalFormat("#0.00", decimalFormatSymbolsBrazilian);

	private static final DecimalFormat threeDecimalUSAFormat = new DecimalFormat("#,###,###,##0.0", decimalFormatSymbolsUSA);
	private static final DecimalFormat threeDecimalBrazilianFormat = new DecimalFormat("#,###,###,##0.0", decimalFormatSymbolsBrazilian);
	private static final DecimalFormat threeDecimalUSAFormatWithoutThousandSeparator = new DecimalFormat("#0.0", decimalFormatSymbolsUSA);
	private static final DecimalFormat threeDecimalBrazilianFormatWithoutThousandSeparator = new DecimalFormat("#0.0", decimalFormatSymbolsBrazilian);

	private static final DecimalFormat fourDecimalUSAFormat = new DecimalFormat("#,###,###,##0.0000", decimalFormatSymbolsUSA);
	private static final DecimalFormat fourDecimalBrazilianFormat = new DecimalFormat("#,###,###,##0.0000", decimalFormatSymbolsBrazilian);
	private static final DecimalFormat fourDecimalUSAFormatWithoutThousandSeparator = new DecimalFormat("#0.0000", decimalFormatSymbolsUSA);
	private static final DecimalFormat fourDecimalBrazilianFormatWithoutThousandSeparator = new DecimalFormat("#0.0000", decimalFormatSymbolsBrazilian);

	private Integer value;
	private boolean isTwoDecimal;
	private boolean isThreeDecimal;
	private boolean isFourDecimal;
	private boolean isDotDecimalSeparator;
	private boolean isCommaDecimalSeparator;
	private boolean isDotThousandSeparator;
	private boolean isCommaThousandSeparator;
	private boolean isThousandWithoutSeparator;

	public static DecimalFormatterUtils value(Integer value) {
		return new DecimalFormatterUtils(value);
	}

	public DecimalFormatterUtils(Integer value) {
		this.value = Integer.valueOf(value == null ? 0 : value);
		isTwoDecimal = true;
		isDotDecimalSeparator = true;
		isThousandWithoutSeparator = true;

		isThreeDecimal = false;
		isFourDecimal = false;
		isCommaDecimalSeparator = false;
		isDotThousandSeparator = false;
		isCommaThousandSeparator = false;
	}

	public DecimalFormatterUtils withTwoDecimal() {
		isTwoDecimal = true;
		isThreeDecimal = false;
		isFourDecimal = false;
		return this;
	}

	public DecimalFormatterUtils withThreeDecimal() {
		isThreeDecimal = true;
		isFourDecimal = false;
		isTwoDecimal = false;
		return this;
	}

	public DecimalFormatterUtils withFourDecimal() {
		isFourDecimal = true;
		isTwoDecimal = false;
		isThreeDecimal = false;
		return this;
	}

	public DecimalFormatterUtils withDotDecimalSeparator() {
		isDotDecimalSeparator = true;
		isCommaDecimalSeparator = false;
		return this;
	}

	public DecimalFormatterUtils withCommaDecimalSeparator() {
		isCommaDecimalSeparator = true;
		isDotDecimalSeparator = false;
		return this;
	}

	public DecimalFormatterUtils withDotThousandSeparator() {
		isDotThousandSeparator = true;
		isCommaThousandSeparator = false;
		isThousandWithoutSeparator = false;
		return this;
	}

	public DecimalFormatterUtils withCommaThousandSeparator() {
		isCommaThousandSeparator = true;
		isDotThousandSeparator = false;
		isThousandWithoutSeparator = false;
		return this;
	}

	public String format() throws IllegalArgumentException{
		if (isTwoDecimal) {
			if (isDotDecimalSeparator && isCommaThousandSeparator)
				return twoDecimalUSAFormat.format(value / 100);
			if (isCommaDecimalSeparator && isDotThousandSeparator)
				return twoDecimalBrazilianFormat.format(value / 100);
			if (isDotDecimalSeparator && isThousandWithoutSeparator)
				return twoDecimalUSAFormatWithoutThousandSeparator.format(value / 100);
			if (isCommaDecimalSeparator && isThousandWithoutSeparator)
				return twoDecimalBrazilianFormatWithoutThousandSeparator.format(value / 100);
			else
				throw new IllegalArgumentException("Converter decimal combination error");
		}

		if (isThreeDecimal) {
			if (isDotDecimalSeparator && isCommaThousandSeparator)
				return threeDecimalUSAFormat.format(value / 1000);
			if (isCommaDecimalSeparator && isDotThousandSeparator)
				return threeDecimalBrazilianFormat.format(value / 1000);
			if (isDotDecimalSeparator && isThousandWithoutSeparator)
				return threeDecimalUSAFormatWithoutThousandSeparator.format(value / 1000);
			if (isCommaDecimalSeparator && isThousandWithoutSeparator)
				return threeDecimalBrazilianFormatWithoutThousandSeparator.format(value / 1000);
			else
				throw new IllegalArgumentException("Converter decimal combination error");
		}

		if (isFourDecimal) {
			if (isDotDecimalSeparator && isCommaThousandSeparator)
				return fourDecimalUSAFormat.format(value / 10000);
			if (isCommaDecimalSeparator && isDotThousandSeparator)
				return fourDecimalBrazilianFormat.format(value / 10000);
			if (isDotDecimalSeparator && isThousandWithoutSeparator)
				return fourDecimalUSAFormatWithoutThousandSeparator.format(value / 10000);
			if (isCommaDecimalSeparator && isThousandWithoutSeparator)
				return fourDecimalBrazilianFormatWithoutThousandSeparator.format(value / 10000);
			else
				throw new IllegalArgumentException("Converter decimal combination error");
		}

		throw new IllegalArgumentException("Converter decimal combination error");
	}
}