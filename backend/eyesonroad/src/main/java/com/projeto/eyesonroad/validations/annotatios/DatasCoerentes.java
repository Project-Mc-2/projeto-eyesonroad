package com.projeto.eyesonroad.validations.annotatios;

// Indica que esta anotação deve aparecer na documentação (JavaDoc).
// Ou seja, ela não é "invisível" — faz parte da API pública.

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.senai.smartfila.validations.validators.DatasCoerentesValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented

// Define que esta anotação é uma validação customizada.
// O parâmetro "validatedBy" informa qual classe contém a lógica da validação.
// Nesse caso, quem valida de verdade é a classe DatasCoerentesValidator.
@Constraint(validatedBy = DatasCoerentesValidator.class)

// Define onde essa anotação pode ser aplicada.
// TYPE significa que ela será usada em nível de classe (e não em um campo específico).
// Exemplo: @DatasCoerentes na classe Aluno.
@Target({ ElementType.TYPE })

// Define que essa anotação estará disponível em tempo de execução.
// Isso é essencial, pois o Spring precisa acessar essa anotação durante a execução.
@Retention(RetentionPolicy.RUNTIME)

// Aqui estamos criando uma anotação personalizada chamada DatasCoerentes.
public @interface DatasCoerentes {

    // Mensagem padrão que será exibida quando a validação falhar.
    // Pode ser sobrescrita quando usada, por exemplo:
    // @DatasCoerentes(message = "Erro personalizado")
    String message() default "As datas informadas são incoerentes.";

    // Campo obrigatório da especificação Bean Validation.
    // Permite agrupar validações em diferentes contextos (mais avançado).
    // Exemplo: validação diferente para criação e atualização.
    Class<?>[] groups() default {};

    // Campo obrigatório da especificação Bean Validation.
    // Usado para adicionar metadados às validações (ex: níveis de severidade).
    // Na prática básica, quase nunca é usado.
    Class<? extends Payload>[] payload() default {};
}