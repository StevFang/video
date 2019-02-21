create table video(
  oid bigint primary key,
	parentId bigint,
	OriginName varchar(50),
	UploadRecordId bigint,
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;