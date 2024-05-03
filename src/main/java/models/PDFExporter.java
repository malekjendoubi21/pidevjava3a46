package models;

import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
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
            String title = "liste de réclamations";
            int titleFontSize = 46;
            float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000 * titleFontSize;
            float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * titleFontSize;
            float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;
            float titleY = yStart - titleHeight;

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, titleFontSize);
            contentStream.newLineAtOffset(titleX, titleY);
            contentStream.showText(title);
            contentStream.endText();

            yStart = titleY - margin;
            yPosition = yStart;


            String imagePath = "C:/Users/Mega-PC/Desktop/Workshop/src/main/resources/mahyeshbentk.png";
            PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
            float imageWidth = 100;
            float imageHeight = 100;
            float xImagePosition = margin+150;
            float yImagePosition = yPosition - imageHeight;
            contentStream.drawImage(image, xImagePosition, yImagePosition, imageWidth, imageHeight);
            yPosition -= imageHeight + 10;


            String[] headers = {"ID", "Sujet", "Contenu"};
            float tableHeight = 20f;
            float rowHeight = 15f;
            float cellMargin = 5f;
            drawTable(contentStream, yPosition, tableWidth, cellMargin, headers, tableHeight, rowHeight);
            yPosition -= (tableHeight + 5);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            drawTableData(contentStream, yPosition, tableWidth, cellMargin, data, tableHeight, rowHeight);
            contentStream.close();
            document.save("list.pdf");
            document.close();
            System.out.println("PDF generated!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while generating PDF: " + e.getMessage());
        }
    }

    private static void drawTable(PDPageContentStream contentStream, float yStart, float tableWidth,
                                  float cellMargin, String[] headers, float tableHeight, float rowHeight) throws IOException {

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        float xPosition = cellMargin;
        for (String header : headers) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition, yStart - tableHeight);
            contentStream.showText(header);
            contentStream.endText();
            xPosition += tableWidth / headers.length;
        }
        contentStream.setLineWidth(1f);
        contentStream.moveTo(cellMargin, yStart - tableHeight);
        contentStream.lineTo(cellMargin + tableWidth, yStart - tableHeight);
        contentStream.stroke();
    }



    private static void drawTableData(PDPageContentStream contentStream, float yStart, float tableWidth,
                                      float cellMargin, ObservableList<reclamation> data, float tableHeight, float rowHeight) throws IOException {
        float yPosition = yStart;
        List<String> badWords = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/Mega-PC/Desktop/Workshop/badwords.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            badWords.add(line.trim().toLowerCase());
        }
        System.out.println(badWords);
        for (reclamation item : data) {
            String content = item.getContenu();
            for (String badWord : badWords) {
                if (content.toLowerCase().contains(badWord)) {
                    content = content.replaceAll("(?i)\\b" + badWord + "\\b", "****");
                }
            }
            item.setContenu(content);
            if (yPosition - rowHeight <= 0) {
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
