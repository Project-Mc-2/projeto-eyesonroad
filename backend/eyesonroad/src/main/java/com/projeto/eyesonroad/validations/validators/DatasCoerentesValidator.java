package com.projeto.eyesonroad.validations.validators;

import java.time.LocalDate;

import com.projeto.eyesonroad.entities.Usuario;
import com.projeto.eyesonroad.validations.annotatios.DatasCoerentes;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DatasCoerentesValidator implements ConstraintValidator<DatasCoerentes, Usuario> {

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {

        // Se o objeto inteiro vier nulo, não invalidamos aqui
        if (usuario == null) {
            return true;
        }

        LocalDate dataNascimento = usuario.getDataNascimento();

        // Se documentos não existir, deixamos outras validações tratarem
        if (usuario.getEmail() == null) {
            return true;
        }

        LocalDate dataEmissao = usuario.getEmail().getDataEmissao();

        // Se algum campo necessário estiver nulo, outra validação cuidará disso
        if (dataNascimento == null || dataEmissao == null) {
            return true;
        }

        // Regra de negócio:
        // a data de emissão não pode ser anterior à data de nascimento
        boolean valido = !dataEmissao.isBefore(dataNascimento);

        if (!valido) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                    "A data de emissão não pode ser anterior à data de nascimento."
            )
            .addPropertyNode("documentos")
            .addPropertyNode("dataEmissao")
            .addConstraintViolation();
        }

        return valido;
    }
}