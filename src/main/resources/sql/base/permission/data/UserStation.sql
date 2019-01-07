create table UserStation(
	oid bigint primary key,
	parentId bigint,
	UserId bigint,
	StationId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;