package com.ttn.ecommerce.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void uploadImage(String path, MultipartFile file, String id) throws IOException;



    String downloadImage(String path, String fileName);
}
