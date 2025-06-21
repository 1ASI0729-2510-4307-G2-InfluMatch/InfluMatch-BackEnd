-- Crear tabla de archivos almacenados
CREATE TABLE IF NOT EXISTS stored_files (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(255) NOT NULL,
    data BYTEA NOT NULL
);

-- Crear tabla de perfiles de marca
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

-- Crear tabla de perfiles de influencer
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

-- Crear tabla para los nichos de influencer
CREATE TABLE IF NOT EXISTS influencer_profiles_niches (
    influencer_profile_id BIGINT NOT NULL,
    niche VARCHAR(255) NOT NULL,
    PRIMARY KEY (influencer_profile_id, niche),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- Crear tabla para enlaces de marca
CREATE TABLE IF NOT EXISTS brand_profiles_links (
    brand_profile_id BIGINT NOT NULL,
    links_url VARCHAR(255),
    links_title VARCHAR(255),
    links_type VARCHAR(50),
    PRIMARY KEY (brand_profile_id, links_url),
    FOREIGN KEY (brand_profile_id) REFERENCES brand_profiles(id) ON DELETE CASCADE
);

-- Crear tabla para adjuntos de marca
CREATE TABLE IF NOT EXISTS brand_profiles_attachments (
    brand_profile_id BIGINT NOT NULL,
    attachments_url VARCHAR(255),
    attachments_title VARCHAR(255),
    attachments_type VARCHAR(50),
    PRIMARY KEY (brand_profile_id, attachments_url),
    FOREIGN KEY (brand_profile_id) REFERENCES brand_profiles(id) ON DELETE CASCADE
);

-- Crear tabla para enlaces sociales de influencer
CREATE TABLE IF NOT EXISTS influencer_profiles_social_links (
    influencer_profile_id BIGINT NOT NULL,
    social_links_platform VARCHAR(255),
    social_links_url VARCHAR(255),
    social_links_followers INTEGER,
    PRIMARY KEY (influencer_profile_id, social_links_platform),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- Crear tabla para enlaces de influencer
CREATE TABLE IF NOT EXISTS influencer_profiles_links (
    influencer_profile_id BIGINT NOT NULL,
    links_url VARCHAR(255),
    links_title VARCHAR(255),
    links_type VARCHAR(50),
    PRIMARY KEY (influencer_profile_id, links_url),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
);

-- Crear tabla para adjuntos de influencer
CREATE TABLE IF NOT EXISTS influencer_profiles_attachments (
    influencer_profile_id BIGINT NOT NULL,
    attachments_url VARCHAR(255),
    attachments_title VARCHAR(255),
    attachments_type VARCHAR(50),
    PRIMARY KEY (influencer_profile_id, attachments_url),
    FOREIGN KEY (influencer_profile_id) REFERENCES influencer_profiles(id) ON DELETE CASCADE
); 