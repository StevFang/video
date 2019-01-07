create table Menu(
	oid bigint primary key,
	parentId bigint,
	Code varchar(50),
	Name varchar(100),
	HierarchyPath varchar(255),
	MenuType varchar(10),
	NeedLink varchar(1),
	LinkUrl varchar(255),
	RootMenuId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;