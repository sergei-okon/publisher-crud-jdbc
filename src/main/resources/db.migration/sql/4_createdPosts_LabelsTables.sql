CREATE TABLE IF NOT EXISTS public.posts_labels
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    post_id bigint NOT NULL,
    label_id bigint NOT NULL,
    CONSTRAINT posts_labels_pkey PRIMARY KEY (id),
    CONSTRAINT labels FOREIGN KEY (label_id)
        REFERENCES public.labels (label_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT posts FOREIGN KEY (post_id)
        REFERENCES public.posts (post_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.posts_labels
    OWNER to postgres;