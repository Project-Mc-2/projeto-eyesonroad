package br.com.eyesonroad.validations.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.eyesonroad.validations.validators.TelefoneBRValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = TelefoneBRValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TelefoneBR {
	
	String message() default "Telefone inválido.";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default{};

}
