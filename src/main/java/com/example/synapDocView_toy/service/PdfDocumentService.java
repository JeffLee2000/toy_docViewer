package com.example.synapDocView_toy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PdfDocumentService implements DocumentService {

    private static final String UPLOAD_DIR = "uploads/pdf/";

    @Override
    public String uploadDocument(MultipartFile file) throws Exception {
        String filename = UUID.randomUUID().toString() + ".pdf";
        Path path = Paths.get(UPLOAD_DIR + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return filename;
    }

    @Override
    public Path getDocumentPath(String filename) {
        return Paths.get(UPLOAD_DIR + filename);
    }
}

