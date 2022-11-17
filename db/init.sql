CREATE SEQUENCE users_id_seq INCREMENT 1 START 1;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL PRIMARY KEY DEFAULT nextval('users_id_seq') ,
    username character varying(100) NOT NULL,
    password text NOT NULL,
    name text NOT NULL,
    role character varying(40),
    created time with time zone,
    locked boolean NOT NULL
);
ALTER SEQUENCE users_id_seq OWNED BY public.users.id;

CREATE UNIQUE INDEX users_username_idx ON public.users (username);
CREATE INDEX users_name_idx ON public.users (name);



CREATE SEQUENCE articles_id_seq INCREMENT 1 START 1;


CREATE TABLE IF NOT EXISTS public.articles
(
    id bigint NOT NULL PRIMARY KEY DEFAULT nextval('articles_id_seq'),
    name character varying(255) NOT NULL,
    author_id bigint NOT NULL,
    description text NOT NULL,
    attachment text NOT NULL,
    created time with time zone,
    CONSTRAINT articles_author_fk1 FOREIGN KEY (author_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED
);

ALTER SEQUENCE articles_id_seq OWNED BY public.articles.id;

CREATE UNIQUE INDEX articles_name_idx ON public.articles (name);


CREATE SEQUENCE comments_id_seq INCREMENT 1 START 1;

CREATE TABLE IF NOT EXISTS public.comments
(
    id bigint NOT NULL PRIMARY KEY DEFAULT nextval('comments_id_seq'),
    article_id bigint NOT NULL,
    author_id bigint NOT NULL,
    comment text NOT NULL,
    attachment text NOT NULL,
    created time with time zone,
    
    CONSTRAINT comments_article_fk1 FOREIGN KEY (article_id)
    REFERENCES public.articles (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED,
    
    CONSTRAINT author_fk2 FOREIGN KEY (author_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED
);

ALTER SEQUENCE comments_id_seq OWNED BY public.comments.id;
