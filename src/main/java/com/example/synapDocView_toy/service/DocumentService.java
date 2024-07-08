package com.example.synapDocView_toy.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface DocumentService {
    String uploadDocument(MultipartFile file) throws Exception;
    Path getDocumentPath(String fileName);
}
