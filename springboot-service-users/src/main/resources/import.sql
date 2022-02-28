INSERT INTO users (username, password, enabled, first_name, last_name, email) VALUES ('dellmdq','$2a$10$ZVhoQ8HVIN.UJ3RR6oBEJOUZZvJMQxX0UlSXapenumgSElF23LuPO','true','Erik','Dell','erikdell@gmail.com');
INSERT INTO users (username, password, enabled, first_name, last_name, email) VALUES ('florchu','$2a$10$cd.yEZ.Eg5fP2dYQraIbVeYAo3Jyv6FBMeWH4HcWo.MsrBKRRHYTi','true','Flor','Daurelio','flordau@gmail.com');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO user_to_roles (user_id, role_id) VALUES (1,1);
INSERT INTO user_to_roles (user_id, role_id) VALUES (2,2);
INSERT INTO user_to_roles (user_id, role_id) VALUES (2,1);