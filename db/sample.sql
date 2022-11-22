INSERT INTO users(
    username, password, fullname, role, locked)
    VALUES ('test@test.com', '$2a$10$hzJAx1lp16h09T4zBwkBZO1LeuFYW/5Smw6hb51MZZftPiLehkACW', 'Test user', 'generic', false);
    
INSERT INTO users(
    username, password, fullname, role, locked)
    VALUES ('second@test.com', '$2a$10$hT4Qv0wwnB5vVPwmaC2sHel2b22YUp8u1jTmy0W4U0sX.X6qgNnHO', 'Bob Cat', 'generic', false);
    
    
INSERT INTO articles(
    article_name, author_id, article_description, article_attachment)
    VALUES ('first-article', 1, 'This is just a test article', '/user/te/test/2022/11/my-attach.mp3');
    
INSERT INTO articles(
    article_name, author_id, article_description, article_attachment)
    VALUES ('another-article', 1, 'Just yet another one', '/user/te/test/2022/11/second.avi');
    

INSERT INTO comments (
    article_id, author_id, comment, comment_attachment)
    VALUES (1, 1, 'Commenting that because I have something to say', '/user/te/test/2022/11/big-one.avi');
    

INSERT INTO comments (
    article_id, author_id, comment, comment_attachment)
    VALUES (1, 2, 'Responding to the first comment', '');
    
INSERT INTO comments (
    article_id, author_id, comment, comment_attachment)
    VALUES (2, 2, 'checking here', '');