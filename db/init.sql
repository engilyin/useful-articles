CREATE TABLE IF NOT EXISTS public.users
(
    username character varying(100) NOT NULL,
    password text COLLATE NOT NULL,
    name text NOT NULL,
    role character varying(40),
    created time with time zone,
    locked boolean NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (username, locked)
)