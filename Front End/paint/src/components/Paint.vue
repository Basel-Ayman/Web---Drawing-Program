<template>
  <div class="toolbox left">
    <div class="group shapes">
      <div class="items" data-tool="line" title="Line Tool" @click="draw('line')">
        <img src="../assets/deg-line.png" alt="Line Tool">
      </div>
      <div class="items" data-tool="circle" title="Circle Tool" @click="draw('circle')">
        <img src="../assets/circle.png" alt="Circle Tool">
      </div>
      <div class="items" data-tool="ellipse" title="Ellipse Tool" @click="draw('ellipse')">
        <img src="../assets/ellipse.png" alt="Ellipse Tool">
      </div>
      <div class="items" data-tool="triangle" title="Triangle Tool" @click="draw('triangle')">
        <img src="../assets/triangle.png" alt="Triangle Tool">
      </div>
      <div class="items" data-tool="rectangle" title="Rectangle Tool" @click="draw('rectangle')">
        <img src="../assets/rectangular.png" alt="Rectangle Tool">
      </div>
      <div class="items" data-tool="square" title="Square Tool" @click="draw('square')">
        <img src="../assets/square.png" alt="Square Tool">
      </div>
    </div>
    <div class="group colors">
      <div class="items" data-color="#FF2200" title="Color Picker">
        <input class="input_color" type="color" value="#000000" id="color">
      </div>
    </div>
  </div>

   <div id="canvas_container">
    <canvas id="canvas" width="980" height="640" @mousedown="dragStart" @mousemove="drag" @mouseup="dragStop"></canvas>
  </div>

  <div class="toolbox right">
    <div class="group commands">
      <div class="items" data-command="load" title="Load">
        <img src="../assets/open.png" alt="Load">
      </div>
      <div class="items" data-command="save" title="Save" >
        <img src="../assets/save.png" alt="Save">
      </div>
      <div class="items" data-command="undo" title="Undo" @click="undo" >
        <img src="../assets/undo.png" alt="Undo">
      </div>
      <div class="items" data-command="redo" title="Redo" @click="redo">
        <img src="../assets/redo.png" alt="Redo">
      </div>
      <div class="items" data-command="move" title="Move" @click="setMove">
        <img src="../assets/move.png" alt="Move">
      </div>
      <div class="items" data-command="resize" title="Resize" @click="setResize">
        <img src="../assets/size.png" alt="Resize">
      </div>
      <div class="items" data-command="delete" title="Delete" @click="remove">
        <img src="../assets/delete.png" alt="Delete">
      </div>
      <div class="items" data-command="copy" title="Copy"  @click="setCopy">
        <img src="../assets/copy.png" alt="Copy">
      </div>
    </div>
    <div class="group fillColors">
      <div class="items" data-command="fill-shapes"  title="Fill Color Picker" @click="fill">
        <input  class="input_color" type="color" value="#f5f5f5" id="fillColor" >
      </div>
    </div>
  </div>

  <div class="canvasOpen" style="display: none">
    <button type="button" open-data="new-drawing">New Drawing</button>
    <input type="file" open-data="local-image">
  </div>



</template>

<script>
import {Circle,Rectangle,Ellipse,Square,Triangle,Line} from './Shapes.js';
import axios from "axios"
export default {
  data() {
    return {
    canvas : null,
    context : null,
    dragging : false,
    move:false,
    resize:false,
    copy:false,
    removing:false,
    x : null,
    y:null,
    x2:null,
    y2:null,
    shape :'',
    colour:'',
    fillColour:'',
    filled:false,
    selectedId:-1,
    id:0,
    shapesArray :[],
    snapshot : null,
    }
  },

  methods:{
 takeSnapshot() {
    this.snapshot = this.context.getImageData(0, 0,this.canvas.width,this.canvas.height);
},
 restoreSnapshot() {
  this.context.putImageData(this.snapshot, 0, 0);
},
draw(shape)
    {
      this.shape=shape;
    },
drawShape(){
      if(this.shape=='line')
      {this.drawLine();}
      if(this.shape=='circle')
      {this.drawCircle();}
      if(this.shape=='rectangle')
      {this.drawRect();}
      if(this.shape=='triangle')
      {this.drawTri();}
      if(this.shape=='square')
      {this.drawSq();}
      if(this.shape=='ellipse')
      {this.drawEll();}
},
reDraw(){
  this.context.clearRect(0,0,this.canvas.width,this.canvas.height);
    for(var i=0;i<this.shapesArray.length;i++)
    {
      this.shapesArray[i].draw(this.context);
    }
},
fill()
{
  this.filled=true;
},
setMove(){
  this.move=true;
},
setResize()
{
  this.resize=true;
},
setCopy()
{
  this.copy=true;
},
remove()
{
  this.removing=true;
},
 drawCircle() { 
   if(this.copy)
   {
     let c=new Circle(this.shapesArray[this.selectedId].x+50,
                      this.shapesArray[this.selectedId].y+50,
                      this.shapesArray[this.selectedId].x2+50,
                      this.shapesArray[this.selectedId].y2+50,
                      this.shapesArray[this.selectedId].radius,
                      this.shapesArray[this.selectedId].colour,
                      this.shapesArray[this.selectedId].shape,
                      this.id,
                      this.shapesArray[this.selectedId].fillColour,
                      this.shapesArray[this.selectedId].filled);
    c.draw(this.context);
    this.shapesArray.push(c);
    this.id++;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "c",
                    q: 0,
                    x: this.shapesArray[this.selectedId].x+50,
                    y:  this.shapesArray[this.selectedId].y+50,
                    x2: this.shapesArray[this.selectedId].x2+50,
                    y2: this.shapesArray[this.selectedId].y2+50,
                    c: this.shapesArray[this.selectedId].colour,
                    fc: this.shapesArray[this.selectedId].fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
                /*console.log(this.shapesArray[this.selectedId].id);*/
               
                
                
    }) .catch(error => alert(error))
   }
   else{
    var radius = Math.sqrt(Math.pow((this.x-this.x2),2)+Math.pow((this.y-this.y2),2)); 
    let c = new Circle(this.x,this.y,this.x2,this.y2,radius,this.colour,this.shape,this.id,this.fillColour,this.filled);
    c.draw(this.context);
    
    if(!this.dragging)
    {this.shapesArray.push(c);
    this.id++;
    this.filled=false;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "c",
                    q: 0,
                    x: this.x,
                    y: this.y,
                    x2: this.x2,
                    y2: this.y2,
                    c: this.colour,
                    fc: this.fillColour,
                    u: false,
                }
            })
            .then(response => {
                console.log(response.data);
                console.log(this.x2);
                console.log(this.y2);
                console.log(radius);

               
                
                
    }) .catch(error => alert(error))}
   }
    
},
 drawRect()
{
   if(this.copy)
   {
     let r=new Rectangle(this.shapesArray[this.selectedId].x+50,
                        this.shapesArray[this.selectedId].y+50,
                        this.shapesArray[this.selectedId].x2+50,
                        this.shapesArray[this.selectedId].y2+50,
                        this.shapesArray[this.selectedId].width,
                        this.shapesArray[this.selectedId].height,
                        this.shapesArray[this.selectedId].colour,
                        this.shapesArray[this.selectedId].shape,
                        this.id,
                        this.shapesArray[this.selectedId].fillColour,
                        this.shapesArray[this.selectedId].filled);
    r.draw(this.context);
    this.shapesArray.push(r);
    this.id++;
     axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "r",
                    q: 0,
                    x: this.shapesArray[this.selectedId].x+50,
                    y:  this.shapesArray[this.selectedId].y+50,
                    x2: this.shapesArray[this.selectedId].x2+50,
                    y2: this.shapesArray[this.selectedId].y2+50,
                    c: this.shapesArray[this.selectedId].colour,
                    fc: this.shapesArray[this.selectedId].fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))
   }
   else{
   var width=this.x2-this.x;
   var height=this.y2-this.y;
   let r = new Rectangle(this.x,this.y,this.x2,this.y2,width,height,this.colour,this.shape,this.id,this.fillColour,this.filled);
    r.draw(this.context);
    if(!this.dragging)
    {this.shapesArray.push(r);
    this.id++;
    this.filled=false;
     axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "r",
                    q: 0,
                    x: this.x,
                    y: this.y,
                    x2: this.x2,
                    y2: this.y2,
                    c: this.colour,
                    fc: this.fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))}
   }
},
 drawTri()
{
   if(this.copy)
   {
     let t=new Triangle(this.shapesArray[this.selectedId].x+50,
                        this.shapesArray[this.selectedId].y+50,
                       this.shapesArray[this.selectedId].x2+50,
                        this.shapesArray[this.selectedId].y2+50,
                         this.shapesArray[this.selectedId].width,
                          this.shapesArray[this.selectedId].height,
                        this.shapesArray[this.selectedId].colour,
                        this.shapesArray[this.selectedId].shape,
                        this.id,
                        this.shapesArray[this.selectedId].fillColour,
                        this.shapesArray[this.selectedId].filled);
    t.draw(this.context);
    this.shapesArray.push(t);
    this.id++;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "t",
                    q: 0,
                    x: this.shapesArray[this.selectedId].x+50,
                    y:  this.shapesArray[this.selectedId].y+50,
                    x2: this.shapesArray[this.selectedId].x2+50,
                    y2: this.shapesArray[this.selectedId].y2+50,
                    c: this.shapesArray[this.selectedId].colour,
                    fc: this.shapesArray[this.selectedId].fillColour,
                    u: false,

                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))
   }
   else{
  var width=this.x2-this.x;
   var height=this.y2-this.y;
  let t = new Triangle(this.x,this.y,this.x2,this.y2,width,height,this.colour,this.shape,this.id,this.fillColour,this.filled);
    t.draw(this.context);
    if(!this.dragging)
    {this.shapesArray.push(t);
    this.id++;
    this.filled=false;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "t",
                    q: 0,
                    x: this.x,
                    y: this.y,
                    x2: this.x2,
                    y2: this.y2,
                    c: this.colour,
                    fc: this.fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))}
   }
},
 drawEll()
{
   if(this.copy)
   {
     let e=new Ellipse(this.shapesArray[this.selectedId].x+50,
                        this.shapesArray[this.selectedId].y+50,
                        this.shapesArray[this.selectedId].x2+50,
                        this.shapesArray[this.selectedId].y2+50,
                        this.shapesArray[this.selectedId].radius1,
                        this.shapesArray[this.selectedId].radius2,
                        this.shapesArray[this.selectedId].colour,
                        this.shapesArray[this.selectedId].shape,
                        this.id,
                        this.shapesArray[this.selectedId].fillColour,
                        this.shapesArray[this.selectedId].filled);
    e.draw(this.context);
    this.shapesArray.push(e);
    this.id++;
     axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "e",
                    q: 0,
                    x: this.shapesArray[this.selectedId].x+50,
                    y:  this.shapesArray[this.selectedId].y+50,
                    x2: this.shapesArray[this.selectedId].x2+50,
                    y2: this.shapesArray[this.selectedId].y2+50,
                    c: this.shapesArray[this.selectedId].colour,
                    fc: this.shapesArray[this.selectedId].fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))
   }
   else{
    var radius1 = Math.abs((this.x2 - this.x)*0.5);
    var radius2 = Math.abs((this.y2 - this.y)*0.5);
    let e = new Ellipse(this.x,this.y,this.x2,this.y2,radius1,radius2,this.colour,this.shape,this.id,this.fillColour,this.filled);
    e.draw(this.context);
    if(!this.dragging)
    {this.shapesArray.push(e);
    this.id++;
    this.filled=false;
     axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "e",
                    q: 0,
                    x: this.x,
                    y: this.y,
                    x2: this.x2,
                    y2: this.y2,
                    c: this.colour,
                    fc: this.fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))}
   }
},
 drawSq()
{
   if(this.copy)
   {
     let s=new Square(this.shapesArray[this.selectedId].x+50,
                        this.shapesArray[this.selectedId].y+50,
                        this.shapesArray[this.selectedId].x2+50,
                        this.shapesArray[this.selectedId].y2+50,
                        this.shapesArray[this.selectedId].width,
                        this.shapesArray[this.selectedId].height,
                        this.shapesArray[this.selectedId].colour,
                        this.shapesArray[this.selectedId].shape,
                        this.id,
                        this.shapesArray[this.selectedId].fillColour,
                        this.shapesArray[this.selectedId].filled);
    s.draw(this.context);
    this.shapesArray.push(s);
    this.id++;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "s",
                    q: 0,
                    x: this.shapesArray[this.selectedId].x+50,
                    y:  this.shapesArray[this.selectedId].y+50,
                    x2: this.shapesArray[this.selectedId].x2+50,
                    y2: this.shapesArray[this.selectedId].y2+50,
                    c: this.shapesArray[this.selectedId].colour,
                    fc: this.shapesArray[this.selectedId].fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))
   }
   else{
  var width=this.x2-this.x;
  var height=this.x2-this.x;
  let s = new Square(this.x,this.y,this.x2,this.y2,width,height,this.colour,this.shape,this.id,this.fillColour,this.filled);
    s.draw(this.context);
    if(!this.dragging)
    {this.shapesArray.push(s);
    this.id++;
    this.filled=false;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "s",
                    q: 0,
                    x: this.x,
                    y: this.y,
                    x2: this.x2,
                    y2: this.y2,
                    c: this.colour,
                    fc: this.fillColour,
                    u: false,
                    

                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))}
   }
},

drawLine() {
  let l = new Line(this.x,this.y,this.x2,this.y2,this.colour,this.shape,this.id,this.fillColour,this.filled);
    l.draw(this.context);
    if(!this.dragging)
    {this.shapesArray.push(l);
    this.id++;
    this.filled=false;
    axios.get("http://localhost:8090/addShape",{
                params :{
                    str: "l",
                    q: 0,
                    x: this.x,
                    y: this.y,
                    x2: this.x2,
                    y2: this.y2,
                    c: this.colour,
                    fc: this.fillColour,
                    u: false,
                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))}
},
selection()
{
  for(var i=0;i<this.shapesArray.length;i++)
  {
    this.shapesArray[i].draw(this.context);
    if(this.context.isPointInPath(this.x,this.y))
    {
      this.selectedId=i;
      this.shape=this.shapesArray[this.selectedId].shape;
      break;
    }
  }
},
resizingButton()
{
  if(this.shape=='circle')
  {
     var radius = Math.sqrt(Math.pow((this.shapesArray[this.selectedId].x-event.offsetX),2)+Math.pow((this.shapesArray[this.selectedId].y-event.offsetY),2)); 
     this.shapesArray[this.selectedId].radius=radius;
     
     
  }
  if(this.shape=='ellipse')
  {
    var radius1 = Math.abs((event.offsetX - this.shapesArray[this.selectedId].x)*0.5);
    var radius2 = Math.abs((event.offsetY- this.shapesArray[this.selectedId].y)*0.5); 
    this.shapesArray[this.selectedId].radius1=radius1;
    this.shapesArray[this.selectedId].radius2=radius2;
  }
  if(this.shape=='rectangle')
  {
     var width=event.offsetX-this.shapesArray[this.selectedId].x;
     var height=event.offsetY-this.shapesArray[this.selectedId].y;
    this.shapesArray[this.selectedId].width=width;
    this.shapesArray[this.selectedId].height=height;
  }
  if(this.shape=='square')
  {
    var width1=event.offsetX-this.shapesArray[this.selectedId].x;
    this.shapesArray[this.selectedId].width=width1;
    this.shapesArray[this.selectedId].height=width1;

  }
  if(this.shape=='triangle')
  {
    this.shapesArray[this.selectedId].x2=event.offsetX;
    this.shapesArray[this.selectedId].y2=event.offsetY;
    var width2=event.offsetX-this.shapesArray[this.selectedId].x;
    var height2=event.offsetY-this.shapesArray[this.selectedId].y;
    this.shapesArray[this.selectedId].width=width2;
    this.shapesArray[this.selectedId].height=height2;
  }
},
draggingButton()
{
  if(this.shape=='circle')
  {
    this.x2=event.offsetX;
    this.y2=event.offsetY;
    this.shapesArray[this.selectedId].x2=this.shapesArray[this.selectedId].x2+(event.offsetX-this.shapesArray[this.selectedId].x);
    this.shapesArray[this.selectedId].y2=this.shapesArray[this.selectedId].y2+(event.offsetY-this.shapesArray[this.selectedId].y);
    this.shapesArray[this.selectedId].x=event.offsetX;
    this.shapesArray[this.selectedId].y=event.offsetY;
    
     

  }
  if(this.shape=='ellipse')
  {
    this.x2=event.offsetX;
    this.y2=event.offsetY;
    this.shapesArray[this.selectedId].x2=this.shapesArray[this.selectedId].x2+(event.offsetX-this.shapesArray[this.selectedId].x);
    this.shapesArray[this.selectedId].y2=this.shapesArray[this.selectedId].y2+(event.offsetY-this.shapesArray[this.selectedId].y);
    this.shapesArray[this.selectedId].x=event.offsetX;
    this.shapesArray[this.selectedId].y=event.offsetY;
  }
  if(this.shape=='rectangle')
  {
    this.x2=event.offsetX;
    this.y2=event.offsetY;
    this.shapesArray[this.selectedId].x2=this.shapesArray[this.selectedId].x2+(event.offsetX-this.shapesArray[this.selectedId].x);
    this.shapesArray[this.selectedId].y2=this.shapesArray[this.selectedId].y2+(event.offsetY-this.shapesArray[this.selectedId].y);
    this.shapesArray[this.selectedId].x=event.offsetX;
    this.shapesArray[this.selectedId].y=event.offsetY;
  }
  if(this.shape=='square')
  {
    this.x2=event.offsetX;
    this.y2=event.offsetY;
    this.shapesArray[this.selectedId].x2=this.shapesArray[this.selectedId].x2+(event.offsetX-this.shapesArray[this.selectedId].x);
    this.shapesArray[this.selectedId].y2=this.shapesArray[this.selectedId].y2+(event.offsetY-this.shapesArray[this.selectedId].y);
    this.shapesArray[this.selectedId].x=event.offsetX;
    this.shapesArray[this.selectedId].y=event.offsetY;
  }
  if(this.shape=='triangle')
  {
    this.x2=event.offsetX;
    this.y2=event.offsetY;
    this.shapesArray[this.selectedId].x2=this.shapesArray[this.selectedId].x2+(event.offsetX-this.shapesArray[this.selectedId].x);
    this.shapesArray[this.selectedId].y2=this.shapesArray[this.selectedId].y2+(event.offsetY-this.shapesArray[this.selectedId].y);
    this.shapesArray[this.selectedId].x=event.offsetX;
    this.shapesArray[this.selectedId].y=event.offsetY;
  }
},
 dragStart(event) {
   if(this.filled)
   {
      this.fillColour = document.getElementById("fillColor").value;
   }
    this.colour=document.getElementById("color").value;
    this.dragging = true;
    this.x = event.offsetX;
    this.y=event.offsetY;
    this.takeSnapshot();
    if(this.move )
    {
      this.dragging=false;
      this.selection();
    }
    if(this.resize)
    {
      this.dragging=false;
      this.selection();
    }
    if(this.copy)
    {
      this.dragging=false;
      this.selection();
      this.drawShape();
      this.copy=false;
      this.selectedId=-1;
    }
    if(this.removing)
    {
        this.dragging=false;
        this.selection();
        let sh;
                  let x= 0;
                  let y= 0;
                  let x2= 0;
                  let y2= 0;
                  let colorr= "";
                  let filcolorr= "";
                  let r;
                  let w;
                  let h;
                  switch(this.shapesArray[this.selectedId].shape)
                  {
                    case "circle":
                      r = Math.sqrt(Math.pow((x-x2),2)+Math.pow((y-y2),2));
                      console.log(r);
                      sh = new Circle(x,y,x2,y2,r,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;
                    case "square":
                      r = x2-x;
                      sh = new Square(x,y,x2,y2,r,r,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;
                    case "rectangle":
                      w = x2-x;
                      h = y2-y;
                      sh = new Rectangle(x,y,x2,y2,w,h,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;
                    case "triangle":
                      w = x2-x;
                      h = y2-y;
                      sh = new Rectangle(x,y,x2,y2,w,h,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;
                    case "line":
                      sh = new Line(x,y,x2,y2,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;
                    case "ellipse":
                      w = Math.abs((x2 - x)*0.5);
                      h = Math.abs((y2 - y)*0.5);
                      sh = new Ellipse(x,y,x2,y2,w,h,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;
                    default:
                      sh = new Circle(x,y,x2,y2,colorr,this.shapesArray[this.selectedId].shape,this.shapesArray[this.selectedId].id,filcolorr,this.shapesArray[this.selectedId].filled);
                      break;        
                  }
                  this.shapesArray.splice(this.selectedId,1,sh);
                  axios.get("http://localhost:8090/removeShape",{
                params :{
                    i: this.selectedId,
                }
            })
            .then(response => {
                console.log(response.data);
               
                
                
    }) .catch(error => alert(error))
        /*this.shapesArray.splice(this.selectedId,1);*/
        this.reDraw();
        this.removing=false;
        this.selectedId=-1;
    }
 
},
 drag(event) {
      if (this.dragging) {
        this.restoreSnapshot();
        this.x2=event.offsetX;
        this.y2=event.offsetY;
        this.drawShape();
       }
       else if(this.move && this.selectedId !==-1)
       {
         this.draggingButton();
         this.reDraw();
       }
       else if(this.resize && this.selectedId !==-1)
        {
          this.resizingButton();
          this.reDraw();
        }
},
 dragStop(event) {
   if(this.dragging){
     this.dragging = false;
     this.restoreSnapshot();
     this.x2=event.offsetX;
     this.y2=event.offsetY;
     this.drawShape();
   }
   else if (this.move)
   {
     this.restoreSnapshot();
     this.draggingButton();
     this.reDraw();
     if(this.shapesArray[this.selectedId].shape=='circle')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "c",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='ellipse')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "e",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='triangle')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "t",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='rectangle')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "r",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='square')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "s",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='line')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "l",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }
     this.move=false;
     this.selectedId=-1;
   }
   else if(this.resize)
   {
    this.restoreSnapshot();
     this.resizingButton();
     this.reDraw();
     this.shapesArray[this.selectedId].x2=event.offsetX;
     this.shapesArray[this.selectedId].y2=event.offsetY;
     if(this.shapesArray[this.selectedId].shape=='circle')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "c",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='ellipse')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "e",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='triangle')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "t",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='rectangle')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "r",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='square')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "s",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }else if(this.shapesArray[this.selectedId].shape=='line')
     {
      axios.get("http://localhost:8090/addShape",{
                  params :{
                      str: "l",
                      q: this.shapesArray[this.selectedId].id,
                      x: this.shapesArray[this.selectedId].x,
                      y: this.shapesArray[this.selectedId].y,
                      x2: this.shapesArray[this.selectedId].x2,
                      y2: this.shapesArray[this.selectedId].y2,
                      c: this.shapesArray[this.selectedId].colour,
                      fc: this.shapesArray[this.selectedId].fillColour,
                      u: true,
                  }
              })
              .then(response => {
                  console.log(response.data);
                
                  
                  
      }) .catch(error => alert(error))
     }
     this.resize=false;
     this.selectedId=-1;
   }
   
},
undo(){
            axios.get("http://localhost:8090/undo")
            .then(response => {
              if(response.data[7]!= 0)
                {
                  let sh;
                  let x= response.data[1];
                  let y= response.data[2];
                  let x2= response.data[3];
                  let y2= response.data[4];
                  let colorr= response.data[5];
                  let filcolorr= response.data[6];
                  let j;
                  let w;
                  let h;
                  switch(this.shapesArray[response.data[0]].shape)
                  {
                    case "circle":
                      j = Math.sqrt(Math.pow((x-x2),2)+Math.pow((y-y2),2));
                      sh = new Circle(x,y,x2,y2,j,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "square":
                      j = x2-x;
                      sh = new Square(x,y,x2,y2,j,j,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "rectangle":
                      w = x2-x;
                      h = y2-y;
                      sh = new Rectangle(x,y,x2,y2,w,h,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "triangle":
                      w = x2-x;
                      h = y2-y;
                      sh = new Triangle(x,y,x2,y2,w,h,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "line":
                      sh = new Line(x,y,x2,y2,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "ellipse":
                      w = Math.abs((x2 - x)*0.5);
                      h = Math.abs((y2 - y)*0.5);
                      sh = new Ellipse(x,y,x2,y2,w,h,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    default:
                      sh = new Circle(x,y,x2,y2,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;        
                  }
                  /*this.shapesArray[response.data[0]].x=response[1];
                  this.shapesArray[response.data[0]].y=response[2];
                  this.shapesArray[response.data[0]].x2=response[3];
                  this.shapesArray[response.data[0]].y2=response[4];
                  this.shapesArray[response.data[0]].colour=response[5];
                  this.shapesArray[response.data[0]].fillColour=response[6];*/
                  this.shapesArray.splice(response.data[0],1,sh);
                  console.log(response.data);
                  console.log(this.shapesArray[response.data[0]]);
                  console.log(sh);
                  this.reDraw();}
            })
        },
        redo(){
            axios.get("http://localhost:8090/redo")
            .then(response => {
              if(response.data[7]!= 0)
                {let sh;
                  let x= response.data[1];
                  let y= response.data[2];
                  let x2= response.data[3];
                  let y2= response.data[4];
                  let colorr= response.data[5];
                  let filcolorr= response.data[6];
                  let r;
                  let w;
                  let h;
                  switch(this.shapesArray[response.data[0]].shape)
                  {
                    case "circle":
                      r = Math.sqrt(Math.pow((x-x2),2)+Math.pow((y-y2),2));
                      console.log(r);
                      sh = new Circle(x,y,x2,y2,r,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "square":
                      r = x2-x;
                      sh = new Square(x,y,x2,y2,r,r,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "rectangle":
                      w = x2-x;
                      h = y2-y;
                      sh = new Rectangle(x,y,x2,y2,w,h,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "triangle":
                      w = x2-x;
                      h = y2-y;
                      sh = new Triangle(x,y,x2,y2,w,h,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "line":
                      sh = new Line(x,y,x2,y2,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    case "ellipse":
                      w = Math.abs((x2 - x)*0.5);
                      h = Math.abs((y2 - y)*0.5);
                      sh = new Ellipse(x,y,x2,y2,w,h,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;
                    default:
                      sh = new Circle(x,y,x2,y2,colorr,this.shapesArray[response.data[0]].shape,this.shapesArray[response.data[0]].id,filcolorr,this.shapesArray[response.data[0]].filled);
                      break;        
                  }
                  this.shapesArray.splice(response.data[0],1,sh);
                console.log(response.data);
                console.log(this.shapesArray[response.data[0]]);
                this.reDraw();}
            })
        },

  },
  mounted(){
     this.canvas =document.getElementById("canvas");
    this.context =this.canvas.getContext('2d');
  },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
body {
  margin: 0;
  background-color: rgba(0, 0, 0, 0.93);
}
.toolbox {
  height: 100vh;
  position: absolute;
  background-color: rgba(24, 24, 24, 0.9);
  z-index: 3;
}
.toolbox .group .items {
  width: 24px;
  height: 24px;
  padding: 10px;
}
.toolbox .group .items img {
  width: 100%;
  -webkit-filter: invert(100%);
          filter: invert(100%);
}
.toolbox .group .items:hover {
  background-color: #353535;
  cursor: pointer;
}
.toolbox.left {
  left: 0;
  top: 0;
}
.toolbox.left .group {
  border-bottom: 1px solid gray;
}
.toolbox.right {
  right: 0;
  top: 0;
}
.toolbox.right .group {
  border-bottom: 1px solid gray;
}
.toolbox.right .fillColors {
  margin-right: 0;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}
.toolbox.right .fillColors .items {
  display: inline-block;
  width: 40px;
  height: 30px;
  padding: 14px;
}
.toolbox.right .fillColors .items:hover {
  background-color: #353535;
  cursor: pointer;
}
.toolbox.right .fillColors .items .input_color {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  padding: 0;
  border: 0 solid #ffffff;
  cursor: pointer;
}
.toolbox.left .colors {
  margin-left: 0;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}
.toolbox.left .colors .items {
  display: inline-block;
  width: 40px;
  height: 30px;
  padding: 14px;
}
.toolbox.left .colors .items:hover {
  background-color: #353535;
  cursor: pointer;
}
.toolbox.left .colors .items .input_color {
  -webkit-box-sizing: border-box;
          box-sizing: border-box;
  width: 100%;
  height: 100%;
  padding: 0;
  border: 0 solid #ffffff;
  cursor: pointer;
}
#canvas {
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
          transform: translate(-50%, -50%);
  background-color: whitesmoke;
  z-index: 1;
}
#canvas:hover {
  cursor: crosshair;
}
#canvas_container {
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
          transform: translate(-50%, -50%);
  width: 95%;
  height: 92%;
}
.canvasOpen {
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  z-index: 3;
  background-color: rgba(0, 0, 0, 0.93);
  color: white;
  padding: 10px;
}
.canvasOpen button {
  margin: 20px 10px;
  display: block;
}
.canvasOpen input {
  margin: 20px 10px;
  display: block;
}
</style>
