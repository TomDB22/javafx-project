import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Save {
    private static final String FILE_NAME = "farm_save.json";

    public static void saveFarm(Object myFarm) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            Object inv1 = getPrivateField(myFarm, "inventory");
            Object inv2 = getPrivateField(myFarm, "inventory2");

            int money = (int) getPrivateField(inv1, "money");
            Map<?, ?> seeds = (Map<?, ?>) getPrivateField(inv1, "seeds");
            Map<?, ?> veggies = (Map<?, ?>) getPrivateField(inv1, "vegetables");
            Map<?, ?> products = (Map<?, ?>) getPrivateField(inv2, "productStock");

            String json = "{\n" +
                    "  \"money\": " + money + ",\n" +
                    "  \"seeds\": " + mapToJson(seeds) + ",\n" +
                    "  \"vegetables\": " + mapToJson(veggies) + ",\n" +
                    "  \"products\": " + mapToJson(products) + "\n" +
                    "}";

            writer.write(json);
            System.out.println("Partie sauvegardée dans farm_save.json");
        } catch (Exception e) {
            System.err.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }

    public static void loadFarm(Object myFarm) {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;

            String content = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
            Object inv1 = getPrivateField(myFarm, "inventory");
            Object inv2 = getPrivateField(myFarm, "inventory2");

            setPrivateField(inv1, "money", Integer.parseInt(extract(content, "money")));

            fillMap(content, "seeds", (Map<String, Integer>) getPrivateField(inv1, "seeds"));
            fillMap(content, "vegetables", (Map<String, Integer>) getPrivateField(inv1, "vegetables"));
            fillMap(content, "products", (Map<String, Integer>) getPrivateField(inv2, "productStock"));

            System.out.println("Données chargées !");
        } catch (Exception e) {
            System.out.println("Aucune sauvegarde à charger.");
        }
    }

    private static Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    private static void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    private static String extract(String json, String key) {
        String search = "\"" + key + "\": ";
        int start = json.indexOf(search) + search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("\n", start);
        return json.substring(start, end).trim();
    }

    private static void fillMap(String json, String key, Map<String, Integer> map) {
        for (String k : map.keySet()) {
            try {
                String search = "\"" + k + "\":";
                int start = json.indexOf(search, json.indexOf(key)) + search.length();
                int end = -1;
                for (int i = start; i < json.length(); i++) {
                    if (json.charAt(i) == ',' || json.charAt(i) == '}') { end = i; break; }
                }
                map.put(k, Integer.parseInt(json.substring(start, end).trim()));
            } catch (Exception ignored) {}
        }
    }

    private static String mapToJson(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> sb.append("\"").append(k).append("\":").append(v).append(","));
        if (sb.length() > 1) sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}