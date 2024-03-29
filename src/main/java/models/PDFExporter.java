package models;

import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PDFExporter {

    private static PDDocument document;

    public static void exportToPDF(ObservableList<reclamation> data) {
        try {
            document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            // Create table header
            String[] headers = {"ID", "Sujet", "Contenu"};
            float tableHeight = 20f;
            float rowHeight = 15f;
            float tableWidthMargin = (tableWidth - 2 * margin) / headers.length;
            float cellMargin = 5f;

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            drawTable(contentStream, yStart, tableWidth, cellMargin, headers, tableHeight, rowHeight);

            // Add data to the table
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            drawTableData(contentStream, yStart, tableWidth, cellMargin, data, tableHeight, rowHeight);

            contentStream.close();

            // Save the document
            document.save("list.pdf");
            document.close();

            System.out.println("PDF genere !");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    private static void drawTable(PDPageContentStream contentStream, float yStart, float tableWidth,
                                  float cellMargin, String[] headers, float tableHeight, float rowHeight) throws IOException {
        // Draw headers
        contentStream.setLineWidth(1f);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        float yPosition = yStart;
        for (String header : headers) {
            contentStream.beginText();
            contentStream.newLineAtOffset(cellMargin, yPosition - tableHeight);
            contentStream.showText(header);
            contentStream.endText();
            yPosition -= rowHeight;
        }

        // Draw horizontal line under headers
        contentStream.setLineWidth(1f);
        contentStream.moveTo(cellMargin, yStart - tableHeight);
        contentStream.lineTo(cellMargin + tableWidth, yStart - tableHeight);
        contentStream.stroke();
    }

    private static void drawTableData(PDPageContentStream contentStream, float yStart, float tableWidth,
                                      float cellMargin, ObservableList<reclamation> data, float tableHeight, float rowHeight) throws IOException {
        float yPosition = yStart;
        for (reclamation item : data) {
            if (yPosition - rowHeight <= 0) {
                // Page overflow, create a new page
                PDPage newPage = new PDPage();
                document.addPage(newPage);
                contentStream.close();

                contentStream = new PDPageContentStream(document, newPage);
                yPosition = yStart;
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(cellMargin, yPosition - rowHeight);
            contentStream.showText(String.valueOf(item.getId()));
            contentStream.newLineAtOffset(tableWidth / 4, 0);
            contentStream.showText(String.valueOf(item.getSujet()));
            contentStream.newLineAtOffset(tableWidth / 4, 0);
            contentStream.showText(String.valueOf(item.getContenu()));
            contentStream.newLineAtOffset(tableWidth / 4, 0);
            contentStream.endText();

            yPosition -= rowHeight;
        }
    }

}
