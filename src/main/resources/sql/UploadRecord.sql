create table UploadRecord(
	oid bigint primary key,
	parentId bigint,
	Code varchar(50),
	OriginName varchar(200),
	SaveName varchar(100),
	ExtName varchar(50),
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8;