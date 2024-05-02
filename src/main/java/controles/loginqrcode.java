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
    private Canvas canvas;

    void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            qrcode.setImage(image);

            //  profileImage.setText(file.toURI().toString());
        }
    }

    public void qrcod(ActionEvent actionEvent) {
        browseImage();
        Image image = qrcode.getImage();

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        int width = bufferedImage.getWidth();
        int[] pixels = new int[width];
        bufferedImage.getRGB(0, 0, width, 1, pixels, 0, width);

        drawPixelsOnCanvas(pixels);
    }

    private void drawPixelsOnCanvas(int[] pixels) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double pixelWidth = canvas.getWidth() / pixels.length;
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            // Dessiner le pixel sur le Canvas
            gc.setFill(javafx.scene.paint.Color.rgb((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF));
            gc.fillRect(i * pixelWidth, 0, pixelWidth, 1);
        }
    }
}
