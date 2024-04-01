package qrcodeapi;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class RequestController {

    private static final int QR_IMAGE_DEFAULT_SIZE = 250;

    private static final int QR_IMAGE_MIN_SIZE = 150;
    private static final int QR_IMAGE_MAX_SIZE = 350;


    @GetMapping("/api/health")
    public ResponseEntity<Object> checkConnection() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<Object> getImage(@RequestParam Optional<Integer> size,
                                           @RequestParam Optional<String> type) {

        int imageSize = size.orElseGet(() -> QR_IMAGE_DEFAULT_SIZE);

        if (imageSize < QR_IMAGE_MIN_SIZE || imageSize > QR_IMAGE_MAX_SIZE) {
            return ResponseEntity.
                    badRequest().body(getIncorrectImageSizeError());
        }

        String imageFormat = type.orElseGet(() ->"png");

        if (!imageFormat.equals("jpeg") & !imageFormat.equals("png") & !imageFormat.equals("gif")) {
            return ResponseEntity.
                    badRequest().body(getIncorrectImageFormatError());
        }

        BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageSize, imageSize);

        MediaType imageMediaType;

        switch (imageFormat) {
            case "jpeg" -> imageMediaType = MediaType.IMAGE_JPEG;
            case "gif" -> imageMediaType = MediaType.IMAGE_GIF;
            default -> imageMediaType = MediaType.IMAGE_PNG;
        }

        return ResponseEntity
                .ok()
                .contentType(imageMediaType)
                .body(bufferedImage);
    }


    private Object getIncorrectImageFormatError() {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Only png, jpeg and gif image types are supported");
        return body;
    }

    private Object getIncorrectImageSizeError() {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Image size must be between 150 and 350 pixels");
        return body;
    }


}