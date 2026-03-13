import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class LandFarm {

    @FXML private GridPane farmGrid;
    @FXML private GridPane animalGrid;
    @FXML private Label moneyLabel;
    @FXML private Label seedStockLabel;
    @FXML private Label veggieStockLabel;
    @FXML private Label animalStockLabel;
    @FXML private Label animalCountLabel;

    private Inventory inventory = new Inventory();
    private Inventory2 inventory2 = new Inventory2();

    private Plant selectedPlant = null;
    private Animal animalToPlace = null;

    // Outil pour pouvoir récolter
    private final Plant harvestHelper = new Corn();

    private void updateUI() {
        if (moneyLabel != null) moneyLabel.setText("Argent : " + inventory.getMoney() + " $");
        if (seedStockLabel != null) {
            seedStockLabel.setText("Maïs : " + inventory.getSeedCount("Maïs") + "\n" +
                    "Tomate : " + inventory.getSeedCount("Tomate") + "\n" +
                    "Citrouille : " + inventory.getSeedCount("Citrouille"));
        }
        if (veggieStockLabel != null) {
            veggieStockLabel.setText("Maïs : " + inventory.getVeggieCount("Maïs") + "\n" +
                    "Tomate : " + inventory.getVeggieCount("Tomate") + "\n" +
                    "Citrouille : " + inventory.getVeggieCount("Citrouille"));
        }
        if (animalCountLabel != null) {
            animalCountLabel.setText("Poules : " + inventory2.getAnimalCount("Poule") + "\n" +
                    "Vaches : " + inventory2.getAnimalCount("Vache") + "\n" +
                    "Cochons : " + inventory2.getAnimalCount("Cochon"));
        }
        if (animalStockLabel != null) {
            animalStockLabel.setText("Oeufs : " + inventory2.getProductCount("Oeufs") + "\n" +
                    "Lait : " + inventory2.getProductCount("Lait") + "\n" +
                    "Bacon : " + inventory2.getProductCount("Bacon"));
        }
    }

    // Selection
    @FXML private void selectCorn() { selectedPlant = new Corn(); animalToPlace = null; }
    @FXML private void selectTomato() { selectedPlant = new Tomato(); animalToPlace = null; }
    @FXML private void selectPumpkin() { selectedPlant = new Pumpkin(); animalToPlace = null; }
    @FXML private void selectChicken() { animalToPlace = new Chicken(); selectedPlant = null; }
    @FXML private void selectCow() { animalToPlace = new Cow(); selectedPlant = null; }
    @FXML private void selectPig() { animalToPlace = new Pig(); selectedPlant = null; }

    // achats graines
    @FXML private void buyCorn() { if (inventory.removeMoney(1)) { inventory.addSeed("Maïs"); updateUI(); } }
    @FXML private void buyTomato() { if (inventory.removeMoney(2)) { inventory.addSeed("Tomate"); updateUI(); } }
    @FXML private void buyPumpkin() { if (inventory.removeMoney(5)) { inventory.addSeed("Citrouille"); updateUI(); } }

    // ventes
    @FXML private void sellCorn() { sellVeggie("Maïs", 5); }
    @FXML private void sellTomato() { sellVeggie("Tomate", 10); }
    @FXML private void sellPumpkin() { sellVeggie("Citrouille", 25); }
    @FXML private void sellEggs() { if (inventory2.getProductCount("Oeufs") >= 25) { inventory.addMoney(150); inventory2.clearResource("Oeufs"); updateUI(); } }
    @FXML private void sellMilk() { if (inventory2.getProductCount("Lait") >= 15) { inventory.addMoney(225); inventory2.clearResource("Lait"); updateUI(); } }
    @FXML private void sellBacon() { if (inventory2.getProductCount("Bacon") >= 50) { inventory.addMoney(400); inventory2.clearResource("Bacon"); updateUI(); } }

    private void sellVeggie(String type, int price) {
        int count = inventory.getVeggieCount(type);
        if (count > 0) {
            inventory.addMoney(count * price);
            inventory.clearVeggie(type);
            updateUI();
        }
    }

    @FXML
    public void initialize() {
        updateUI();

        // initialisation legumes
        if (farmGrid != null) {
            farmGrid.getChildren().clear();
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    final Plot logic = new Plot(); // INDISPENSABLE : un objet par case
                    StackPane cell = createCell(32);
                    ImageView iv = (ImageView) cell.getChildren().get(0);
                    farmGrid.add(cell, col, row);

                    cell.setOnMouseClicked(e -> {
                        if (logic.isOccupied()) {
                            harvestHelper.handleAction(logic, iv, inventory);
                        } else if (selectedPlant != null) {
                            selectedPlant.handleAction(logic, iv, inventory);
                        }
                        updateUI();
                    });
                }
            }
        }

        // initialisation animaux
        if (animalGrid != null) {
            animalGrid.getChildren().clear();
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    final Plot aLogic = new Plot();
                    StackPane cell = createCell(40);
                    ImageView iv = (ImageView) cell.getChildren().get(0);
                    final Animal[] currentAnimal = {null}; // Stocke l'animal sur cette case
                    animalGrid.add(cell, col, row);

                    cell.setOnMouseClicked(e -> {
                        if (animalToPlace != null && !aLogic.isOccupied()) {
                            int price = (animalToPlace instanceof Chicken) ? 75 : (animalToPlace instanceof Cow) ? 125 : 200;
                            if (inventory.removeMoney(price)) {
                                currentAnimal[0] = animalToPlace;
                                inventory2.addAnimal(currentAnimal[0].name);
                                currentAnimal[0].handleAction(aLogic, iv, inventory, inventory2);
                                animalToPlace = null;
                                updateUI();
                            }
                        } else if (currentAnimal[0] != null) {
                            currentAnimal[0].handleAction(aLogic, iv, inventory, inventory2);
                            updateUI();
                        }
                    });
                }
            }
        }
    }

    private StackPane createCell(int size) {
        StackPane sp = new StackPane();
        sp.setStyle("-fx-border-color: #8B4513; -fx-background-color: white;");
        ImageView iv = new ImageView(new Image(getClass().getResource("/images/herbe.png").toExternalForm()));
        iv.setFitWidth(size); iv.setFitHeight(size);
        sp.getChildren().add(iv);
        return sp;
    }
}