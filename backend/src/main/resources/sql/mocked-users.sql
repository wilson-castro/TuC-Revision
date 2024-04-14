TRUNCATE users;

INSERT INTO users (id, active, email, name, password) VALUES (1, true, 'email', 'Wilson', '$2a$10$FqPQ15ndFBdKKwEcExciOuFkHasOKDRpoOAHJatx0pcYcj7gZSy1a');
INSERT INTO users (id, active, email, name, password) VALUES (2, true, 'email1', 'Wilson', '$2a$10$s9KpFUq.11.cLISHFaO40eL/b9/FnkYSzZeuexnVXFb/TYwgZDZce');

alter sequence users_id_seq restart with 3;