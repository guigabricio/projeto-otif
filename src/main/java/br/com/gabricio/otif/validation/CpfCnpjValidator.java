package br.com.gabricio.otif.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.gabricio.otif.utils.CpfCnpjUtil;

public class CpfCnpjValidator implements ConstraintValidator<ValidarCpfCnpj, String> {

    @Override
    public void initialize(ValidarCpfCnpj constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty() || CpfCnpjUtil.isCPForCPNJ(value);
    }

}