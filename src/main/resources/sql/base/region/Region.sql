create table Region(
	oid bigint primary key,
	parentId bigint,
	Code varchar(50),
	Name varchar(100),
	HierarchyPath varchar(255),
	RegionType varchar(10),
	RootRegionId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;