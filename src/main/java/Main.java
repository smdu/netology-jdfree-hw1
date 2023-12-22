import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String apiKey = "3zpFnvE1aodH5Mvzwn1PfatS5KWvFBY3HWmFnXtX";
        String nasaUrl = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;

        ObjectMapper mapper = new ObjectMapper();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(
                RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofSeconds(5000))
                        .setRedirectsEnabled(false)
                        .build()
        ).build();
        HttpGet request = new HttpGet(nasaUrl);

        CloseableHttpResponse response = client.execute(request);

        NasaAnswer answer = mapper.readValue(response.getEntity().getContent(), NasaAnswer.class);

        // send
        CloseableHttpResponse image = client.execute(new HttpGet(answer.url));

        // read file
        String[] fileNameSplit = answer.url.split("/");
        String fileName = fileNameSplit[fileNameSplit.length - 1];
        FileOutputStream file = new FileOutputStream(fileName);
        image.getEntity().writeTo(file);
    }
}
