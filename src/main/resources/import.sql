INSERT INTO users (email, fullname, username, password, role) VALUES ('admin@example.com', 'Administrador', 'admin', '{noop}12345', 'ADMIN');

INSERT INTO users (email, fullname, username, password, role) VALUES ('gestor@example.com', 'Usuario Gestor', 'gestor', '{noop}12345', 'GESTOR');

INSERT INTO users (email, fullname, username, password, role) VALUES ('usuario@example.com', 'Usuario Normal', 'usuario', '{noop}12345', 'USUARIO');

INSERT INTO categories (title) VALUES ('General');

INSERT INTO tasks (title, description, completed, created_at, author_id, category_id, priority) VALUES ('Tarea 1', 'Primera tarea de prueba', 0, NOW(), 1, 1, 'MEDIA');