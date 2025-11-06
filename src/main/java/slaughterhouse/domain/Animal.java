package slaughterhouse.domain;


import com.google.protobuf.Timestamp;
import com.google.type.Date;

public class Animal
{
    private static int nextId = 0;
    private int id;
    private double weight;
    private String origin;


    public Animal(){}

    public Animal(double weight , String origin)
    {
        this.id = nextId++;
        this.weight = weight;
        this.origin = origin;
    }

    public Animal(int id, double weight, String origin)
    {
        this.id = id;
        this.weight = weight;
        this.origin = origin;

    }

    public int getId()
    {
        return id;
    }

    public double getWeight()
    {
        return weight;
    }

    public String getOrigin() {
        return origin;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


}