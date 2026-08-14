
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL ,
                       password_hash VARCHAR(500) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE urls (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      short_code VARCHAR(500) NOT NULL UNIQUE,
                      link VARCHAR(500) NOT NULL,
                      access_count INT NOT NULL DEFAULT 0,
                      created_at TIMESTAMP NOT NULL,
                      last_access TIMESTAMP NULL,
                      user_id BIGINT NOT NULL,

                      CONSTRAINT fk_url_user
                          FOREIGN KEY (user_id)
                              REFERENCES users(id)
);