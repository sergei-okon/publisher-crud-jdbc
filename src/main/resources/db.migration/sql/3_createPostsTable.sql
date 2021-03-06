CREATE TABLE IF NOT EXISTS public.posts
(
    post_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    content character varying(100) COLLATE pg_catalog."default" NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone NOT NULL,
    CONSTRAINT posts_pkey PRIMARY KEY (post_id)
)
    TABLESPACE pg_default;
