create table DownloadRecord(
	oid bigint primary key,
	parentId bigint,
	Code varchar(50),
	UploadRecordId varchar(200),
	DownloadFlag varchar(1),
	DownloadMsg varchar(200),
	createdOn datetime,
	createdBy bigint,
	updatedOn datetime,
	updatedBy bigint
)
ENGINE = INNODB
DEFAULT CHARACTER SET=utf8;