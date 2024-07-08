package com.example.synapDocView_toy.controller;

import com.example.synapDocView_toy.service.DocumentService;
import com.example.synapDocView_toy.service.HwpDocumentService;
import com.example.synapDocView_toy.service.PdfDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class DocumentController {

    private final Map<String, DocumentService> documentServices;

    @Autowired
    public DocumentController(List<DocumentService> documentServiceList) {
        this.documentServices = documentServiceList.stream()
                .collect(Collectors.toMap(service -> {
                    if (service instanceof PdfDocumentService) return "pdf";
                    else if (service instanceof HwpDocumentService) return "hwp";
                    else throw new IllegalArgumentException("Unsupported service type");
                }, Function.identity()));
    }

    @PostMapping("/api/documents/upload")
    @ResponseBody
    public String uploadDocument(@RequestParam("file") MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String originFileExtension = StringUtils.getFilenameExtension(originFileName);

        DocumentService documentService = documentServices.get(originFileExtension.toLowerCase());
        if (documentService == null) {
            return "Unsupported file type";
        }

        try {
            return documentService.uploadDocument(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload file";
        }
    }

    @GetMapping("/api/documents/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        String fileExtension = StringUtils.getFilenameExtension(filename);
        DocumentService documentService = documentServices.get(fileExtension);

        if (documentService == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path path = documentService.getDocumentPath(filename);
            // System.out.println(path.toUri()); 2번 동작 됨 왜?
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/view")
    public String viewDocument(@RequestParam String filename, Model model) {
        String fileExtension = StringUtils.getFilenameExtension(filename);
        DocumentService documentService = documentServices.get(fileExtension);

        if (documentService == null) {
            return "Unsupported file type";
        }

        try {
            model.addAttribute("filename", filename);
            return "view";  // This should map to src/main/resources/templates/view.html
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to convert file";
        }
    }
}
