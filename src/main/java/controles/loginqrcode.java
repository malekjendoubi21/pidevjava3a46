package controles;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;

public class loginqrcode {
    @FXML
    private ImageView qrcode;

    @FXML
    private Canvas canvas; // Ajoutez un Canvas dans votre fichier FXML et injectez-le ici

    void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            qrcode.setImage(image);

            // Mettre à jour le champ de texte profileImage avec le chemin de l'image sélectionnée
            //  profileImage.setText(file.toURI().toString());
        }
    }

    public void qrcod(ActionEvent actionEvent) {
        browseImage();
        Image image = qrcode.getImage();

        // Convertir l'image en BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        // Extraire la première ligne de pixels
        int width = bufferedImage.getWidth();
        int[] pixels = new int[width];
        bufferedImage.getRGB(0, 0, width, 1, pixels, 0, width);

        // Dessiner les pixels sur le Canvas
        drawPixelsOnCanvas(pixels);
    }

    private void drawPixelsOnCanvas(int[] pixels) {
        // Obtenez le contexte graphique du Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Effacer le contenu précédent du Canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessiner les pixels sur le Canvas
        double pixelWidth = canvas.getWidth() / pixels.length;
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            // Dessiner le pixel sur le Canvas
            gc.setFill(javafx.scene.paint.Color.rgb((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF));
            gc.fillRect(i * pixelWidth, 0, pixelWidth, 1);
        }
    }
}
