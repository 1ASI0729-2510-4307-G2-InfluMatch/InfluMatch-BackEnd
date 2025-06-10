/* Tabla de colaboraciones */
CREATE TABLE collaborations (
    id              BIGSERIAL PRIMARY KEY,
    initiator_id    BIGINT NOT NULL,
    counterpart_id  BIGINT NOT NULL,
    initiator_role  VARCHAR(20) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    message         TEXT NOT NULL,
    action_type     VARCHAR(20) NOT NULL,
    target_date     DATE NOT NULL,
    budget          DECIMAL(10,2) NOT NULL,
    location        VARCHAR(255),
    deliverables    TEXT,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (initiator_id) REFERENCES users(id),
    FOREIGN KEY (counterpart_id) REFERENCES users(id)
);

/* Tabla de hitos de colaboración */
CREATE TABLE collaboration_milestones (
    collaboration_id BIGINT NOT NULL,
    title           VARCHAR(255) NOT NULL,
    date            DATE NOT NULL,
    description     TEXT,
    location        VARCHAR(255),
    deliverables    TEXT,
    FOREIGN KEY (collaboration_id) REFERENCES collaborations(id) ON DELETE CASCADE
); 