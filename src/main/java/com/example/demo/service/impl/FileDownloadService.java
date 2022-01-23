package com.example.demo.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileDownloadService {
    public File downloadPdf(String text) throws IOException, DocumentException {
        Document pdfDoc = new Document(PageSize.A4);
        File tempFile = File.createTempFile("text", ".pdf");
        PdfWriter.getInstance(pdfDoc, new FileOutputStream(tempFile))
                .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();
        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        pdfDoc.add(new Paragraph("\n"));
        BufferedReader br = new BufferedReader(new FileReader(text));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            Paragraph para = new Paragraph(strLine + "\n", myfont);
            para.setAlignment(Element.ALIGN_JUSTIFIED);
            pdfDoc.add(para);
        }
        pdfDoc.close();
        br.close();
        return tempFile;
    }
}
