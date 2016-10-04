CREATE TABLE public.users (
  id BIGINT NOT NULL,
  login VARCHAR(100) NOT NULL,
  name VARCHAR(255),
  CONSTRAINT table_login_key UNIQUE(login),
  CONSTRAINT table_pkey PRIMARY KEY(id)
) 
WITH (oids = false);
commit;

insert into public.users (id, login,name)
values(1,'user1','name for user1');
insert into public.users(id, login,name)
values(2,'user2','name for user2');
commit;