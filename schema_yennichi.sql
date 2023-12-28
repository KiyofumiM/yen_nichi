DROP TABLE IF EXISTS comment_learning CASCADE;
DROP TABLE IF EXISTS comment_event CASCADE;
DROP TABLE IF EXISTS comment_board CASCADE;
DROP TABLE IF EXISTS favorite_learning CASCADE;
DROP TABLE IF EXISTS favorite_event CASCADE;
DROP TABLE IF EXISTS good_learning CASCADE;
DROP TABLE IF EXISTS good_event CASCADE;
DROP TABLE IF EXISTS good_board CASCADE;
DROP TABLE IF EXISTS learning CASCADE;
DROP TABLE IF EXISTS event CASCADE;
DROP TABLE IF EXISTS board CASCADE;
DROP TABLE IF EXISTS contact CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL NOT NULL,
  authority VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS learning (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  path VARCHAR(255) NOT NULL,
  title VARCHAR(20) NOT NULL,
  description VARCHAR(1000),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE learning ADD CONSTRAINT FK_users_learning FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS event (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  path VARCHAR(255) NOT NULL,
  event_at VARCHAR(10) NOT NULL,
  title VARCHAR(20) NOT NULL,
  description VARCHAR(1000),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE event ADD CONSTRAINT FK_users_event FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS board (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(30) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE board ADD CONSTRAINT FK_users_board FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS contact (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(50) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE contact ADD CONSTRAINT FK_users_contact FOREIGN KEY (user_id) REFERENCES users;

CREATE TABLE IF NOT EXISTS good_learning (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  learning_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE good_learning ADD CONSTRAINT FK_good_learning_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE good_learning ADD CONSTRAINT FK_good_learning_learning FOREIGN KEY (learning_id) REFERENCES learning;

CREATE TABLE IF NOT EXISTS good_event (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  event_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE good_event ADD CONSTRAINT FK_good_event_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE good_event ADD CONSTRAINT FK_good_event_event FOREIGN KEY (event_id) REFERENCES event;

CREATE TABLE IF NOT EXISTS good_board (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  board_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE good_board ADD CONSTRAINT FK_good_board_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE good_board ADD CONSTRAINT FK_good_board_board FOREIGN KEY (board_id) REFERENCES board;


CREATE TABLE IF NOT EXISTS favorite_learning (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  learning_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE favorite_learning ADD CONSTRAINT FK_favorite_learning_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE favorite_learning ADD CONSTRAINT FK_favorite_learning_learning FOREIGN KEY (learning_id) REFERENCES learning;

CREATE TABLE IF NOT EXISTS favorite_event (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  event_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE favorite_event ADD CONSTRAINT FK_favorite_event_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE favorite_event ADD CONSTRAINT FK_favorite_event_event FOREIGN KEY (event_id) REFERENCES event;

CREATE TABLE IF NOT EXISTS comment_learning (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  learning_id INT NOT NULL,
  description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE comment_learning ADD CONSTRAINT FK_comment_learning_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE comment_learning ADD CONSTRAINT FK_comment_learning_learning FOREIGN KEY (learning_id) REFERENCES learning;

CREATE TABLE IF NOT EXISTS comment_event (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  event_id INT NOT NULL,
  description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE comment_event ADD CONSTRAINT FK_comment_event_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE comment_event ADD CONSTRAINT FK_comment_event_event FOREIGN KEY (event_id) REFERENCES event;

CREATE TABLE IF NOT EXISTS comment_board (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  board_id INT NOT NULL,
  description VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE comment_board ADD CONSTRAINT FK_comment_board_users FOREIGN KEY (user_id) REFERENCES users;
ALTER TABLE comment_board ADD CONSTRAINT FK_comment_board_board FOREIGN KEY (board_id) REFERENCES board;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO yennichi;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO yennichi;