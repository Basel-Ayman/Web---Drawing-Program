package eg.edu.alexu.csd.oop.draw.cs;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.ArrayList;

@RestController
@CrossOrigin
public class Controller {
    DrawingEngine d;
    @GetMapping("/addShape")
    public Object[] addS(String str,int q, int x,int y,int x2, int y2, String c, String fc,boolean u)
    {
        java.awt.Point p=new Point();
        java.awt.Point p2=new Point();
        p.x=x;
        p.y=y;
        p2.x=x2;
        p2.y=y2;
        if(d==null)
            d=new DrawingEngine();
        return d.Factory(str,q,p,p2,c,fc,u);
    }
    @GetMapping("/removeShape")
    public void removeS(int i)
    {
        d.removeShape(i);
    }
    @GetMapping("/undo")
    public Object[]  und()
    {
        if(d==null)
            d= new DrawingEngine();
        return(d.opUndo());
    }
    @GetMapping("/redo")
    public Object[] red()
    {
        if(d==null)
            d= new DrawingEngine();
        return(d.opRedo());
    }
    @GetMapping("/array")
    public Object[][] arr()
    {
        Object[][] o= new Object[2][2];
        o[0][0]=1;
        o[0][1]=2;
        o[1][0]=3;
        o[1][1]=4;
        return o;
    }
}
