/* SQL query to kill all active connections to encrypted diary database and drop it */

Select concat('KILL ',id,';') from information_schema.processlist where user='user'; /* I don't think this is necessary, but I'm keeping it regardless */
DROP DATABASE ENCRYPTED_DIARY_DB;