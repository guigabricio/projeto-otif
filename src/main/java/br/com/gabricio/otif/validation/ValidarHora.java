package br.com.gabricio.otif.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = {HoraValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ValidarHora {

    String message() default "Hora inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}