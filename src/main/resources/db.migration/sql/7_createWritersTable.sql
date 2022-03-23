CREATE TABLE IF NOT EXISTS public.writers
(
    writer_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    first_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT writers_pkey PRIMARY KEY (writer_id)
)
    TABLESPACE pg_default;
