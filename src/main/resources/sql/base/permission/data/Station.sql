create table Station(
	oid bigint primary key,
	parentId bigint,
	Code varchar(50),
	Name varchar(100),
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;