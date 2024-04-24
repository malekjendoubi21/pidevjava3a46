package toolkit;


import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class MyAnimation {


        private MyAnimation() {
        }

        public static void shake(Node node) {
            TranslateTransition tt;
            tt = new TranslateTransition(Duration.millis(50), node);
            tt.setFromX(0f);
            tt.setByX(10f);
            tt.setCycleCount(2);
            tt.setAutoReverse(true);
            tt.playFromStart();
        }
    }

