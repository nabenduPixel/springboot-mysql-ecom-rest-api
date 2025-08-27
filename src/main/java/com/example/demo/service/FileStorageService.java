package com.example.demo.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class FileStorageService {

    private static final String BUCKET_NAME = "blog-4c826.appspot.com"; // Use your correct Firebase Storage bucket name

    public String uploadImage(MultipartFile file) throws IOException {
        // Get the Firebase Storage client
        StorageClient storageClient = StorageClient.getInstance();
        
        // Get the bucket by its name directly
        Bucket bucket = storageClient.bucket(BUCKET_NAME);

        // Generate a unique filename
        String fileName = "uploads/category/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Upload the file to Firebase Storage
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

        // Make the file publicly accessible
        blob.createAcl(com.google.cloud.storage.Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(), com.google.cloud.storage.Acl.Role.READER));

        // Return the public URL
        return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), fileName);
    }

}
