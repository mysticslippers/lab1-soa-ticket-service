CREATE TYPE EVENT_TYPE AS ENUM (
    'CONCERT',
    'BASKETBALL',
    'OPERA'
);

CREATE TYPE TICKET_TYPE AS ENUM(
    'VIP',
    'USUAL',
    'BUDGETARY',
    'CHEAP'
);

CREATE TABLE EVENTS (
    id         SERIAL PRIMARY KEY CONSTRAINT is_positive CHECK (id > 0),
    name       TEXT NOT NULL CONSTRAINT is_name_not_empty CHECK (char_length(name) > 0),
    date       TIMESTAMP WITH TIME ZONE,
    event_type EVENT_TYPE NOT NULL
);

CREATE TABLE COORDINATES (
    id          SERIAL PRIMARY KEY CONSTRAINT is_positive CHECK (id > 0),
    x           REAL NOT NULL CONSTRAINT is_x_available CHECK (x > -999),
    y           REAL NOT NULL,

    CONSTRAINT is_coordinates_finite CHECK (
        x < 'Infinity'::REAL
            AND y > '-Infinity'::REAL AND y < 'Infinity'::REAL
    )
);

CREATE TABLE TICKETS (
    id            BIGSERIAL PRIMARY KEY,
    name          TEXT NOT NULL CONSTRAINT is_tickets_name_not_empty CHECK (char_length(name) > 0),
    coordinates_id INTEGER NOT NULL REFERENCES COORDINATES(id) ON DELETE RESTRICT,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT LOCALTIMESTAMP,
    price         INTEGER NOT NULL CONSTRAINT is_tickets_price_positive CHECK (price > 0),
    comment       TEXT CONSTRAINT is_comment_not_empty CHECK (char_length(comment) > 0),
    type          TICKET_TYPE NOT NULL,
    event_id      INTEGER NOT NULL REFERENCES EVENTS(id) ON DELETE RESTRICT
);

CREATE INDEX idx_tickets_event_id ON tickets (event_id);
CREATE INDEX idx_tickets_price ON tickets (price);
CREATE INDEX idx_tickets_type ON tickets (type);