create table topicos(

    id bigint not null auto_increment,
    titulo varchar(255) not null,
    mensagem text not null,
    data_criacao datetime default current_timestamp,
    status ENUM('NAO_RESPONDIDO', 'NAO_SOLUCIONADO', 'SOLUCIONADO', 'FECHADO') not null,
	autor_id int,
	curso_id int,
	respostas text,
    
    primary key(id)

);