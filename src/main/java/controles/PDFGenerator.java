package controles;

import models.Don;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PDFGenerator {
    public static String generateDonationListPDF(Don donation, String pdfFilePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFile("C:/Users/Desktop/Don/images/lg.png", document);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, 450, 750, 100, 50); // Adjust size & position

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                float marginX = 50;
                float yPosition = 700;

                contentStream.newLineAtOffset(marginX, yPosition);
                contentStream.showText("Donation receipt");
                contentStream.newLineAtOffset(0, -20);

                contentStream.setFont(PDType1Font.HELVETICA, 10);

                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                contentStream.showText("Donation Date: " + formattedDateTime);

                contentStream.newLineAtOffset(0, -15);
                Random random = new Random();
                int donationId = random.nextInt(1000);
                contentStream.showText("Donation Number: " + donationId);

                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("Donor: " + donation.getNom() + " " + donation.getPrenom());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Email: " + donation.getEmail());

                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("Item Description: " + donation.getDescription());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Amount: " + donation.getMontant()+ "TND");

                contentStream.endText();




            }

            document.save(pdfFilePath);
            return pdfFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
