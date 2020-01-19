CREATE DATABASE video_quizzes;

USE video_quizzes;

CREATE TABLE courses (
	courseid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	code VARCHAR(8) NOT NULL,
	semester VARCHAR(3) NOT NULL,
	section INT NOT NULL);

CREATE TABLE users (
	userid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	username VARCHAR(8) NOT NULL,
	trinityid VARCHAR(7) NOT NULL);

CREATE TABLE user_course_assoc (
	userid INT,
	FOREIGN KEY (userid) REFERENCES users (userid) ON DELETE CASCADE, 
	courseid INT,
	FOREIGN KEY (courseid) REFERENCES courses (courseid) ON DELETE CASCADE, 
	role INT NOT NULL);

CREATE TABLE quizzes (
	quizid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(80) NOT NULL,
	description VARCHAR(4000) NOT NULL);

CREATE TABLE quiz_course_close_assoc (
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE, 
	courseid INT,
	FOREIGN KEY (courseid) REFERENCES courses (courseid) ON DELETE CASCADE, 
	close_time TIMESTAMP NOT NULL);

CREATE TABLE multiple_choice_questions (
	mc_question_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prompt VARCHAR(4000) NOT NULL,
	option1 VARCHAR(4000) NOT NULL,
	option2 VARCHAR(4000) NOT NULL,
	option3 VARCHAR(4000),
	option4 VARCHAR(4000),
	option5 VARCHAR(4000),
	option6 VARCHAR(4000),
	option7 VARCHAR(4000),
	option8 VARCHAR(4000),
	correct_option INT NOT NULL);

CREATE TABLE multiple_choice_assoc (
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE,
	mc_question_id INT,
	FOREIGN KEY (mc_question_id) REFERENCES multiple_choice_questions (mc_question_id) ON DELETE CASCADE);

CREATE TABLE function_questions (
	func_question_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prompt VARCHAR(4000) NOT NULL,
	correct_code VARCHAR(4000) NOT NULL,
	function_name VARCHAR(40) NOT NULL,
	num_runs INT NOT NULL);

CREATE TABLE function_assoc (
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE,
	func_question_id INT,
	FOREIGN KEY (func_question_id) REFERENCES function_questions (func_question_id) ON DELETE CASCADE);

CREATE TABLE lambda_questions (
	lambda_question_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prompt VARCHAR(4000) NOT NULL,
	correct_code VARCHAR(4000) NOT NULL,
	return_type VARCHAR(40) NOT NULL,
	num_runs INT NOT NULL);

CREATE TABLE lambda_assoc (
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE,
	lambda_question_id INT,
	FOREIGN KEY (lambda_question_id) REFERENCES lambda_questions (lambda_question_id) ON DELETE CASCADE);

CREATE TABLE expression_questions (
	expr_question_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prompt VARCHAR(4000) NOT NULL,
	correct_code VARCHAR(4000) NOT NULL,
	general_setup VARCHAR(4000) NOT NULL,
	num_runs INT NOT NULL);

CREATE TABLE expression_assoc (
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE,
	expr_question_id INT,
	FOREIGN KEY (expr_question_id) REFERENCES expression_questions (expr_question_id) ON DELETE CASCADE);

CREATE TABLE variable_specifications (
	question_id INT NOT NULL,
	question_type INT NOT NULL,
	param_number INT NOT NULL,
	spec_type INT NOT NULL,
	name VARCHAR(40) NOT NULL,
	min INT,
	max INT,
	length INT,
	min_length INT,
	max_length INT,
	gen_code VARCHAR(4000),
	CONSTRAINT UNIQUE (question_id, question_type, param_number));
	
CREATE TABLE mc_answers (
	userid INT,
	FOREIGN KEY (userid) REFERENCES users (userid) ON DELETE CASCADE,
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE,
	mc_question_id INT,
	FOREIGN KEY (mc_question_id) REFERENCES multiple_choice_questions (mc_question_id) ON DELETE CASCADE,
	selection INT NOT NULL,
	correct BOOLEAN NOT NULL,
	answer_time TIMESTAMP NOT NULL);
	
CREATE TABLE code_answers (
	userid INT,
	FOREIGN KEY (userid) REFERENCES users (userid) ON DELETE CASCADE,
	quizid INT,
	FOREIGN KEY (quizid) REFERENCES quizzes (quizid) ON DELETE CASCADE,
	question_id INT NOT NULL,
	question_type INT NOT NULL,
	answer VARCHAR(4000) NOT NULL,
	correct BOOLEAN NOT NULL,
	answer_time TIMESTAMP NOT NULL);

GRANT ALL ON video_quizzes.* TO 'mlewis'@'localhost';