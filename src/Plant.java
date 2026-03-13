import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public abstract class Plant {
    protected String name;
    protected String imagePath;
    protected int growthTime;

    // Constructeur
    public Plant(String name, String imagePath, int growthTime) {
        this.name = name;
        this.imagePath = imagePath;
        this.growthTime = growthTime;
    }

    public void handleAction(Plot plot, ImageView imgView, Inventory inventory) {
        if (!plot.isOccupied()) {
            // Planter
            // On vérifie si on a des graines dans l'inventaire
            if (inventory.getSeedCount(this.name) > 0) {
                inventory.useSeed(this.name);
                plot.plant(this.name);

                // On affiche la petite pousse
                imgView.setImage(new Image(getClass().getResource("/images/pousse.png").toExternalForm()));

                // Chronomètre de pousse
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(this.growthTime), e -> {
                    plot.setReady(true);
                    // On affiche l'image finale du légume
                    imgView.setImage(new Image(getClass().getResource(this.imagePath).toExternalForm()));
                }));
                timeline.play();
            }
        } else if (plot.isReady()) {
            // Récolter
            // On récupère le type de plante
            String typeRecolte = plot.getPlantType();
            inventory.addVegetable(typeRecolte);

            // On nettoie la case
            plot.setEmpty();
            imgView.setImage(new Image(getClass().getResource("/images/herbe.png").toExternalForm()));
        }
    }
}