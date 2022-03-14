INSERT INTO writers_posts (writer_id, post_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 4);

SELECT writers_posts.writer_id, writers_posts.post_id,  posts.content, posts.created, posts.updated FROM posts JOIN writers_posts ON writers_posts.post_id=posts.post_id

SELECT writers_posts.writer_id, writers_posts.post_id, posts.post_id, content, posts.created, posts.updated FROM posts JOIN writers_posts ON writers_posts.post_id=posts.post_id"