insert into category_news (title_category, description_category) values ('World', 'news about events in the world'),
('Sport', 'News about sport'), ('Economy', 'News about Economy');
insert into users (user_name, user_surname, email, password) values ('Admin', 'Adminovich', 'a@gmail.com', '123'),
('User', 'Userovich', 'u@gmail.com', '321');
insert into authorities (authority, user_id) values ('ROLE_ADMIN', 1), ('ROLE_USER', 2);
insert into news (title, content, user_id, category_news_id, create_at, update_at) values
('news 1', 'content 1', 1, 1, '2024-01-03T10:15:30.00Z', '2024-01-05T10:15:30.00Z'),
('news 2', 'content 2', 2, 1, '2024-01-03T10:15:30.00Z', '2024-01-05T10:15:30.00Z'),
('news 3', 'content 3', 1, 3, '2024-01-03T10:15:30.00Z', '2024-01-05T10:15:30.00Z'),
('news 4', 'content 4', 1, 2, '2024-01-03T10:15:30.00Z', '2024-01-05T10:15:30.00Z'),
('news 5', 'content 5', 2, 2, '2024-01-03T10:15:30.00Z', '2024-01-05T10:15:30.00Z');
insert into comment_news (comment, create_at, update_at, news_id, user_id) values
('comment 1', '2024-01-04T10:15:30.00Z', '2024-01-06T10:15:30.00Z', 1, 1),
('comment 2', '2024-01-04T10:15:30.00Z', '2024-01-06T10:15:30.00Z', 1, 2),
('comment 3', '2024-01-04T10:15:30.00Z', '2024-01-06T10:15:30.00Z', 2, 1);