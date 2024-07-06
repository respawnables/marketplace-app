#!/bin/bash

# Kafka'yı Docker Compose ile başlat
sudo docker-compose -f kafka.yml up -d

# Servis klasörleri
services=("stock-service" "order-service" "notification-service")

# Her bir servis klasöründeki deploy.sh betiğini çalıştır
for service in "${services[@]}"
do
  echo "Deploying $service..."
  (cd "$service" && ./deploy.sh)
done

echo "Deployment completed."
