import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de l'interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/farmland.fxml"));
        Parent root = loader.load();

        // Récupération du contrôleur LandFarm
        LandFarm myFarm = loader.getController();

        // chargement données
        Save.loadFarm(myFarm);

        // On utilise Platform.runLater pour être sûr que l'interface est prête
        Platform.runLater(() -> {
            try {
                java.lang.reflect.Method updateMethod = myFarm.getClass().getDeclaredMethod("updateUI");
                updateMethod.setAccessible(true);
                updateMethod.invoke(myFarm);
            } catch (Exception e) {
                System.out.println("Note : Pense à vérifier que ta méthode updateUI() existe dans LandFarm");
            }
        });

        // Configuration de la fenêtre
        primaryStage.setTitle("Farm my Farm");
        primaryStage.setScene(new Scene(root, 600, 450));

        // Sauvegarde a la fermeture
        primaryStage.setOnCloseRequest(event -> {
            Save.saveFarm(myFarm);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


















