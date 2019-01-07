CREATE TABLE User (
  oid  bigint primary key,
	Code varchar(50),
  Name varchar(100),
  IsActive varchar(1),
	EmployeeId bigint,
	OpenAccount varchar(1),
	Account varchar(50),
	Password varchar(100),
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint,
  UNIQUE INDEX `idx_oid` (`oid`) USING BTREE
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8;
