-- =====================================================
-- V1: Create all tables
-- =====================================================

-- Department table
CREATE TABLE IF NOT EXISTS t_department (
    id   INT          NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Employee table
CREATE TABLE IF NOT EXISTS t_employee (
    id            INT          NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    salary        DOUBLE       NOT NULL,
    department_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id) REFERENCES t_department (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Users table (Spring Security / AppUser)
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- User roles (ElementCollection for AppUser.roles)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT       NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Refresh tokens
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    token       VARCHAR(255) NOT NULL,
    user_id     BIGINT,
    expiry_date DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_refresh_token UNIQUE (token),
    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
