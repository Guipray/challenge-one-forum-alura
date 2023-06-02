alter table cursos add column ativo tinyint not null;
update cursos set ativo = 1;
