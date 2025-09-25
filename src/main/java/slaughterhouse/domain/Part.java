package slaughterhouse.domain;


public class Part
{
  private String Type;
  private double Weight;
  private int animalId;
  private int id;

  public Part(String type, double weight, int animalId, int id)
  {
    Type = type;
    Weight = weight;
    this.animalId = animalId;
    this.id = id;
  }

  public String getType()
  {
    return Type;
  }

  public void setType(String type)
  {
    Type = type;
  }

  public double getWeight()
  {
    return Weight;
  }

  public void setWeight(double weight)
  {
    Weight = weight;
  }

  public int getAnimalId()
  {
    return animalId;
  }


  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String toString()
  {
    return "Part [Type=" + Type + ", Weight=" + Weight + ", animalId="
        + animalId + ", id=" + id + "]";
  }

  public void equals(Part part)
  {
    if (this.id == part.getId())
      System.out.println("The parts are the same");
    else
      System.out.println("The parts are different");

  }
}
