create table UserRole(
	oid bigint primary key,
	parentId bigint,
	UserId bigint,
	RoleId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;