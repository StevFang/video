create table RoleMenu(
	oid bigint primary key,
	parentId bigint,
	RoleId bigint,
	RangeType varchar(20),
	MenuId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;