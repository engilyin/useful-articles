CREATE SEQUENCE users_id_seq INCREMENT 1 START 1;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigint NOT NULL PRIMARY KEY DEFAULT nextval('users_id_seq') ,
    username character varying(100) NOT NULL,
    password text NOT NULL,
    fullname text NOT NULL,
    role character varying(40),
    created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    locked boolean NOT NULL
);
ALTER SEQUENCE users_id_seq OWNED BY public.users.user_id;

CREATE UNIQUE INDEX users_username_idx ON public.users (username);
CREATE INDEX users_fullname_idx ON public.users (fullname);



CREATE SEQUENCE articles_id_seq INCREMENT 1 START 1;


CREATE TABLE IF NOT EXISTS public.articles
(
    article_id bigint NOT NULL PRIMARY KEY DEFAULT nextval('articles_id_seq'),
    article_name character varying(255) NOT NULL,
    author_id bigint NOT NULL,
    article_description text NOT NULL,
    article_attachment text NOT NULL,
    article_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT articles_author_fk1 FOREIGN KEY (author_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED
);

ALTER SEQUENCE articles_id_seq OWNED BY public.articles.article_id;

CREATE UNIQUE INDEX articles_article_name_idx ON public.articles (article_name);


CREATE SEQUENCE comments_id_seq INCREMENT 1 START 1;

CREATE TABLE IF NOT EXISTS public.comments
(
    comment_id bigint NOT NULL PRIMARY KEY DEFAULT nextval('comments_id_seq'),
    article_id bigint NOT NULL,
    author_id bigint NOT NULL,
    comment text NOT NULL,
    comment_attachment text NOT NULL,
    comment_created timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT comments_article_fk1 FOREIGN KEY (article_id)
    REFERENCES public.articles (article_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED,
    
    CONSTRAINT author_fk2 FOREIGN KEY (author_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED
);

ALTER SEQUENCE comments_id_seq OWNED BY public.comments.comment_id;
