CREATE TABLE IF NOT EXISTS public.users
(
    username character varying(100) NOT NULL,
    password text COLLATE NOT NULL,
    name text NOT NULL,
    role character varying(40),
    created time with time zone,
    locked boolean NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (username)
)


CREATE TABLE IF NOT EXISTS public.articles
(
    article_id character varying(255) NOT NULL,
    author character varying(100) NOT NULL,
    description text NOT NULL,
    attachment text NOT NULL,
    created time with time zone,
    CONSTRAINT article_pkey PRIMARY KEY (article_id),
    CONSTRAINT author_fk1 FOREIGN KEY (author)
    REFERENCES public.users (username) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE INITIALLY DEFERRED
)