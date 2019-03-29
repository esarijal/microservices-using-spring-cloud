INSERT INTO `role` (id, role_name) VALUES(1, 'ADMIN');
INSERT INTO `role` (id, role_name) VALUES(2, 'USER');

INSERT INTO privilege (id, name) VALUES(1, 'WRITE_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES(2, 'READ_PRIVILEGE');

INSERT INTO role_privilege (role_id, privilege_id) VALUES(1, 1);
INSERT INTO role_privilege (role_id, privilege_id) VALUES(1, 2);
INSERT INTO role_privilege (role_id, privilege_id) VALUES(2, 2);

-- admin/password
INSERT INTO `user` (id, password, username) VALUES(1, '$2a$10$SlfNbDFdqhfq2E5qxqXnTeigi.3Gov79.pz9fo2NUfo8BPr337zI2', 'admin');
INSERT INTO `user` (id, password, username) VALUES(2, '$2a$10$SlfNbDFdqhfq2E5qxqXnTeigi.3Gov79.pz9fo2NUfo8BPr337zI2', 'user');
INSERT INTO user_role (user_id, role_id) VALUES(1,1);
INSERT INTO user_role (user_id, role_id) VALUES(2,2);