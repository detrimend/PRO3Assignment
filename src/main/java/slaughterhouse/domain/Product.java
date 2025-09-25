package slaughterhouse.domain;


import java.util.HashMap;
import java.util.Map;

public class Product {
    private String productType;
    private int id;
    private HashMap<Tray, String> trayMap;
    private static int nextID = 0;

    public Product(String productType) {
        this.productType = productType;
        this.id = nextID++;
        trayMap = new HashMap<>();
    }


}
