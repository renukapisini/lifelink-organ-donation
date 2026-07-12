package com.lifetrace.backend.service;

import com.lifetrace.backend.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class IpfsService {

    @Value("${pinata.api.key}")
    private String apiKey;

    @Value("${pinata.secret.key}")
    private String secretKey;

    private static final String PINATA_URL =
            "https://api.pinata.cloud/pinning/pinFileToIPFS";

    public String uploadFile(MultipartFile file) {

        try {
            String boundary = "----LifeTraceBoundary" + System.currentTimeMillis();

            URL url = new URL(PINATA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("pinata_api_key", apiKey);
            conn.setRequestProperty("pinata_secret_api_key", secretKey);

            OutputStream outputStream = conn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // File part
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(file.getOriginalFilename()).append("\"\r\n");
            writer.append("Content-Type: application/octet-stream\r\n\r\n");
            writer.flush();

            outputStream.write(file.getBytes());
            outputStream.flush();

            writer.append("\r\n");
            writer.append("--").append(boundary).append("--\r\n");
            writer.flush();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new BadRequestException("Pinata error: HTTP " + responseCode);
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            String json = response.toString();

            int start = json.indexOf("\"IpfsHash\":\"") + 12;
            int end = json.indexOf("\"", start);

            return json.substring(start, end);

        } catch (Exception e) {
            throw new BadRequestException("IPFS upload error: " + e.getMessage());
        }
    }
}