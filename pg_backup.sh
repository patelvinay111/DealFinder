#!/bin/bash

pg_dump --column-inserts -h localhost -p 5432 -d dealfinder -U postgres --format plain --file pg_backup.sql
