job "order-service" {
  datacenters = ["dc1"]

  group "order-db-group" {
    count = 1

    network {
      mode = "host"
      port "db" {
        to = 3306
      }
    }

    service {
      name = "order-service-db"
      port = "db"
      provider = "consul"
    }

    task "order-db" {
      driver = "docker"

      config {
        image = "mysql:8.4"
        ports = ["db"]
        network_mode = "host"
      }

      env {
        MYSQL_ROOT_PASSWORD = "order-service"
        MYSQL_PASSWORD = "order-service"
        MYSQL_USER = "mysql-user"
        MYSQL_DATABASE = "orderdb"
      }

      resources {
        cores = 1
        memory = 512
      }
    }
  }

  group "order-service-group" {
    count = 1

    network {
      mode = "host"
      port "app" {}
    }

    service {
      name = "order-service"
      port = "app"
      provider = "consul"
    }

    task "order-service" {
      driver = "docker"

      config {
        image = "respawnables/order-service:latest"
        ports = ["app"]
        network_mode = "host"
      }

      env {
        SERVER_PORT = "${NOMAD_PORT_app}"
        SPRING_DATASOURCE_URL = "jdbc:mysql://127.0.0.1:3306/orderdb"
        SPRING_DATASOURCE_USERNAME = "mysql-user"
        SPRING_DATASOURCE_PASSWORD = "order-service"
      }

      resources {
        cores = 1
        memory = 1024
      }
    }
  }
}
