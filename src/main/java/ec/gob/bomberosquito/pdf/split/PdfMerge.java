/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.gob.bomberosquito.pdf.split;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stali
 */
public class PdfMerge {

    static byte[] mergePdfFiles(List<InputStream> inputPdfList) throws IOException {

        List<PdfReader> readers = new ArrayList<>();

        for (InputStream pdf : inputPdfList) {
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer;
            try {
                writer = PdfWriter.getInstance(document, outputStream);
            } catch (DocumentException ex) {
                throw new IOException("An error has occurred while splitting file", ex);
            }

            document.open();

            PdfContentByte pageContentByte = writer.getDirectContent();

            PdfImportedPage pdfImportedPage;

            for (PdfReader pdfReader : readers) {
                int currentPdfReaderPage = 1;
                while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                    pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                    currentPdfReaderPage++;
                }
            }

            document.close();
            return outputStream.toByteArray();
        }
    }

    public static void main(String args[]) throws IOException {
        List<InputStream> inputPdfList = new ArrayList<>();
        inputPdfList.add(new FileInputStream("C:\\Users\\stali\\Downloads\\1.pdf"));
        inputPdfList.add(new FileInputStream("C:\\Users\\stali\\Downloads\\2.pdf"));
        byte[] bytes = mergePdfFiles(inputPdfList);
        try (OutputStream outputStream = new FileOutputStream("C:\\Users\\stali\\Downloads\\MergeFile.pdf")) {
            outputStream.write(bytes);
        }
    }
}
