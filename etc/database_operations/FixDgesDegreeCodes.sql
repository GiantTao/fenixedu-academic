
SET AUTOCOMMIT = 0;
START TRANSACTION;

UPDATE DEGREE SET CODE = '9508', MINISTRY_CODE = '9508' WHERE ID_INTERNAL = '2379';
UPDATE DEGREE SET MINISTRY_CODE = CODE;
ALTER TABLE DEGREE DROP COLUMN CODE;

COMMIT;
