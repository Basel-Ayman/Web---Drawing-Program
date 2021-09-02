package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.*;


public class cShape implements Shape {

    protected Point point1;
    protected Point point2;
    protected String color;
    protected String fillcolor;
    protected Double[] pro;
    protected String str;

    public String getStr() {
        return str;
    }

    @Override
    public void setPosition1(Point position) {
        this.point1=position;
    }
    public void setPosition2(Point position) {
        this.point2 = position;
    }
    @Override
    public Point getPosition1() {
        return point1;
    }
    public Point getPosition2() {
        return point2;
    }

    @Override
    public void setProperties(Double[] properties) {
        this.pro=properties;
    }

    @Override
    public Double[] getProperties() {
        return pro;
    }

    @Override
    public void setColor(String color) {
        this.color= color;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setFillColor(String color) {
        this.fillcolor=color;
    }

    @Override
    public String getFillColor() {
        return fillcolor;
    }
}
