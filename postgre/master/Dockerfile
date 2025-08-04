FROM postgres:14.5

COPY scripts/ /docker-entrypoint-initdb.d/

RUN chmod +x /docker-entrypoint-initdb.d/pg_hba.sh
