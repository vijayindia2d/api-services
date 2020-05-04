DROP TABLE IF EXISTS exams;
CREATE TABLE exams (
  id SERIAL PRIMARY KEY,
  name VARCHAR(55) NOT NULL
);
DROP TABLE IF EXISTS questions;
CREATE TABLE questions (
  id SERIAL ,
  exam_id int NOT NULL,
  question VARCHAR(500) NOT NULL,
  answer VARCHAR(500) NOT NULL,
	PRIMARY KEY (id, exam_id),
  FOREIGN KEY (exam_id) REFERENCES exams (id)
);
DROP TABLE IF EXISTS answers;
CREATE TABLE answers (
  id SERIAL PRIMARY KEY,
  exam_id int NOT NULL ,
  question_id int NOT NULL,
  student_answer VARCHAR(500) NOT NULL,
 FOREIGN KEY (exam_id, question_id) REFERENCES questions (exam_id, id)
);