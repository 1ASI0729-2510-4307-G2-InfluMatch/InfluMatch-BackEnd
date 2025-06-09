package com.influmatch.shared.infrastructure;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Convierte CamelCase a snake_case y pluraliza los nombres de tablas.
 *  Ej.: User               -> users
 *       BrandProfile       -> brand_profiles
 *       createdAt (column) -> created_at
 */
public class SnakePluralNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name,
                                          JdbcEnvironment env) {
        String plural = name.getText().endsWith("s")
                ? name.getText()
                : name.getText() + "s";
        return new Identifier(toSnake(plural), name.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name,
                                           JdbcEnvironment env) {
        return new Identifier(toSnake(name.getText()), name.isQuoted());
    }

    /* Convierte CamelCase → snake_case */
    private String toSnake(String text) {
        return text
            .replaceAll("([a-z])([A-Z])", "$1_$2")
            .toLowerCase();
    }
}
