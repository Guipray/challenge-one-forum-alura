alter table usuarios add column ativo tinyint;
update usuarios set ativo = 1;
alter table usuarios modify ativo tinyint not null;