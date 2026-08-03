-- src/main/resources/db/migration/V1__create_rides_table.sql

CREATE TABLE rides (
                       ride_id VARCHAR(36) PRIMARY KEY,
                       rider_id VARCHAR(36) NOT NULL,
                       driver_id VARCHAR(36),

    -- Embedded Location Fields
                       pickup_latitude DOUBLE PRECISION NOT NULL,
                       pickup_longitude DOUBLE PRECISION NOT NULL,
                       dropoff_latitude DOUBLE PRECISION NOT NULL,
                       dropoff_longitude DOUBLE PRECISION NOT NULL,

                       status VARCHAR(30) NOT NULL,
                       fare_amount NUMERIC(10, 2),
                       version BIGINT NOT NULL DEFAULT 0,
                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

ALTER TABLE rides ALTER COLUMN created_at SET DEFAULT now();

-- Indexing for fast queries by rider, driver, and active status
CREATE INDEX idx_rides_rider_id ON rides(rider_id);
CREATE INDEX idx_rides_driver_id ON rides(driver_id);
CREATE INDEX idx_rides_status ON rides(status);