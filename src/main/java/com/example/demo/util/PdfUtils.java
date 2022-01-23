package com.example.demo.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class PdfUtils {
    public static String PdfToString(MultipartFile multipartFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        File tempFile = File.createTempFile("text", ".pdf");
        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write(multipartFile.getBytes());
        }
        try (PDDocument document = PDDocument.load(tempFile)) {
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);
                String lines[] = pdfFileInText.split("\\r?\\n");
                for (String line : lines) {
                    sb.append(line);
                }
            }
        }
        return sb.toString();
    }
}
