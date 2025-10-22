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

    public Animal(int id, double weight)
    {
        this.id = id;
        this.weight = weight;
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