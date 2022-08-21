-- table for tasks
create TABLE IF NOT EXISTS tasks
(
    id          SERIAL PRIMARY KEY,
    name        TEXT  NOT NULL,
    description TEXT NOT NULL,
    status      TEXT NOT NULL check (status in ('NEW', 'FINISHED')),
    created     TIMESTAMP     NOT NULL
);

-- table for user's accounts
create TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    name     TEXT,
    login    TEXT,
    password TEXT
);