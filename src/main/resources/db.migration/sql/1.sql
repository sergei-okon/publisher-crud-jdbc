Select writers.writer_id,
       first_name,
       last_name,
       writers_posts.post_id,
       posts.content,
       POSTS.created,
       POSTS.updated
FROM writers
         inner join writers_posts
                    on writers.writer_id = writers_posts.writer_id
         inner join posts
                    ON writers_posts.post_id = posts.post_id;

SELECT posts.post_id, content, created, updated
FROM posts;

SELECT posts_labels.post_id, posts_labels.label_id, labels.name
FROM labels
         JOIN posts_labels
              ON posts_labels.label_id = labels.label_id;

SELECT posts.post_id, content, created, updated, posts_labels.label_id, labels.name FROM posts INNER JOIN posts_labels ON posts.post_id = posts_labels.post_id INNER JOIN labels ON posts_labels.label_id = labels.label_id