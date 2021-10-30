package br.com.gabricio.otif.validation;

import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HoraValidator implements ConstraintValidator<ValidarHora, String> {

	@Override
	public void initialize(ValidarHora constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ehHoraValida(value);
	}

	public boolean ehHoraValida(String date) {
		SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");
		try {
			formatador.parse(date);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

}