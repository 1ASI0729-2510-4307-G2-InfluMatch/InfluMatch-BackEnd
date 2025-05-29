package com.influmatch.shared.infrastructure;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/** Convierte CamelCase ➜ snake_case y añade plural simple (…s). */
public class SnakePluralNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment env) {
        String snake = addUnderscores(name.getText());
        String plural = snake.endsWith("s") ? snake : snake + "s";
        return Identifier.toIdentifier(plural);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment env) {
        return Identifier.toIdentifier(addUnderscores(name.getText()));
    }

    /* camelCase ➜ camel_case */
    private String addUnderscores(String text) {
        return text                       // UserProfile → user_profile
                .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
                .toLowerCase();
    }
}
