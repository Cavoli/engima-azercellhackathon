package com.example.demo.client;

import com.example.demo.model.client.QuillBotResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * We had to scrap that client because service just decided not work in middle of the day
 * We dec
 */
@Service
public class QuillBotClient {

    private final WebClient.Builder webClientBuilder;
    private static final String BASE_URL = "https://rest.quillbot.com/api/summarizer/summarize-para/abs";

    public QuillBotClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public QuillBotResponse reponse(String text) throws WebClientResponseException {


        QuillBotResponse block = webClientBuilder.build()
                .post()
                .uri(BASE_URL)
                .headers(this::getDefaultHeaders)
                .bodyValue(text)
                .retrieve()
                .bodyToMono(QuillBotResponse.class).block();
        return block;
    }

    private void getDefaultHeaders(HttpHeaders headers){

        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
        headers.add("api-key", "ff812953-494f-4102-ac7a-50fd09d0a2ed");
    }
}
