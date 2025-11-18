#!/bin/bash

echo "Realizando deploy local..."

# Buscar el jar generado
JAR=$(ls target/*.jar | head -n 1)

if [ -z "$JAR" ]; then
  echo "ERROR: No se encontró el archivo JAR para deploy."
  exit 1
fi

echo "JAR encontrado: $JAR"

# Matar instancia anterior si existe
echo "Terminando instancias anteriores..."
pkill -f "$JAR" || true

# Arrancar la aplicación en segundo plano
echo "Iniciando aplicación..."
nohup java -jar "$JAR" --server.port=8080 > deploy.log 2>&1 &

echo "Deploy completado. Log disponible en deploy.log"
