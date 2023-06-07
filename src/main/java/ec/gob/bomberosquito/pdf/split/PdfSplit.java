/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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

/**
 *
 * @author stali
 */
public class PdfSplit {

    static byte[] splitPdfFile(final InputStream inputPdf, final int startPage, final int endPage) throws IOException {
        PdfReader pdfReader = new PdfReader(inputPdf);
        int totalPages = pdfReader.getNumberOfPages();

        if (startPage > endPage || endPage > totalPages) {
            throw new IllegalArgumentException("Kindly pass the valid values for startPage and endPage.");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            Document document = new Document();
            PdfWriter writer;
            try {
                writer = PdfWriter.getInstance(document, outputStream);
            } catch (DocumentException ex) {
                throw new IOException("An error has occurred while splitting file", ex);
            }

            document.open();

            PdfContentByte pdfContentByte = writer.getDirectContent();

            for (int numberPage = startPage; numberPage <= endPage; numberPage++) {
                document.newPage();
                PdfImportedPage page = writer.getImportedPage(pdfReader, numberPage);
                pdfContentByte.addTemplate(page, 0, 0);
            }
            document.close();
            return outputStream.toByteArray();
        }
    }

    public static void main(String args[]) throws IOException {
        try (InputStream is = new FileInputStream("C:\\Users\\stali\\Downloads\\plan-de-emergencias.pdf")) {
            byte[] bytes1 = splitPdfFile(is, 1, 6);
            try (OutputStream outputStream = new FileOutputStream("C:\\Users\\stali\\Downloads\\1.pdf")) {
                outputStream.write(bytes1);
            }
        }
        try (InputStream is = new FileInputStream("C:\\Users\\stali\\Downloads\\plan-de-emergencias.pdf")) {
            byte[] bytes2 = splitPdfFile(is, 7, 11);
            try (OutputStream outputStream = new FileOutputStream("C:\\Users\\stali\\Downloads\\2.pdf")) {
                outputStream.write(bytes2);
            }
        }

        System.out.println("Pdf file splitted successfully.");
    }
}
