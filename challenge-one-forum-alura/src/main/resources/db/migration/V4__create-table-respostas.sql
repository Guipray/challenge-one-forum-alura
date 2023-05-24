create table respostas(

	id bigint not null auto_increment,
	mensagem text not null,
	topico_id int,
	data_criacao datetime default current_timestamp,
	autor_id int,
	solucao tinyint,
	
	primary key(id)
	
);
