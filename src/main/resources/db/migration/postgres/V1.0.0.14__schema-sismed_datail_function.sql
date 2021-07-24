-- FUNCTION: sismed.sismed_detaill(date, date, integer)

-- DROP FUNCTION sismed.sismed_detaill(date, date, integer);

CREATE OR REPLACE FUNCTION sismed.sismed_detaill(
	initial_date date,
	finish_date date,
	file_transaction_type integer)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN

	UPDATE sismed.detail_imported_file SET
		invoice_month = DATE_PART('month',invoice_date)
	WHERE detail_imported_file.invoice_month IS NULL;
	
	DELETE FROM sismed.sismed_report
		WHERE sismed_report.initial_date = $1
			AND sismed_report.finish_date = $2
			AND sismed_report.file_transaction_type = $3;
	
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
			,initial_date
			,finish_date
			,file_transaction_type
		FROM sismed.imported_file
		INNER JOIN sismed.detail_imported_file AS dif
			ON dif.imported_file_id = sismed.imported_file.id
		INNER JOIN sismed.user AS u
			ON u.id = imported_file.user_id
		INNER JOIN sismed.actor
			ON actor.id = u.actor_id
		INNER JOIN sismed.file_type
			ON file_type.id = sismed.imported_file.file_type_id
		WHERE imported_file.file_type_id = file_transaction_type
			AND DATE_PART('year',invoice_date) = DATE_PART('year',finish_date)
			AND (invoice_date >= initial_date AND invoice_date <= finish_date)
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

ALTER FUNCTION sismed.sismed_detaill(date, date, integer)
    OWNER TO postgres;
