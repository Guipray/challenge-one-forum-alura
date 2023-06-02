alter table respostas add column ativo tinyint not null;
update respostas set ativo = 1;
