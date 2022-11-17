INSERT INTO users(
    username, password, name, role, created, locked)
    VALUES ('test', 'testpass', 'Test user', 'generic', null, false);
    
    
INSERT INTO articles(
    name, author_id, description, attachment)
    VALUES ('first-article', 1, 'This is just a test article', '/user/te/test/2022/11/my-attach.mp3');
    
INSERT INTO articles(
    name, author_id, description, attachment)
    VALUES ('another-article', 1, 'Just yet another one', '/user/te/test/2022/11/second.avi');