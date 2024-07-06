job "notification-service" {
  datacenters = ["dc1"]

  group "notification-service-group" {
    count = 1

    network {
      mode = "host"
      port "app" {}
    }

    service {
      name = "notification-service"
      port = "app"
      provider = "consul"
    }

    task "notification-service" {
      driver = "docker"

      config {
        image = "respawnables/notification-service:latest"
        ports = ["app"]
        network_mode = "host"
      }

      env {
        SERVER_PORT = "${NOMAD_PORT_app}"
      }

      resources {
        cores = 1
        memory = 1024
      }
    }
  }
}
