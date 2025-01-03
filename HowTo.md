# How To - Teckel Server

TODO: Describe how to run the project. IT NEEDS TO BE IMPROVED

## Start Spark Server Script

```bash
#!/bin/bash

# Ejecutar Spark Connect Server
/opt/spark/sbin/start-connect-server.sh --packages org.apache.spark:spark-connect_2.13:3.5.4 --conf spark.jars.ivy=/tmp/ivy/cache

# Mantener el contenedor en ejecución
tail -f /dev/null
```

## Run Docker with Spark and Teckel Server

```yaml
services:
  spark:
    image: eff3ct/spark:latest
    container_name: spark
    tty: true
    ports:
      - "7077:7077"  # Puerto para el Spark Master
      - "8080:8080"  # Puerto para la interfaz web de Spark Master
      - "15002:15002"  # Puerto para el servidor de Spark
    volumes:
      - ./app:/app  # Directorio de la aplicación
    environment:
      - SPARK_MODE=master  # Configura Spark como Master
      - SPARK_MASTER_HOST=spark
    entrypoint: ["/bin/bash" , "-c", "/app/start-spark-server.sh"]
    networks:
      - app-network

  teckel-server:
    image: eff3ct/teckel-server:latest
    container_name: teckel-server
    depends_on:
      - spark
    ports:
      - "1443:1443"  # Puerto de tu API
    environment:
      - SPARK_MASTER_URL=spark://spark:7077  # URL del Spark Master
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```
## POST API ETL

```API
curl -X POST http://localhost:1443/etl -H "Content-Type: application/json" -d '{
    "jobConfigRequest": {
        "url": "sc://localhost:15002"
    },
    "etl": {
        "input": [
            {
                "name": "table1",
                "format": "csv",
                "path": "/app/data/csv/example.csv",
                "options": {
                    "header": true,
                    "sep": "|"
                }
            }
        ],
        "output": [
            {
                "name": "table1",
                "format": "parquet",
                "mode": "overwrite",
                "path": "/tmp/data/parquet/example"
            }
        ]
    }
}'
```

## POST API DataFrame Test

```API
curl -X POST http://localhost:1443/dataframe -H "Content-Type: application/json" -d '{
  "jobConfigRequest": {
      "url": "sc://localhost:15002"
  }
}
