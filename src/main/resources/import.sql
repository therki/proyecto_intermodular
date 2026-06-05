-- Crear usuarios
INSERT INTO users (email, fullname, username, password, role) VALUES ('admin@example.com', 'Administrador', 'admin', '{noop}12345', 'ADMIN');

INSERT INTO users (email, fullname, username, password, role) VALUES ('gestor@example.com', 'Usuario Gestor', 'gestor', '{noop}12345', 'GESTOR');

INSERT INTO users (email, fullname, username, password, role) VALUES ('usuario@example.com', 'Usuario Normal', 'usuario', '{noop}12345', 'USUARIO');

-- Crear categoria
INSERT INTO categories (title, user_id) VALUES ('General',(SELECT id FROM users WHERE username = 'admin'));
-- Crear etiqueta
INSERT INTO tags (name, user_id) VALUES ('Clase', (SELECT id FROM users WHERE username = 'usuario'));
-- Crear tarea
INSERT INTO tasks (title, description, completed, created_at, author_id, category_id, priority) VALUES ('Nueva Tarea', 'Primera tarea de prueba', 0, NOW(),(SELECT id FROM users WHERE username = 'usuario'), (SELECT id FROM categories WHERE title = 'General'), 'MEDIA');
-- Relacionar tarea y etiqueta
INSERT INTO task_tag(tag_id, task_id) VALUES(1,1);