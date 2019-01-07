create table StationOrg(
	oid bigint primary key,
	parentId bigint,
	StationId bigint,
	RangeType varchar(20),
	OrgId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;