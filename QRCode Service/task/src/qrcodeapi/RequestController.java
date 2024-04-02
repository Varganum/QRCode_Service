package qrcodeapi;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

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
    public ResponseEntity<Object> getImage(@RequestParam Optional<String> contents,
                                            @RequestParam Optional<String> correction,
                                            @RequestParam Optional<Integer> size,
                                           @RequestParam Optional<String> type) {

        String contentData;

        //check if contents is got and convert it to the String
        if (contents.isPresent() && !contents.get().isBlank()) {
            contentData = contents.get();
        } else {
            //otherwise return BAD REQUEST with Error
            return ResponseEntity.
                    badRequest().body(getIncorrectContentsError());
        }


        int imageSize = size.orElse(QR_IMAGE_DEFAULT_SIZE);

        //check if image size is in a reasonable range
        if (imageSize < QR_IMAGE_MIN_SIZE || imageSize > QR_IMAGE_MAX_SIZE) {
            //otherwise return BAD REQUEST with Error
            return ResponseEntity.
                    badRequest().body(getIncorrectImageSizeError());
        }


        String correctionLevel = correction.orElse("L").toLowerCase();

        //check if correction level is valid
        if (!correctionLevel.matches("[lmqh]")) {
            //otherwise return BAD REQUEST with Error
            return ResponseEntity.badRequest().body(getIncorrectCorrectionLevelError());
        }
        ErrorCorrectionLevel errorCorrectionLevel;
        switch (correctionLevel) {
            case "m" -> errorCorrectionLevel = ErrorCorrectionLevel.M;
            case "q" -> errorCorrectionLevel = ErrorCorrectionLevel.Q;
            case "h" -> errorCorrectionLevel = ErrorCorrectionLevel.H;
            default -> errorCorrectionLevel = ErrorCorrectionLevel.L;
        }


        String imageFormat = type.orElse("png");

        //check if image format is valid
        if (!imageFormat.equals("jpeg") & !imageFormat.equals("png") & !imageFormat.equals("gif")) {
            //otherwise return BAD REQUEST with Error
            return ResponseEntity.
                    badRequest().body(getIncorrectImageFormatError());
        }


        //generate default white square QR code
        BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageSize, imageSize);

        //generate real QR code with requested parameters and data
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        try {
            BitMatrix bitMatrix = writer.encode(contentData, BarcodeFormat.QR_CODE, imageSize, imageSize, hints);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            // handle the WriterException
            e.printStackTrace();
        }



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

    private Object getIncorrectCorrectionLevelError() {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Permitted error correction levels are L, M, Q, H");
        return body;
    }

    private Object getIncorrectContentsError() {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Contents cannot be null or blank");
        return body;
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