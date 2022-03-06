#!/bin/bash

pg_dump -h localhost -p 5432 -d dealfinder -U postgres -s --file pg_backup.sql