import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public abstract class Animal {
    protected String name;         // Poule, Vache, Cochon
    protected String foodNeeded;   // Maïs, Tomate, Citrouille
    protected String animalPath;   // Image de l'animal
    protected String productPath;  // Image du produit
    protected int waitTime;        // Temps d'attente
    protected int yieldAmount;     // Quantité produite
    protected String resourceName; // Nom pour Inventory2

    public Animal(String name, String food, String animalImg, String productImg, int time, int amount, String resName) {
        this.name = name;
        this.foodNeeded = food;
        this.animalPath = animalImg;
        this.productPath = productImg;
        this.waitTime = time;
        this.yieldAmount = amount;
        this.resourceName = resName;
    }

    public void handleAction(Plot plot, ImageView imgView, Inventory inv1, Inventory2 inv2) {

        // si classe vide on place l'animal
        if (!plot.isOccupied()) {
            plot.plant(this.name);
            // On change l'image pour montrer l'animal
            imgView.setImage(new Image(getClass().getResource(this.animalPath).toExternalForm()));
        }

        // si là mais pas pret on essaie de le nourrir
        else if (plot.isOccupied() && !plot.isReady()) {
            // On vérifie dans l'inventaire s'il y a la nourriture
            if (inv1.getVeggieCount(this.foodNeeded) > 0) {
                // On consomme légume dans l'inventaire
                inv1.consumeVegetable(this.foodNeeded);

                System.out.println(this.name + " mange " + this.foodNeeded);

                // On lance le chrono
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(this.waitTime), e -> {
                    plot.setReady(true);
                    // On affiche le produit
                    imgView.setImage(new Image(getClass().getResource(this.productPath).toExternalForm()));
                }));
                timeline.play();
            } else {
                System.out.println("Il vous manque " + this.foodNeeded + " pour nourrir cet animal !");
            }
        }

        // 3. si produit pret on récolte dans l'inventaire
        else if (plot.isReady()) {
            // On ajoute les ressources dans l'inventaire
            inv2.addResource(this.resourceName, this.yieldAmount);

            // On libère la case
            plot.setEmpty();

            // On remet l'image de l'herbe
            imgView.setImage(new Image(getClass().getResource("/images/herbe.png").toExternalForm()));
        }
    }
}