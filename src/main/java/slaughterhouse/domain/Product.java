package slaughterhouse.domain;


import java.util.HashMap;
import java.util.Map;

public class Product {
    private String productType;

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  private int id;
    private HashMap<Tray, String> trayMap;
    private static int nextID = 0;

    public Product(String productType) {
        this.productType = productType;
        this.id = nextID++;
        trayMap = new HashMap<>();
    }

    public Product (String productType, int id) {
        this.productType = productType;
        this.id = id;
        trayMap = new HashMap<>();
    }

  public String getType()
  {
    return productType;
  }

  public void setType(String productType)
  {
    this.productType = productType;
  }
}
