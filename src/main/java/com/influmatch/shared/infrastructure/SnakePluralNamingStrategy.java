package com.influmatch.shared.infrastructure;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Convierte CamelCase a snake_case y pluraliza los nombres de tablas.
 *  Ej.: User               -> users
 *       BrandProfile       -> brand_profiles
 *       createdAt (column) -> created_at
 */
public class SnakePluralNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // Primero convertir a snake_case
        String tableName = toSnake(name.getText());
        
        // Solo pluralizar si no termina en 's'
        if (!tableName.endsWith("s")) {
            tableName = tableName + "s";
        }
        
        return Identifier.toIdentifier(tableName);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment env) {
        return new Identifier(toSnake(name.getText()), name.isQuoted());
    }

    /* Convierte CamelCase → snake_case */
    private String toSnake(String name) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return name.replaceAll(regex, replacement).toLowerCase();
    }
}
