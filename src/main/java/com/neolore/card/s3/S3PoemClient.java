package com.neolore.card.s3;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class S3PoemClient {
    private final S3Client s3Client;

    S3PoemClient(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String readFile() throws IOException {
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                request -> request.bucket("abd401-first-bucket").key("poem.txt"));

        String fileContent = StreamUtils.copyToString(response, StandardCharsets.UTF_8);

        System.out.println(fileContent);
        return fileContent;
    }
}