package com.cgi.cinema;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/{mapType}/{z}/{x}/{y}.{tileFormat}")
    public ResponseEntity<byte[]> proxyTile(
            @PathVariable String mapType,
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y,
            @PathVariable String tileFormat) {

        // Construct the original URL
        String tileUrl = String.format("https://tiles.maaamet.ee/tm/tms/1.0.0/%s/%d/%d/%d.%s",
                mapType, z, x, y, tileFormat); // Note: Maaamet uses negative Y

        // Fetch data from the external API
        ResponseEntity<byte[]> response = restTemplate.getForEntity(tileUrl, byte[].class);

        // Return the response as-is (image data)
        return ResponseEntity
                .status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}
