package eg.edu.alexu.csd.oop.draw.cs;

public interface Shape {
    public void setPosition1(java.awt.Point position);
    public void setPosition2(java.awt.Point position);
    public java.awt.Point getPosition1();
    public java.awt.Point getPosition2();
    public String getStr();
    public void setProperties(Double[] properties);

    public Double[] getProperties();

    public void setColor(String color);

    public String getColor();

    public void setFillColor(String color);

    public String getFillColor();


}
