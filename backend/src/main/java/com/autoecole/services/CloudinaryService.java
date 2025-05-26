package com.autoecole.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@Service
public class CloudinaryService {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadFile(MultipartFile file) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            logger.info("Starting file upload for: {}", originalFilename);
            
            if (originalFilename != null && !isAllowedFileType(originalFilename)) {
                throw new IOException("Invalid file type. Only images and PDFs are allowed.");
            }

            Map<String, Object> options = new HashMap<>();
            // Do NOT set resource_type for PDFs; let Cloudinary treat it as an image
            // This will generate an image (PNG/JPG) of the first page of the PDF
            // if (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf")) {
            //     options.put("resource_type", "raw");
            //     String baseName = originalFilename;
            //     if (!baseName.toLowerCase().endsWith(".pdf")) {
            //         baseName = baseName + ".pdf";
            //     }
            //     options.put("public_id", baseName);
            // }
            
            Map result = cloudinary.uploader().upload(file.getBytes(), options);
            logger.info("File uploaded successfully to Cloudinary: {}", result);
            return result;
            
        } catch (Exception e) {
            logger.error("Error uploading file to Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }
    
    private boolean isAllowedFileType(String filename) {
        String lowerFilename = filename.toLowerCase();
        // Allow only images and PDFs
        return lowerFilename.endsWith(".jpg") || 
               lowerFilename.endsWith(".jpeg") || 
               lowerFilename.endsWith(".png") || 
               lowerFilename.endsWith(".gif") || 
               lowerFilename.endsWith(".pdf");
    }
} 