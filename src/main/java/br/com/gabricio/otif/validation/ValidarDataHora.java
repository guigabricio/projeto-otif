package br.com.gabricio.otif.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Constraint(validatedBy = {DataHoraValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidarDataHora {

    String message() default "Data Hora inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}