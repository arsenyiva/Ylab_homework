CREATE SCHEMA liquibase_schema;


ALTER DATABASE mydatabase SET search_path TO liquibase_schema,public;
