INSERT INTO users(
    username, password, fullname, role, locked)
    VALUES ('test', 'testpass', 'Test user', 'generic', false);
    
    
INSERT INTO articles(
    article_name, author_id, article_description, article_attachment)
    VALUES ('first-article', 1, 'This is just a test article', '/user/te/test/2022/11/my-attach.mp3');
    
INSERT INTO articles(
    article_name, author_id, article_description, article_attachment)
    VALUES ('another-article', 1, 'Just yet another one', '/user/te/test/2022/11/second.avi');