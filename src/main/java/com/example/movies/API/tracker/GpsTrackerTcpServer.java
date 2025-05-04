package com.example.movies.API.tracker;

import com.example.movies.API.service.GpsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.io.IOException; 
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GpsTrackerTcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GpsService.class);

    @Value("${tracker.tcp.port:6100}")
    private int port;

    private final GpsService gpsService;

    public GpsTrackerTcpServer(GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Thread t = new Thread(this::runServer, "gps-tcp-server");
        t.setDaemon(true);
        t.start();
    }

    private void runServer() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("GPS TCP Server listening on port " + port);
            while (true) {
                Socket client = server.accept();
                handleClient(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   private void handleClient(Socket client) {
  try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
    String chunk;
    while ((chunk = reader.readLine()) != null) {
      LOGGER.debug("[TCP] received chunk: “{}”", chunk);

      // if it contains the "BR" (real-time GPS) marker, hand it off
      if (chunk.contains("BR")) {
        gpsService.parseAndSave(chunk);
      }
    }
  } catch (Exception e) {
    LOGGER.error("TCP error handling client", e);
  } finally {
    try { client.close(); } catch (IOException ignored) {}
  }
}


}
