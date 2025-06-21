-- Corregir la estructura de la tabla users para que coincida con la entidad Java
-- Primero eliminamos la tabla si existe para recrearla correctamente
DROP TABLE IF EXISTS collaboration_milestones;
DROP TABLE IF EXISTS collaborations;
DROP TABLE IF EXISTS users CASCADE;

-- Recrear la tabla users con la estructura correcta
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

-- Recrear la tabla collaborations
CREATE TABLE collaborations (
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

-- Recrear la tabla collaboration_milestones
CREATE TABLE collaboration_milestones (
    collaboration_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    location VARCHAR(255),
    deliverables TEXT,
    FOREIGN KEY (collaboration_id) REFERENCES collaborations(id) ON DELETE CASCADE
); 