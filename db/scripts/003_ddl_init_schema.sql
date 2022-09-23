-- create table for priorities category
CREATE TABLE IF NOT EXISTS priorities
(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    position INTEGER NOT NULL
);

-- inserting 3 main category
INSERT INTO priorities (name, position) VALUES ('Low', 1);
INSERT INTO priorities (name, position) VALUES ('Normal', 2);
INSERT INTO priorities (name, position) VALUES ('High', 3);

-- updating tasks table - adding new column with reference to priorities
ALTER TABLE tasks ADD COLUMN  priority_id INTEGER REFERENCES priorities (id);

-- filling new column
UPDATE tasks SET priority_id = (SELECT id FROM priorities WHERE name = 'Normal' LIMIT 1);