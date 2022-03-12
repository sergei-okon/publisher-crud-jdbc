SELECT posts.post_id, content,created,updated,label_id FROM posts, posts_labels WHERE (posts.post_id=2 AND posts_labels.post_id=2)


SELECT posts.post_id, content,created,updated,label_id FROM posts   LEFT OUTER JOIN posts_labels ON  posts.post_id = posts_labels.post_id WHERE posts.post_id=2

SELECT posts.post_id, content,created,updated,label_id FROM posts LEFT OUTER JOIN posts_labels ON  posts.post_id = posts_labels.post_id


SELECT   posts_labels.label_id, labels.name FROM labels, posts_labels    WHERE post_id=2    ;

SELECT posts_labels.label_id FROM posts_labels JOIN labels   on  labels.label_id=posts_labels.label_id

SELECT post_id, label_id FROM posts_labels;


SELECT  labels.name, posts_labels.label_id FROM labels JOIN posts_labels  ON posts_labels.label_id=labels.label_id WHERE post_id=2

SELECT post_id, label_id FROM posts_labels

SELECT  posts_labels.post_id, posts_labels.label_id, labels.name FROM labels JOIN posts_labels  ON posts_labels.label_id=labels.label_id
SELECT  posts_labels.post_id, posts_labels.label_id, labels.name FROM labels JOIN posts_labels  ON posts_labels.label_id=labels.label_id

SELECT label_id, name FROM labels WHERE label_id=7

INSERT INTO posts_labels (post_id, label_id)  VALUES ()

UPDATE posts SET  posts.content, updated  WHERE post_id=?