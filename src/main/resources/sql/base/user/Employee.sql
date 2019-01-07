CREATE TABLE Employee (
  oid  bigint primary key,
	Code varchar(50),
  Name varchar(100),
  IsActive varchar(1),
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint,
  UNIQUE INDEX `idx_oid` (`oid`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8;
