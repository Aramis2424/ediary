FROM postgres:14.5

COPY scripts/pg_hba.sh /docker-entrypoint-initdb.d/

RUN chmod +x /docker-entrypoint-initdb.d/pg_hba.sh
