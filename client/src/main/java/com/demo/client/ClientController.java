package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final WebClient webClient;

    @Autowired
    public ClientController(WebClient.Builder webClientBuilder) {
        // WebClient is used to make REST API calls to other services
        this.webClient = webClientBuilder
                .baseUrl("https://localhost:8443")  // The server's base URL
                .build();
    }

    @GetMapping("/client-call")
    public String callServer() {
        System.out.println("client-call");
        try {
            // Call the server's /api/hello endpoint
            Mono<String> response = this.webClient.get()
                    .uri("/api/hello")  // The server's specific endpoint
                    .retrieve()
                    .bodyToMono(String.class);  // Get the response body as a String

//            return response.block();  // Block to wait for the response synchronously\
            System.out.println("response:"+response.block());
            return "dong";
        } catch (WebClientResponseException e) {
            // Handle errors in the response
            return "Error: " + e.getStatusCode();
        } catch (Exception e) {
            // Handle other exceptions
            return "Error occurred while calling the server: " + e.getMessage();
        }
    }
}
