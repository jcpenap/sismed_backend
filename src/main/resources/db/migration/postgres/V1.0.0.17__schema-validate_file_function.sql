-- FUNCTION: sismed.validate_file(integer)

-- DROP FUNCTION sismed.validate_file(integer);

CREATE OR REPLACE FUNCTION sismed.validate_file(
	file_id_evaluated integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
   count_errors integer := 0; 
   
BEGIN

	DELETE FROM sismed.errors WHERE errors.file_id = file_id_evaluated;

	UPDATE sismed.detail_imported_file AS d SET
	line = tmp.seq
	FROM (SELECT tm.*, row_number() over () as seq FROM sismed.detail_imported_file AS tm 
		   WHERE tm.imported_file_id = file_id_evaluated ORDER BY tm.id ASC) AS tmp
	WHERE d.id = tmp.id
		AND d.imported_file_id = file_id_evaluated;

	INSERT INTO sismed.errors(messa, rownumber, file_id)
		--Validacion de valores nulos
		SELECT 'El valor de la cantidad no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.amount IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del expediente no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.consecutive IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del expediente no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.expedient IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del primer IUM no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.first_ium IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor de la fecha de la factura  no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.invoice_date IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del numero de la factura no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.invoice_number IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del nit no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.nit IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del segundo IUM no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.second_ium IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del tercer IUM no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.third_ium IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor del tipo de transaccion no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.transaction_type IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor de la unidad de medida no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.unit_invoice IS NULL
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor unitario no es valido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.unit_value IS NULL
			AND i.id = file_id_evaluated
		UNION
		--Validacion de contenidos
		SELECT 'Columna Tipo de transaccion no permitido'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id
		WHERE NOT EXISTS (SELECT transaction_type.name FROM sismed.transaction_type WHERE dif.transaction_type = transaction_type.name)
			AND i.id = file_id_evaluated
		UNION
		SELECT 'Columna unidad de la Factura no permitida' 
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id
		WHERE NOT EXISTS (SELECT unit_invoice.description FROM sismed.unit_invoice WHERE dif.unit_invoice = unit_invoice.description)
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor unitario debe ser mayor a 0'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.unit_value <= 0
			AND i.id = file_id_evaluated
		UNION
		SELECT 'El valor de la cantidad debe ser mayor a 0'
			,dif.line
			,file_id_evaluated
		FROM sismed.imported_file AS i
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = i.id	
		WHERE dif.amount <= 0
			AND i.id = file_id_evaluated;
			
		SELECT COUNT(*) INTO count_errors FROM sismed.errors WHERE sismed.errors.file_id = file_id_evaluated;
		IF count_errors = 0 THEN
			UPDATE sismed.imported_file SET
				is_valid = true
			WHERE sismed.imported_file.id = file_id_evaluated;
		END IF;
		
		RETURN count_errors;
	END
$BODY$;

ALTER FUNCTION sismed.validate_file(integer)
    OWNER TO postgres;
