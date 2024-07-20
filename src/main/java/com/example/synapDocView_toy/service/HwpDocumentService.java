package com.example.synapDocView_toy.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class HwpDocumentService implements DocumentService {

    private static final String UPLOAD_DIR = "src/main/resources/uploads/hwp/";

    @Override
    public String uploadDocument(MultipartFile file) throws Exception {
        String[] rmAllFileCmd = {"cmd.exe", "/c", "del", "*.hwp"}; // del *.* -> 프리징 현상 *.hwp로 대체
        String[] rmAllFolderCmd = {"cmd.exe", "/c", "for", "/d", "%p", "in", "(*)", "do", "rmdir", "/s", "/q", "\"%p\""};
        runCommand(rmAllFileCmd, UPLOAD_DIR);
        runCommand(rmAllFolderCmd, UPLOAD_DIR); // for /d %p in (*) do rmdir /s /q "%p"

        String filename = UUID.randomUUID().toString() + ".hwp";
        Path hwpPath = Paths.get(UPLOAD_DIR + filename);
        Files.createDirectories(hwpPath.getParent());
        System.out.println("hwpPat.getParent(): " + hwpPath.getParent().toString());
        System.out.println("filename: " + filename);
        Files.write(hwpPath, file.getBytes());

        String xhtmlFilename = convertHwp2Xhtml(filename);

        return xhtmlFilename;
    }

    @Override
    public Path getDocumentPath(String filename) {
        return Paths.get(UPLOAD_DIR + filename);
    }

    private String convertHwp2Xhtml(String filename) throws IOException {
        // hwp -> xhtml 변환
        String[] installPyhwpCmd = {"pip", "install", "pyhwp"};
        String[] convertCmd = {"cmd.exe", "/c", "hwp5html", filename};
        runCommand(installPyhwpCmd, UPLOAD_DIR);
        runCommand(convertCmd, UPLOAD_DIR);

        // UPLOAD_DIR로 파일 이동 -> localhost:8081/uploads/hwp/index.xhtml 요청을 위해서
        String fileBaseName = FilenameUtils.getBaseName(filename);
        String[] moveCmd1 = {"cmd.exe", "/c", "move", fileBaseName + "\\index.xhtml", "."};
        String[] moveCmd2 = {"cmd.exe", "/c", "move", fileBaseName + "\\bindata", "."};
        String[] moveCmd3 = {"cmd.exe", "/c", "move", fileBaseName + "\\styles.css", "."};
        runCommand(moveCmd1, UPLOAD_DIR);
        runCommand(moveCmd2, UPLOAD_DIR);
        runCommand(moveCmd3, UPLOAD_DIR);

        return filename;
    }

    private void runCommand(String[] command, String executeDir) throws IOException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(executeDir));

            Process process = processBuilder.start();

            // 표준 출력 처리
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 프로세스 종료 및 결과 출력
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);
            for (String cmd : command) {
                System.out.print(cmd + " ");
            }
            System.out.println(" output:\n" + result.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}