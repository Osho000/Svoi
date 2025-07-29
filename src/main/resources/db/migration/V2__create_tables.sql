CREATE TABLE IF NOT EXISTS users_schema.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS users_schema.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users_schema.user_roles (
    user_id INTEGER NOT NULL REFERENCES users_schema.users(id),
    role_id INTEGER NOT NULL REFERENCES users_schema.roles(id),
    PRIMARY KEY (user_id, role_id)
);