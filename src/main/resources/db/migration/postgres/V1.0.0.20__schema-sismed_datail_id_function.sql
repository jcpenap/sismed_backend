-- FUNCTION: sismed.sismed_detail_string(character varying, character varying, integer)

-- DROP FUNCTION sismed.sismed_detail_string(character varying, character varying, integer);

CREATE OR REPLACE FUNCTION sismed.sismed_detail_id(
	register_id integer)
    RETURNS boolean
    LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    v_initial_date date; 
	v_finish_date date; 
	v_file_transaction_type integer;

BEGIN

	SELECT log_report.initial_date INTO v_initial_date FROM sismed.log_report WHERE log_report.id = register_id;
	SELECT log_report.finish_date INTO v_finish_date FROM sismed.log_report WHERE log_report.id = register_id;
	SELECT log_report.file_type INTO v_file_transaction_type FROM sismed.log_report WHERE log_report.id = register_id;

	UPDATE sismed.detail_imported_file SET
		invoice_month = DATE_PART('month',invoice_date)
	WHERE detail_imported_file.invoice_month IS NULL;
	
	DELETE FROM sismed.sismed_report
		WHERE sismed_report.initial_date = v_initial_date
			AND sismed_report.finish_date = v_finish_date
			AND sismed_report.file_transaction_type = v_file_transaction_type;
	
	INSERT INTO sismed.sismed_report(
		reg, 
		rownumber, 
		avaliability_code, 
		invoice_month, 
		role_actor, 
		description, 
		transaction_type, 
		first_ium, 
		second_ium, 
		third_ium, 
		expedient, 
		consecutive, 
		unit_invoice, 
		min_price, 
		max_price, 
		total_price, 
		total_units, 
		min_invoice, 
		max_invoice, 
		total_meddicine,
		initial_date,
		finish_date,
		file_transaction_type
	)
	SELECT 2
			,ROW_NUMBER() OVER(PARTITION BY u.avaliability_code)
			,u.avaliability_code
			,LPAD(dif.invoice_month::text, 2, '0') AS invoice_month
			,actor.name as role_actor
			,file_type.description
			,dif.transaction_type
			,dif.first_ium
			,dif.second_ium
			,dif.third_ium
			,dif.expedient
			,dif.consecutive
			,dif.unit_invoice
			,MIN(dif.unit_value) as min_price
			,MAX(dif.unit_value) max_price
			,SUM(dif.unit_value*amount) total_price
			,SUM(dif.amount) as total_units
			,(SELECT f.invoice_number FROM sismed.detail_imported_file AS f
				WHERE f.expedient = dif.expedient
					AND f.consecutive = dif.consecutive
					AND f.invoice_month = dif.invoice_month
				ORDER BY f.unit_value ASC
				LIMIT 1) as min_invoice
			,(SELECT f.invoice_number FROM sismed.detail_imported_file AS f
				WHERE f.expedient = dif.expedient
					AND f.consecutive = dif.consecutive
					AND f.invoice_month = dif.invoice_month
				ORDER BY f.unit_value DESC
				LIMIT 1) as max_invoice
			,((SELECT COUNT(*)
				FROM (SELECT DISTINCT d.consecutive, d.expedient, d.first_ium, d.second_ium, d.third_ium 
						FROM sismed.detail_imported_file AS d) AS dr)) AS mediccine_amount
			,v_initial_date
			,v_finish_date
			,v_file_transaction_type
		FROM sismed.imported_file
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = sismed.imported_file.id
		INNER JOIN sismed.user AS u
			ON u.id = imported_file.user_id
		INNER JOIN actor
			ON actor.id = u.actor_id
		INNER JOIN file_type
			ON file_type.id = sismed.imported_file.file_type_id
		WHERE imported_file.file_type_id = v_file_transaction_type
			AND DATE_PART('year',invoice_date) = DATE_PART('year',v_finish_date)
			AND (invoice_date >= v_initial_date AND invoice_date <= v_finish_date)
			AND imported_file.is_valid = true
		GROUP BY u.avaliability_code
			,dif.invoice_month
			,actor.name
			,file_type.description
			,dif.transaction_type
			,dif.first_ium
			,dif.second_ium
			,dif.third_ium
			,dif.expedient
			,dif.consecutive
			,dif.unit_invoice;
			RETURN true;
	END
$BODY$;

ALTER FUNCTION sismed.sismed_detail_string(character varying, character varying, integer)
    OWNER TO postgres;
