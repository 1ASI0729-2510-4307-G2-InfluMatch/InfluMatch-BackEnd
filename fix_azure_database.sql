-- Script para corregir la estructura de la base de datos en Azure
-- Ejecuta este script directamente en tu base de datos PostgreSQL de Azure

-- 1. Verificar si la tabla users existe y tiene la estructura correcta
DO $$
BEGIN
    -- Si la tabla users no existe, crearla
    IF NOT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'users') THEN
        CREATE TABLE users (
            id BIGSERIAL PRIMARY KEY,
            email VARCHAR(255) NOT NULL UNIQUE,
            password VARCHAR(255),
            hashed_password VARCHAR(255) NOT NULL,
            role VARCHAR(20) NOT NULL,
            profile_completed BOOLEAN DEFAULT FALSE,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP
        );
        RAISE NOTICE 'Tabla users creada';
    ELSE
        -- Verificar si la columna id existe
        IF NOT EXISTS (SELECT FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'id') THEN
            -- Agregar la columna id si no existe
            ALTER TABLE users ADD COLUMN id BIGSERIAL PRIMARY KEY;
            RAISE NOTICE 'Columna id agregada a la tabla users';
        END IF;
        
        -- Verificar si la columna hashed_password existe
        IF NOT EXISTS (SELECT FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'hashed_password') THEN
            -- Agregar la columna hashed_password si no existe
            ALTER TABLE users ADD COLUMN hashed_password VARCHAR(255);
            RAISE NOTICE 'Columna hashed_password agregada a la tabla users';
        END IF;
        
        -- Verificar si la columna profile_completed existe
        IF NOT EXISTS (SELECT FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'profile_completed') THEN
            -- Agregar la columna profile_completed si no existe
            ALTER TABLE users ADD COLUMN profile_completed BOOLEAN DEFAULT FALSE;
            RAISE NOTICE 'Columna profile_completed agregada a la tabla users';
        END IF;
        
        -- Verificar si la columna created_at existe
        IF NOT EXISTS (SELECT FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'created_at') THEN
            -- Agregar la columna created_at si no existe
            ALTER TABLE users ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE 'Columna created_at agregada a la tabla users';
        END IF;
        
        -- Verificar si la columna updated_at existe
        IF NOT EXISTS (SELECT FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'updated_at') THEN
            -- Agregar la columna updated_at si no existe
            ALTER TABLE users ADD COLUMN updated_at TIMESTAMP;
            RAISE NOTICE 'Columna updated_at agregada a la tabla users';
        END IF;
    END IF;
END $$;

-- 2. Crear tabla de colaboraciones si no existe
CREATE TABLE IF NOT EXISTS collaborations (
    id BIGSERIAL PRIMARY KEY,
    initiator_id BIGINT NOT NULL,
    counterpart_id BIGINT NOT NULL,
    initiator_role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    action_type VARCHAR(20) NOT NULL,
    target_date DATE NOT NULL,
    budget DECIMAL(10,2) NOT NULL,
    location VARCHAR(255),
    deliverables TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (initiator_id) REFERENCES users(id),
    FOREIGN KEY (counterpart_id) REFERENCES users(id)
);

-- 3. Crear tabla de hitos de colaboración si no existe
CREATE TABLE IF NOT EXISTS collaboration_milestones (
    collaboration_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    location VARCHAR(255),
    deliverables TEXT,
    FOREIGN KEY (collaboration_id) REFERENCES collaborations(id) ON DELETE CASCADE
);

-- 4. Crear tabla de archivos almacenados si no existe
CREATE TABLE IF NOT EXISTS stored_files (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(255) NOT NULL,
    data BYTEA NOT NULL
);

-- 5. Crear tabla de perfiles de marca si no existe
CREATE TABLE IF NOT EXISTS brand_profiles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sector VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    logo_url VARCHAR(255),
    profile_photo_url VARCHAR(255),
    website_url VARCHAR(255),
    location VARCHAR(255),
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 6. Crear tabla de perfiles de influencer si no existe
CREATE TABLE IF NOT EXISTS influencer_profiles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    bio TEXT NOT NULL,
    country VARCHAR(255) NOT NULL,
    photo_url VARCHAR(255),
    profile_photo_url VARCHAR(255),
    followers INTEGER NOT NULL,
    location VARCHAR(255),
    user_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 7. Crear tabla para los nichos de influencer si no existe
CREATE TABLE IF NOT EXISTS influencer_profiles_niches (
    influencer_profile_id BIGINT NOT NULL,
    niche VARCHAR(255) NOT NULL,
    PRIMARY KEY (influencer_profile_id, niche),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- 8. Crear tabla para enlaces de marca si no existe
CREATE TABLE IF NOT EXISTS brand_profiles_links (
    brand_profile_id BIGINT NOT NULL,
    links_url VARCHAR(255),
    links_title VARCHAR(255),
    links_type VARCHAR(50),
    PRIMARY KEY (brand_profile_id, links_url),
    FOREIGN KEY (brand_profile_id) REFERENCES brand_profiles(id) ON DELETE CASCADE
);

-- 9. Crear tabla para adjuntos de marca si no existe
CREATE TABLE IF NOT EXISTS brand_profiles_attachments (
    brand_profile_id BIGINT NOT NULL,
    attachments_url VARCHAR(255),
    attachments_title VARCHAR(255),
    attachments_type VARCHAR(50),
    PRIMARY KEY (brand_profile_id, attachments_url),
    FOREIGN KEY (brand_profile_id) REFERENCES brand_profiles(id) ON DELETE CASCADE
);

-- 10. Crear tabla para enlaces sociales de influencer si no existe
CREATE TABLE IF NOT EXISTS influencer_profiles_social_links (
    influencer_profile_id BIGINT NOT NULL,
    social_links_platform VARCHAR(255),
    social_links_url VARCHAR(255),
    social_links_followers INTEGER,
    PRIMARY KEY (influencer_profile_id, social_links_platform),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- 11. Crear tabla para enlaces de influencer si no existe
CREATE TABLE IF NOT EXISTS influencer_profiles_links (
    influencer_profile_id BIGINT NOT NULL,
    links_url VARCHAR(255),
    links_title VARCHAR(255),
    links_type VARCHAR(50),
    PRIMARY KEY (influencer_profile_id, links_url),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- 12. Crear tabla para adjuntos de influencer si no existe
CREATE TABLE IF NOT EXISTS influencer_profiles_attachments (
    influencer_profile_id BIGINT NOT NULL,
    attachments_url VARCHAR(255),
    attachments_title VARCHAR(255),
    attachments_type VARCHAR(50),
    PRIMARY KEY (influencer_profile_id, attachments_url),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- 13. Crear tabla de chats si no existe
CREATE TABLE IF NOT EXISTS chats (
    id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user1_id) REFERENCES users(id),
    FOREIGN KEY (user2_id) REFERENCES users(id),
    UNIQUE(user1_id, user2_id)
);

-- 14. Crear tabla de mensajes si no existe
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    attachment_url VARCHAR(255),
    attachment_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id)
);

-- Verificar la estructura final
SELECT 'Tabla users:' as info, column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'users' 
ORDER BY ordinal_position; 