package slaughterhouse.domain;

public class Animal
{
    private static int nextId = 0;
    private int id;
    private double weight;

    public Animal(double weight)
    {
        this.id = nextId++;
        this.weight = weight;
    }

    public Animal(double weight, int id)
    {
        this.weight = weight;
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public double getWeight()
    {
        return weight;
    }
}