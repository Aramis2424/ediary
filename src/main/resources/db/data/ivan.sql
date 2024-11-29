INSERT INTO owners (name, birth_date, login, password, created_date) VALUES ('Ivan', '2000-01-01', 'ivan01', '$2b$12$HUN.9ltTgXBo4GLrxaUy6uk4HZp0Hgg87rwsUBAfC4CcVlxD2M9d6', '2020-01-01');
INSERT INTO diaries (owner_fk, title, description, cnt_entries, created_date) VALUES (11, 'Direction.', 'These morning religious bar call reflect.', 3, '2020-02-10');
INSERT INTO diaries (owner_fk, title, description, cnt_entries, created_date) VALUES (11, 'Give.', 'Future themselves investment however leader.', 3, '2020-02-10');
INSERT INTO moods (owner_fk, score_mood, score_productivity, bedtime, wake_up_time, created_date) VALUES (11, 4, 2, '2020-02-04T00:00:00', '2020-02-04T00:00:00', '2020-03-15');
INSERT INTO moods (owner_fk, score_mood, score_productivity, bedtime, wake_up_time, created_date) VALUES (11, 5, 10, '2020-02-12T00:00:00', '2020-02-12T00:00:00', '2020-03-19');
INSERT INTO moods (owner_fk, score_mood, score_productivity, bedtime, wake_up_time, created_date) VALUES (11, 10, 2, '2020-02-01T00:00:00', '2020-02-01T00:00:00', '2020-03-12');
INSERT INTO entries (diary_fk, title, content, created_date) VALUES (14, 'Mr lawyer.', 'Upon husband the quickly something. Grow cold special. Will see court however.', '2020-03-19');
INSERT INTO entries (diary_fk, title, content, created_date) VALUES (14, 'Great.', 'Summer our blood ask face sort significant someone. Billion democratic ball past. Contain traditional seem suddenly deep.', '2020-03-12');
INSERT INTO entries (diary_fk, title, content, created_date) VALUES (14, 'Say.', 'Second wear order lawyer single. Carry drug opportunity still. Out church well see business.', '2020-03-01');
