CREATE OR REPLACE FUNCTION sismed.clean_sismed(
	)
    RETURNS void
    LANGUAGE 'sql'
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DELETE FROM sismed.errors;
DELETE FROM sismed.sismed_report;
DELETE FROM sismed.detail_imported_file;
DELETE FROM sismed.imported_file;
$BODY$;

ALTER FUNCTION sismed.clean_sismed()
    OWNER TO postgres;