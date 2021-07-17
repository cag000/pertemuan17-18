CREATE table data_kontak(
	id_kontak int not null primary key,
	nama_kontak varchar(255),
	no_kontak varchar(255),
	alamat varchar(255)
);

alter table data_kontak add column status varchar(255);