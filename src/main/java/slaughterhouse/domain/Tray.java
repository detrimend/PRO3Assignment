package slaughterhouse.domain;

import java.util.Stack;

public class Tray
{
  private Stack<Part> parts;
  private double maxWeight;
  private double currentWeight;

  public Tray(double maxWeight)
  {
    this.maxWeight = maxWeight;
    this.currentWeight = 0;
    this.parts = new Stack<>();
  }

  public boolean addPart(Part part)
  {
    if (currentWeight + part.getWeight() <= maxWeight)
    {
      parts.push(part);
      currentWeight += part.getWeight();
      return true;
    }
    return false;
  }

  public Part takePart()
  {
    if (!parts.isEmpty())
    {
      Part part = parts.pop();
      currentWeight -= part.getWeight();
      return part;
    }
    return null;
  }
}
