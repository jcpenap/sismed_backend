-- -----------------------------------------------------
-- Table file_type
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS sismed.file_type (
  id int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  description varchar(255) NULL,
  info varchar(255) NULL,
  status bool NOT NULL,
  CONSTRAINT file_type_pkey PRIMARY KEY (id));

-- -----------------------------------------------------
-- Data for file_type
-- -----------------------------------------------------
INSERT INTO sismed.file_type(
	description, info, status)
	VALUES ('VN', 'Venta', true);
	
INSERT INTO sismed.file_type(
	description, info, status)
	VALUES ('CM', 'Compra', true);
	
INSERT INTO sismed.file_type(
	description, info, status)
	VALUES ('RC', 'Recobro', true);