-- Create the Application Database if it doesn't exist
SELECT 'CREATE DATABASE ppdtcore'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'ppdtcore')\gexec
