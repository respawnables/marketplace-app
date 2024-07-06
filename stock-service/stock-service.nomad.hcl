job "stock-service" {
  datacenters = ["dc1"]

  group "stock-db-group" {
    count = 1
    network {
      mode = "host"
      port "db" {
        to = 5432
      }
    }
    service {
      name = "stock-db"
      port = "db"
      provider = "consul"
    }
    task "stock-db" {
      driver = "docker"
      config {
        image = "postgres:15"
        ports = ["db"]
        network_mode = "host"
      }
      env {
        POSTGRES_DB = "stockdb"
        POSTGRES_USER = "stock-service"
        POSTGRES_PASSWORD = "stock-service"
      }
      resources {
        cores = 1
        memory = 512
      }
    }
  }

  group "stock-service-group" {
    count = 1
    network {
      mode = "host"
      port "app" {}
    }
    service {
      name = "stock-service"
      port = "app"
      provider = "consul"
    }
    task "stock-service" {
      driver = "docker"
      config {
        image = "respawnables/stock-service:latest"
        ports = ["app"]
        network_mode = "host"
      }
      env {
        SERVER_PORT = "${NOMAD_PORT_app}"
        SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/stockdb"
        SPRING_DATASOURCE_USERNAME = "stock-service"
        SPRING_DATASOURCE_PASSWORD = "stock-service"
      }
      resources {
        cores = 1
        memory = 1024
      }
    }
  }
}
