package com.ttn.ecommerce.service.image;

import com.ttn.ecommerce.service.image.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${base.path}")
    private String basePath;

    @Override
    public void uploadImage(String path, MultipartFile file, String id) throws IOException {
        String name = file.getOriginalFilename();
        String extension = name.substring(name.lastIndexOf("."));
        if (extension.equals(".png") ||
                extension.equals(".jpg") ||
                extension.equals(".jpeg") ||
                extension.equals(".bmp")) {

            String fileName = id + name.substring(name.lastIndexOf("."));
            String filePath = basePath + path + File.separator + fileName;
            File f = new File(path);

            if (!f.exists()) {
                f.mkdir();
            }

            Files.copy(file.getInputStream(), Paths.get(filePath));
        }
    }

    @Override
    public String downloadImage(String path, String fileName) {
        File directory = new File(path);
        String[] flist = directory.list();
        for (String file : flist) {
            if (file.split("\\.")[0].equalsIgnoreCase(fileName)) {
                return basePath + path + file;
            }
        }
        return basePath + path + "default.png";
    }
}
