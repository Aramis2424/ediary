FROM postgres:14.5

RUN apt-get update && apt-get install -y postgresql-client

CMD bash -c "until pg_isready -h db-master -p 5432; do sleep 2; done && \
  export PGPASSWORD=replica2424 && \
  rm -rf /var/lib/postgresql/data/* && \
  pg_basebackup -h db-master -D /var/lib/postgresql/data -U replica -Fp -Xs -P -R && \
  echo \"primary_conninfo = 'host=db-master port=5432 user=replica password=replica2424'\" >> /var/lib/postgresql/data/postgresql.auto.conf && \
  chown -R postgres:postgres /var/lib/postgresql/data && \
  docker-entrypoint.sh postgres"
