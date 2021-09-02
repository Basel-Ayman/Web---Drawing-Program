package eg.edu.alexu.csd.oop.draw.cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;

@SpringBootApplication
public class CsApplication {

    public static void main(String[] args) {
        /*  DrawingEngine d=new DrawingEngine();
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        Point p4 = new Point();
        Point p5 = new Point();
        p5.x=5;
        p5.y=5;
        p1.x=1;
        p1.y=2;
        p2.x=1;
        p2.y=2;
        p3.x=1;
        p3.y=2;
        p4.x=1;
        p4.y=2;

        Object[] o =d.Factory("c",0,p1,p2,"Red","Red",false);
        d.Factory("c",0,p1,p2,"Red","Red",false);
        d.Factory("c",0,p3,p4,"Red","Red",false);
        d.Factory("c",0,p3,p4,"Red","Red",false);
        d.Factory("c",0,p1,p2,"Red","Red",false);
        d.Factory("c",0,p1,p2,"Red","Red",false);
        d.Factory("c",0,p3,p4,"Red","Red",false);
        d.Factory("c",0,p3,p4,"Red","Red",false);
        d.Factory("c",1,p5,p5,"Blue","Green",true);
        Object[] o1= d.opUndo();
        Object[] o2=d.opRedo();*/
        SpringApplication.run(CsApplication.class, args);
    }

}
