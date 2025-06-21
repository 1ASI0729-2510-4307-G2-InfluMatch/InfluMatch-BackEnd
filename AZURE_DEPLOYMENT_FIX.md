# Solución para el problema de Azure Deployment

## Problema identificado

El error `ERROR: column u1_0.id does not exist` indica que las migraciones de Flyway no se han ejecutado correctamente en Azure, o que la estructura de la base de datos no coincide con lo que espera la aplicación.

## Solución paso a paso

### 1. Ejecutar el script SQL de corrección

Conecta a tu base de datos PostgreSQL de Azure y ejecuta el script `fix_azure_database.sql` que se encuentra en la raíz del proyecto. Este script:

- Verifica y corrige la estructura de la tabla `users`
- Crea todas las tablas necesarias si no existen
- Asegura que todas las columnas requeridas estén presentes

### 2. Verificar las variables de entorno en Azure

Asegúrate de que las siguientes variables de entorno estén configuradas en tu Azure App Service:

```
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://tu-servidor-postgresql:5432/tu-base-de-datos
SPRING_DATASOURCE_USERNAME=tu-usuario
SPRING_DATASOURCE_PASSWORD=tu-contraseña
APPLICATION_SECURITY_JWT_SECRET_KEY=tu-clave-secreta-jwt
```

### 3. Configuración actualizada

Se han actualizado las siguientes configuraciones:

#### application.yml
- Se agregó la configuración completa de JPA y Flyway para el perfil de producción
- Se configuró el `SnakePluralNamingStrategy` para ambas configuraciones
- Se estableció `hibernate.ddl-auto: validate` para producción

#### Nuevas migraciones
- `V5__fix_users_table.sql`: Corrige la estructura de la tabla users
- `V6__create_profile_tables.sql`: Crea las tablas de perfiles
- `V7__create_chat_tables.sql`: Crea las tablas de chat

### 4. Desplegar los cambios

1. Haz commit de todos los cambios:
```bash
git add .
git commit -m "Fix Azure deployment - database structure and configuration"
git push
```

2. Azure debería detectar los cambios y hacer el redeploy automáticamente.

### 5. Verificar el despliegue

Después del despliegue, verifica que:

1. La aplicación se inicie sin errores
2. Las migraciones de Flyway se ejecuten correctamente
3. El endpoint `/api/auth/register` funcione correctamente

### 6. Logs de verificación

Revisa los logs de Azure para confirmar que:

- Flyway se ejecuta correctamente
- No hay errores de JPA/Hibernate
- La aplicación se conecta correctamente a la base de datos

## Comandos útiles para debugging

### Verificar la estructura de la base de datos
```sql
-- Verificar que la tabla users existe y tiene la estructura correcta
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'users' 
ORDER BY ordinal_position;

-- Verificar todas las tablas
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public';
```

### Verificar las migraciones de Flyway
```sql
-- Verificar la tabla de control de Flyway
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

## Si el problema persiste

1. **Reiniciar la aplicación**: A veces es necesario reiniciar completamente la App Service
2. **Verificar conectividad**: Asegúrate de que la App Service puede conectarse a la base de datos
3. **Revisar logs detallados**: Habilita logs más detallados temporalmente cambiando `show-sql: true` en producción
4. **Verificar permisos**: Asegúrate de que el usuario de la base de datos tiene permisos para crear/modificar tablas

## Configuración de seguridad adicional

Considera agregar estas configuraciones adicionales en Azure:

```
WEBSITES_PORT=80
WEBSITES_CONTAINER_START_TIME_LIMIT=1800
```

Esto asegura que la aplicación tenga tiempo suficiente para inicializar y ejecutar las migraciones. 