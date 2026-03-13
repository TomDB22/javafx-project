public class Store {
    // Prix d'achat des graines
    public static final int CORN_PRICE = 1;
    public static final int TOMATO_PRICE = 2;
    public static final int PUMPKIN_PRICE = 5;

    // Prix de revente (plus cher pour faire du profit !)
    public static final int CORN_SELL = 5;
    public static final int TOMATO_SELL = 10;
    public static final int PUMPKIN_SELL = 25;

    public void buySeed(String type, Inventory inv) {
        int cost = 0;
        if (type.equals("Maïs")) cost = CORN_PRICE;
        else if (type.equals("Tomate")) cost = TOMATO_PRICE;
        else if (type.equals("Citrouille")) cost = PUMPKIN_PRICE;

        if (inv.removeMoney(cost)) {
            inv.addSeed(type);
            System.out.println("Achat réussi : " + type);
        } else {
            System.out.println("Pas assez d'argent !");
        }
    }

}