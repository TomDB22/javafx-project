import java.util.HashMap;

public class Inventory2 {
    // Stock des animaux
    private HashMap<String, Integer> animalStock = new HashMap<>();
    // Stock des produits
    private HashMap<String, Integer> productStock = new HashMap<>();

    public Inventory2() {
        animalStock.put("Poule", 0);
        animalStock.put("Vache", 0);
        animalStock.put("Cochon", 0);

        productStock.put("Oeufs", 0);
        productStock.put("Lait", 0);
        productStock.put("Bacon", 0);
    }

    // Animaux
    public void addAnimal(String type) {
        animalStock.put(type, animalStock.getOrDefault(type, 0) + 1);
    }

    public int getAnimalCount(String type) {
        return animalStock.getOrDefault(type, 0);
    }

    // Produits
    public void addResource(String type, int amount) {
        productStock.put(type, productStock.getOrDefault(type, 0) + amount);
    }

    public int getProductCount(String type) {
        return productStock.getOrDefault(type, 0);
    }

    public void clearResource(String type) {
        productStock.put(type, 0);
    }
}