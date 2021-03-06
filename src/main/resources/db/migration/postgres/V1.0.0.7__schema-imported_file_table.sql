-- -----------------------------------------------------
-- Table imported_file
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS sismed.imported_file (
  id int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  create_time timestamp(0) NULL,
  "name" varchar(255) NULL,
  file_type_id int4 NOT NULL,
  user_id int4 NOT NULL,
  is_valid boolean NULL,
  CONSTRAINT imported_file_pkey PRIMARY KEY (id),
  CONSTRAINT imported_file_user_FK FOREIGN KEY (user_id) REFERENCES sismed.user (id),
  CONSTRAINT imported_file_file_type_FK FOREIGN KEY (file_type_id) REFERENCES sismed.file_type (id));