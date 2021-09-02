class Circle {
    constructor(x,y,x2,y2,radius,colour,shape,id,fillColour,filled) {
      this.x = x;
      this.y = y;
      this.x2=x2;
      this.y2=y2;
      this.radius=radius;
      this.colour=colour;
      this.shape=shape;
      this.id=id;
      this.fillColour=fillColour;
      this.filled=filled;
    }
    draw(context) {
        context.strokeStyle = this.colour;
        context.fillStyle=this.fillColour;
        context.beginPath();
        context.arc(this.x, this.y, this.radius, 0, 2 * Math.PI, false);
        if(this.filled)
        {context.fill();}
        context.stroke();
    }
}
    export {Circle};
  

  class Rectangle{
      constructor(x,y,x2,y2,width,height,colour,shape,id,fillColour,filled)
      {
          this.x=x;
          this.y=y;
          this.x2=x2;
          this.y2=y2;
          this.width=width;
          this.height=height;
          this.colour=colour;
          this.shape=shape;
          this.id=id;
          this.fillColour=fillColour;
          this.filled=filled;
      }
      draw(context) {
        context.strokeStyle = this.colour;
        context.fillStyle=this.fillColour;
        context.beginPath();
        if(this.filled)
        {context.fillRect(this.x, this.y, this.width ,this.height );}
        context.rect(this.x, this.y, this.width ,this.height );
        context.stroke();
        
    }
  }
  export {Rectangle};


  class Triangle{
    constructor(x,y,x2,y2,width,height,colour,shape,id,fillColour,filled)
    {
        this.x=x;
        this.y=y;
        this.x2=x2;
        this.y2=y2;
        this.width=width;
        this.height=height;
        this.colour=colour;
        this.shape=shape;
        this.id=id;
        this.fillColour=fillColour;
        this.filled=filled;
    }
    draw(context) {
        context.strokeStyle = this.colour;
        context.fillStyle=this.fillColour;
        context.beginPath();
        context.moveTo( this.x, this.y);
        context.lineTo(this.x+this.width, this.y+this.height);
        context.lineTo(this.x,this.y+this.height);
        if(this.filled)
        {context.fill();}
        context.closePath();
        context.stroke();
  }
}
export {Triangle};


class Ellipse{
    constructor(x,y,x2,y2,radius1,radius2,colour,shape,id,fillColour,filled)
    {
        this.x=x;
        this.y=y;
        this.x2=x2;
        this.y2=y2;
        this.radius1=radius1;
        this.radius2=radius2;
        this.colour=colour;
        this.shape=shape;
        this.id=id;
        this.fillColour=fillColour;
        this.filled=filled;
    }
    draw(context) {
        context.strokeStyle = this.colour;
        context.fillStyle=this.fillColour;
        context.beginPath();
        context.ellipse(this.x,this.y,this.radius1,this.radius2, 0, 0, 2 * Math.PI);
        if(this.filled)
        {context.fill();}
        context.closePath();
        context.stroke();
  }
}
export {Ellipse};

class Square{
    constructor(x,y,x2,y2,width,height,colour,shape,id,fillColour,filled)
    {
        this.x=x;
        this.y=y;
        this.x2=x2;
        this.y2=y2;
        this.width=width;
        this.height=height;
        this.colour=colour;
        this.shape=shape;
        this.id=id;
        this.fillColour=fillColour;
        this.filled=filled;
    }
    draw(context) {
        context.strokeStyle = this.colour;
        context.fillStyle=this.fillColour;
        context.beginPath();
        if(this.filled)
        {context.fillRect(this.x, this.y, this.width , this.width);}
        context.rect(this.x, this.y, this.width , this.width);
        context.stroke();
        
  }
}
export {Square};

class Line{
    constructor(x,y,x2,y2,colour,shape,id,fillColour,filled)
    {
        this.x=x;
        this.y=y;
        this.x2=x2;
        this.y2=y2;
        this.colour=colour;
        this.shape=shape;
        this.id=id;
        this.fillColour=fillColour;
        this.filled=filled;
    }
    draw(context) {
        context.strokeStyle = this.colour;
        context.fillStyle=this.fillColour;
        context.beginPath();
        context.moveTo(this.x, this.y);
        context.lineTo(this.x2,this.y2);
        if(this.filled)
        {context.fill();}
        context.closePath();
        context.stroke();
  }
}
export {Line};
