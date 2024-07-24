CREATE TABLE IF NOT EXISTS quiz
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    time INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS question
(
    id        BIGSERIAL PRIMARY KEY,
    name      TEXT NOT NULL,
    file_url VARCHAR,
    quiz_id BIGINT NOT NULL,
    CONSTRAINT fk_quiz_key FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE IF NOT EXISTS answer
(
    id          BIGSERIAL PRIMARY KEY,
    name        TEXT    NOT NULL,
    correct     BOOLEAN NOT NULL,
    question_id BIGINT  NOT NULL,
    CONSTRAINT fk_question_key FOREIGN KEY (question_id) REFERENCES question (id)
);

CREATE TABLE IF NOT EXISTS quiz_result
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR NOT NULL,
    surname      VARCHAR NOT NULL,
    album_number VARCHAR NOT NULL,
    quiz_id      BIGINT NOT NULL,
    correct_answers INTEGER NOT NULL,
    total_questions INTEGER NOT NULL,
    CONSTRAINT fk_quiz_key FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);
