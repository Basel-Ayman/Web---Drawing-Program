package eg.edu.alexu.csd.oop.draw.cs;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class DrawingEngine {
    protected Stack<Object[]> undo;
    protected Stack<Object[]> redo;
    protected ArrayList<Shape> shapes;

    public Object[] Factory(String str,int id, Point p, Point p2, String c, String fc,boolean u) {
        Shape q;
        switch (str) {
            case "c":
                q=new Circle();
                break;
            case "e":
                q=new Ellipse();
                break;
            case "l":
                q=new LineSegment();
                break;
            case "r":
                q=new Rectangle();
                break;
            case "s":
                q=new Square();
                break;
            case "t":
                q=new Triangle();
                break;
            default:
                q=new Circle();
                break;
        }
        q.setPosition1(p);
        q.setPosition2(p2);
        q.setColor(c);
        q.setFillColor(fc);
        addShape(id,q,u);
        Object[] o = new Object[7];
        o[0] = getShapes().size()-1;
        o[1] = getShapes().get(getShapes().size()-1).getPosition1().x;
        o[2] = getShapes().get(getShapes().size()-1).getPosition1().y;
        o[3] = getShapes().get(getShapes().size()-1).getPosition2().x;
        o[4] = getShapes().get(getShapes().size()-1).getPosition2().y;
        o[5] = getShapes().get(getShapes().size()-1).getColor();
        o[6] = getShapes().get(getShapes().size()-1).getFillColor();
        return o;
    }
    public Stack<Object[]> getUndo() {

        return undo;
    }

    public Stack<Object[]> getRedo() {
        return redo;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void addShape(int i,Shape sh,boolean u) {
        if (shapes==null)
            shapes = new ArrayList<Shape>();
        if (undo==null)
            undo= new Stack<Object[]>();
        Object[]m;
        if(u==true)
        {
            m = new Object[]{i, shapes.get(i)};
            shapes.set(i,sh);
        } else
        {
            m = new Object[]{shapes.size(), null};
            shapes.add(sh);
        }
        undo.push(m);
        if(redo!=null)
            redo.clear();
    }
    /*public void updateShape(int i,Shape sh)
    {
        if (shapes==null)
            shapes = new ArrayList<Shape>();
        if (undo==null)
            undo= new Stack<Object[]>();
        Object[] m = {i,shapes.get(i)};
        undo.push(m);
        shapes.set(i,sh);
    }*/
    public void removeShape(int i)
    {
        if (shapes==null) {
        }
        else {
            Object[] m = new Object[2];
            m[0]=i;
            m[1]=shapes.get(i);
            undo.push(m);
            shapes.set(i,null);
            if(redo!=null)
                redo.clear();
        }

    }
    public Object[] opUndo() {
        Object[] o = new Object[8];
        if (undo != null && !undo.isEmpty()) {
            Integer i = (Integer) undo.peek()[0];
            Shape sh = (Shape) undo.peek()[1];
            undo.pop();
            Object[] n = new Object[2];
            n[0]=i;
            n[1]=shapes.get(i);
            if (redo==null)
                redo= new Stack<Object[]>();
            redo.push(n);
            if (sh==null)
            {
                shapes.set(i,null);
                o[0] = i;
                o[1] = 0;
                o[2] = 0;
                o[3] = 0;
                o[4] = 0;
                o[5] = 0;
                o[6] = 0;
                o[7] = 1;
                return o;
            } else {
                shapes.set(i, sh);
                o[0] = i;
                o[1] = sh.getPosition1().x;
                o[2] = sh.getPosition1().y;
                o[3] = sh.getPosition2().x;
                o[4] = sh.getPosition2().y;
                o[5] = sh.getColor();
                o[6] = sh.getFillColor();
                o[7] = 1;
                return o;
            }
        }
        o[7]=0;
        return o;
    }
    public Object[] opRedo()
    {
        Object[] o = new Object[8];
        if ((redo != null) && !redo.isEmpty())
        {
            Integer i = (Integer) redo.peek()[0];
            Shape sh = (Shape) redo.peek()[1];
            redo.pop();
            Object[] n = new Object[2];
            n[0]=i;
            n[1]=shapes.get(i);
            undo.push(n);
            if (sh==null)
            {
                shapes.set(i,null);
                o[0] = i;
                o[1] = 0;
                o[2] = 0;
                o[3] = 0;
                o[4] = 0;
                o[5] = 0;
                o[6] = 0;
                o[7]=1;
                return o;
            } else {
                shapes.set(i, sh);
                o[0] =i;
                o[1] = sh.getPosition1().x;
                o[2] = sh.getPosition1().y;
                o[3] = sh.getPosition2().x;
                o[4] = sh.getPosition2().y;
                o[5] = sh.getColor();
                o[6] = sh.getFillColor();
                o[7] = 1;
                return o;
            }
        }
        o[7]=0;
        return o;
    }



}
