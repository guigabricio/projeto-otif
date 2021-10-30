package br.com.gabricio.otif.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.gabricio.otif.utils.DataUtil;

public class DataHoraValidator implements ConstraintValidator<ValidarDataHora, String> {

	@Override
	public void initialize(ValidarDataHora constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ehHoraValida(value);
	}

	public boolean ehHoraValida(String date) {
		try {
			if(date == null || "".equals(date)) {
				return true;
			}
			return DataUtil.ehValido(date, DataUtil.DDMMYYY_HHMMSS);
		} catch (Exception e) {
			return false;
		}
	}
}