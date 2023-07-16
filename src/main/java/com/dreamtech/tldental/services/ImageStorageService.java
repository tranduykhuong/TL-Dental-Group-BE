package com.dreamtech.tldental.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ImageStorageService implements IStorageService {
    Cloudinary cloudinary;

    String cloudName = "646e61783174376d34";
    String apiKey = "393832343431313839353531313934";
    String apiSecret = "702d5a43534d356f63396e58614d534d574951596a5f5831615863";


    // constructor
    public ImageStorageService() throws DecoderException {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", new String(Hex.decodeHex(cloudName)),
                "api_key", new String(Hex.decodeHex(apiKey)),
                "api_secret", new String(Hex.decodeHex(apiSecret))));
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb");
            }

            Map c = this.cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            String imgPath = (String) c.get("secure_url");
            System.out.println(imgPath);

            return imgPath;
        }catch (IOException exception) {
            throw new RuntimeException("Failed to store file", exception);
        }
    }

    @Override
    public boolean deleteFile(String url) {
        String publicId = extractPublicIdFromUrl(url);
        try {
            this.cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return true;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to delete file", exception);
        }
    }


    private boolean isImageFile(MultipartFile file) {
        // Let install
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    private static String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String filename = parts[parts.length - 1];
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex != -1) {
            return filename.substring(0, extensionIndex);
        }
        return filename;
    }
}
