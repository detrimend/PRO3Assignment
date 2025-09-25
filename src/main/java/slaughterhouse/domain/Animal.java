package slaughterhouse.domain;

public class Animal
{
    private static int nextId = 1;
    private int id;
    private double weight;

    public Animal(double weight)
    {
        this.id = nextId++;
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