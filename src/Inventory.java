import java.util.HashMap;

public class Inventory {
    private int money = 50;
    private HashMap<String, Integer> seeds = new HashMap<>();
    private HashMap<String, Integer> vegetables = new HashMap<>();

    public Inventory() {
        seeds.put("Maïs", 0);
        seeds.put("Tomate", 0);
        seeds.put("Citrouille", 0);
        vegetables.put("Maïs", 0);
        vegetables.put("Tomate", 0);
        vegetables.put("Citrouille", 0);
    }

    public int getMoney() { return money; }
    public void addMoney(int amount) { money += amount; }
    public boolean removeMoney(int amount) {
        if (money >= amount) { money -= amount; return true; }
        return false;
    }

    public void addSeed(String type) { seeds.put(type, seeds.getOrDefault(type, 0) + 1); }
    public int getSeedCount(String type) { return seeds.getOrDefault(type, 0); }
    public void useSeed(String type) {
        int current = getSeedCount(type);
        if (current > 0) seeds.put(type, current - 1);
    }

    public void addVegetable(String type) { vegetables.put(type, vegetables.getOrDefault(type, 0) + 1); }
    public int getVeggieCount(String type) { return vegetables.getOrDefault(type, 0); }

    public void clearVeggie(String type) {
        vegetables.put(type, 0);
    }

    public boolean consumeVegetable(String type) {
        int current = vegetables.getOrDefault(type, 0);
        if (current > 0) {
            vegetables.put(type, current - 1);
            return true;
        }
        return false;
    }
}