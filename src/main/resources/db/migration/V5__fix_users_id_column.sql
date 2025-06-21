-- Agrega la columna id como BIGSERIAL PRIMARY KEY si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name='users' AND column_name='id'
    ) THEN
        ALTER TABLE users ADD COLUMN id BIGSERIAL PRIMARY KEY;
    END IF;
END $$; 