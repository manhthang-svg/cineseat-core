-- Preserve existing client tokens by hashing their raw value in-place.
ALTER TABLE refresh_token
    CHANGE COLUMN token token_hash VARCHAR(64) NOT NULL;

UPDATE refresh_token
SET token_hash = SHA2(token_hash, 256);

-- A user may have independent sessions on multiple devices.
ALTER TABLE refresh_token
    DROP INDEX user_id,
    ADD COLUMN family_id CHAR(36) NULL AFTER token_hash,
    ADD COLUMN revoked_at DATETIME(6) NULL AFTER expiry_date,
    ADD COLUMN replaced_by_token_hash VARCHAR(64) NULL AFTER revoked_at;

UPDATE refresh_token
SET family_id = UUID()
WHERE family_id IS NULL;

ALTER TABLE refresh_token
    MODIFY COLUMN family_id CHAR(36) NOT NULL,
    MODIFY COLUMN version BIGINT NOT NULL DEFAULT 0;

CREATE INDEX idx_refresh_token_family_id ON refresh_token(family_id);
CREATE INDEX idx_refresh_token_user_id ON refresh_token(user_id);
