-- table for tasks
CREATE TABLE IF NOT EXISTS tasks
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000) NOT NULL,
    status      INT           NOT NULL,
    created     TIMESTAMP     NOT NULL
);

-- table for user's accounts
CREATE TABLE IF NOT EXISTS accounts
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100),
    login    VARCHAR(100),
    password VARCHAR(100)
);