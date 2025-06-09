-- Renombrar columna password_hash a hashed_password
ALTER TABLE users RENAME COLUMN password_hash TO hashed_password;

-- Agregar columna password
ALTER TABLE users ADD COLUMN password VARCHAR(255);

-- Agregar columna profile_completed si no existe
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_completed BOOLEAN DEFAULT FALSE; 