-- add column for reference to the owner of the task (user)
ALTER TABLE tasks ADD COLUMN user_id integer REFERENCES users (id);