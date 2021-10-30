/**
 * Autor: gabricio
 * Criado em: 2020-10-24
 */
package br.com.gabricio.otif.utils;

import java.text.DecimalFormat;

public class CpfCnpjUtil {

	private static final String VALUE_CANNOT_BE_NULL_OR_EMPTY = "Valor não pode ser nulo.";

	private static final DecimalFormat CNPJ_NFORMAT = new DecimalFormat("00000000000000");

	private static final DecimalFormat CPF_NFORMAT = new DecimalFormat("00000000000");

	private static final int[] weightCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int[] weightCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	public static String removeFormatacao(String cpfCnpj) {
		if (cpfCnpj != null && isNumeric(cpfCnpj)) {
			Long cpfCnpjLong = Long.parseLong(cpfCnpj.replaceAll("[^0-9]+", ""));
			if (isCPF(cpfCnpjLong)) {
				return String.format("%011d", cpfCnpjLong);
			} else if (isCNPJ(cpfCnpjLong)) {
				return String.format("%014d", cpfCnpjLong);
			}
		}
		return cpfCnpj;
	}

	public static boolean isNumeric(String str) {
		try {
			return str.matches(".*\\d.*");
		} catch (Throwable e) {
			return false;
		}
	}

	public static boolean isCPForCPNJ(String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_OR_EMPTY);
		}
		try {
			return isCPForCPNJ(Long.parseLong(value.replaceAll("[^0-9]+", "")));
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isCPForCPNJ(Long value) {

		final int valueSize = value.toString().length();
		if (valueSize > 14) {
			return false;
		}

		boolean isCPF = valueSize < 12;

		return isCPF ? isCPF(value) : isCNPJ(value);
	}

	public static boolean isCPF(Long value) {

		String CPF = CPF_NFORMAT.format(value);

		int firstPart = calcDigit(CPF.substring(0, 9), weightCPF);
		int lastPart = calcDigit(CPF.substring(0, 9) + firstPart, weightCPF);

		return CPF.substring(9).equals(String.format("%d%d", firstPart, lastPart));
	}

	public static boolean isCNPJ(Long value) {

		String CNPJ = CNPJ_NFORMAT.format(value);

		Integer firstPart = calcDigit(CNPJ.substring(0, 12), weightCNPJ);
		Integer lastPart = calcDigit(CNPJ.substring(0, 12) + firstPart, weightCNPJ);

		return CNPJ.substring(12).equals(String.format("%d%d", firstPart, lastPart));
	}

	private static int calcDigit(String stringBase, int[] weight) {
		int sum = 0;
		for (int index = stringBase.length() - 1, digit; index >= 0; index--) {
			digit = Integer.parseInt(stringBase.substring(index, index + 1));
			sum += digit * weight[weight.length - stringBase.length() + index];
		}
		sum = 11 - sum % 11;
		return sum > 9 ? 0 : sum;
	}
}