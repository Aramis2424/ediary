DELETE FROM entries;
DELETE FROM diaries;
DELETE FROM moods;
DELETE FROM owners;
ALTER TABLE entries ALTER COLUMN entry_id RESTART WITH 1;
ALTER TABLE diaries ALTER COLUMN diary_id RESTART WITH 1;
ALTER TABLE moods ALTER COLUMN mood_id RESTART WITH 1;
ALTER TABLE owners ALTER COLUMN owner_id RESTART WITH 1;

INSERT INTO owners (name, birth_date, login, password, created_date) VALUES ('Ivan', '2000-01-01', 'example123', '$2b$12$wtI65LG8LCaTL8/1haeuM.7BRwWZ5Y9KoVd00dTEQIciGgL1Ll83.', '2020-01-01'); -- bcrypt hash of passsword pass123

INSERT INTO diaries (owner_fk, title, description, cnt_entries, created_date) VALUES (1, 'd1', 'about1', 5, '2020-01-01');
INSERT INTO diaries (owner_fk, title, description, cnt_entries, created_date) VALUES (1, 'd2', 'about1', 5, '2020-01-01');

INSERT INTO entries (diary_fk, title, content, created_date) VALUES (1, 'Day1', 'Good day 1', '2021-01-01');
INSERT INTO entries (diary_fk, title, content, created_date) VALUES (1, 'Day2', 'Good day 2', '2021-01-02');

INSERT INTO moods (owner_fk, score_mood, score_productivity, bedtime, wake_up_time, created_date) VALUES (1, 1, 10, '2021-01-01 01:30:00', '2021-01-01 08:15:00', '2021-01-01');
INSERT INTO moods (owner_fk, score_mood, score_productivity, bedtime, wake_up_time, created_date) VALUES (1, 2, 9, '2021-01-02 01:00:00', '2021-01-02 08:30:00', '2021-01-02');
