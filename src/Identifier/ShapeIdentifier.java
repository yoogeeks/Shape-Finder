package Identifier;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class ShapeIdentifier {
	
	BufferedImage img;
	Color[][] c;
	Point top = new Point(0,0);
	Point bottom = new Point(0,0);
	Point left = new Point(0,0);
	Point right = new Point(0,0);
	Point top_left = new Point(0,0);
	Point top_right = new Point(0,0);
	Point bottom_left = new Point(0,0);
	Point bottom_right = new Point(0,0);
   
    public ShapeIdentifier(BufferedImage image){
    	img = image;
    }
     
    public String identifyShape() {
        

        findCorners(); // Get vertices of the shape
        findEdgeCenters(); //form a rectangle around the shape
        
        System.out.println(top.x + " , " + top.y);
        System.out.println(bottom.x + " , " + bottom.y);
        System.out.println(left.x + " , " + left.y);
        System.out.println(right.x + " , " + right.y);
    	
    	//Check if all extreme points are distinct
    	if (!(isEqual(top.x, left.x, 10) && isEqual(top.y, left.y, 10)) && !(isEqual(top.x, right.x, 10) && isEqual(top.y, right.y, 10))
    			&& !(isEqual(bottom.x, right.x, 10) && isEqual(bottom.y, right.y, 10)) && !(isEqual(bottom.x, left.x, 10) && isEqual(bottom.y, left.y, 10))){

    		return checkByFourEdges();
    	}
    	//Check if the 2 extreme points are overlapping
    	else if ((isEqual(top.x, left.x, 10) && isEqual(top.y, left.y, 10)) && (isEqual(bottom.x, right.x, 10) && isEqual(bottom.y, right.y, 10))){
    		if (areOtherCorners()){ //try to find other corners by traversing image from other ends
    			return checkByFourEdges();
    		};
    	}

    
        return "Unknown";
    }

    
     
    private BufferedImage removeColor() {
    	BufferedImage bwImage = new BufferedImage(c.length, c[0].length, BufferedImage.TYPE_BYTE_BINARY);
        for(int x = 0; x < c.length; x++)
            for(int y = 0; y < c[x].length; y++)
            {
            	try{
                if (c[x][y].getRed() < 50) //Threshold value
                    c[x][y] = Color.BLACK;
                else
                    c[x][y] = Color.white;
                
            	bwImage.setRGB(x, y, c[x][y].getRGB());
            	} catch(Exception e) {}
            }
        img = bwImage;
        return bwImage;
    }
    
    void findLeft(){ //Leftmost drawn point in the image
    	for (int x = 0; x < img.getWidth() - 1; x++){
            for (int y = 0; y < img.getHeight() - 1; y++) {
                if (c[x][y] == Color.BLACK && x < img.getWidth()) {
                	left.x = x;
                	left.y = y;
                    return;
                }
           }
        }
    }
    
    void findRight(){//Rightmost drawn point in the image
    	for (int x = img.getWidth()-1; x > 0 ; x--){
            for (int y = img.getHeight()-1; y > 0; y--) {
                if (c[x][y] == Color.BLACK && x > -1) {
                	right.x = x;
                	right.y = y;
                    return;
                }
           }
        }
    }
    
    void findTop(){	//Topmost
        for (int y = 0; y < img.getHeight() - 1; y++) {
        	for (int x = 0; x < img.getWidth() - 1; x++){
                if (c[x][y] == Color.BLACK && x < img.getWidth()) {
                	top.x = x;
                	top.y = y;
                    return;
                }
           }
        }
    }
    
    void findBottom(){ //Bottom most
        for (int y = img.getHeight() - 1; y > 0 ; y--) {
        	for (int x = img.getWidth() - 1; x > 0 ; x--){
                if (c[x][y] == Color.BLACK && x < img.getWidth()) {
                	bottom.x = x;
                	bottom.y = y;
                    return;
                }
           }
        }
    }
    
    String checkByFourEdges(){
    	double TR, RB, BL, LT, TB, LR, TL_BR, BL_TR;
    	//Calculate distance between vertices 
    	TR = Math.sqrt((top.x-right.x)*(top.x-right.x) + (top.y-right.y)*(top.y-right.y));
    	RB = Math.sqrt((right.x-bottom.x)*(right.x-bottom.x) + (right.y-bottom.y)*(right.y-bottom.y));
    	BL = Math.sqrt((bottom.x-left.x)*(bottom.x-left.x) + (bottom.y-left.y)*(bottom.y-left.y));
    	LT = Math.sqrt((left.x - top.x)*(left.x - top.x) + (left.y - top.y)*(left.y - top.y));
    	
    	TB = Math.sqrt((top.x-bottom.x)*(top.x-bottom.x) + (top.y-bottom.y)*(top.y-bottom.y));
    	LR = Math.sqrt((left.x - right.x)*(left.x - right.x) + (left.y - right.y)*(left.y - right.y));
    	
    	TL_BR = Math.sqrt((top_left.x-bottom_right.x)*(top_left.x-bottom_right.x) + (top_left.y-bottom_right.y)*(top_left.y-bottom_right.y));
		BL_TR = Math.sqrt((bottom_left.x-top_right.x)*(bottom_left.x-top_right.x) + (bottom_left.y-top_right.y)*(bottom_left.y-top_right.y));
    	
    	if ((isEqual(TR, RB, 30)) && (isEqual(RB, BL, 30)) && (isEqual(BL, LT, 30)) && (isEqual(LT, TR, 30))) { //If all edges are equal
    		if (!isEqual(TB, LR, 25)){ //if distance between opposite vertices is different, its rhombus
    			if (isEqual(TL_BR, TR, 25) && isEqual(BL_TR, TR, 25))
    				return "Rhombus";
    		} else {
    			if ((isEqual(TL_BR, TR, 25) && isEqual(BL_TR, TR, 25)) || (isEqual(left.x, top_left.x, 10) && isEqual(left.y, top_left.y, 10))) //if distance between opposite edge centers is equal to edges, its square
    				return "Square";
    			else if (isEqual(TL_BR, TB, 30) && isEqual(BL_TR, LR, 30)) //if distance between opposite edge centers is equal to opposite vertices (diameter) its cirlce.
    				return "Circle";
    		}
    	} else if ((isEqual(TR, BL, 25)) && (isEqual(RB, LT, 25))){
    		if ((isEqual(TL_BR, TR, 25) && isEqual(BL_TR, LT, 25)) || (isEqual(left.x, top_left.x, 10) && isEqual(left.y, top_left.y, 10)))
    			return "Rectangle";
    		else if (isEqual(TL_BR, BL_TR, 25))
    			return "Elipse";
    	}
    	
    	
    	return "Unknown";
    }
    
    void findCorners(){
        findTop();
        findBottom();
        findLeft();
        findRight();
    }
    
    void findEdgeCenters(){
        Line2D line;        
        Point2D current;
        line= new Line2D.Double(left.x,top.y,right.x,bottom.y); //Conect corners of surrounding rectagle, and find its intersection point with shape
        for(Iterator<Point2D> iter = new LineIterator(line); iter.hasNext();) {
            current =iter.next();
            if ((c[(int)current.getX()][(int)current.getY()]) == Color.BLACK){
            	top_left.x = (int)current.getX();
            	top_left.y = (int)current.getY();
            	break;
            }         
        }
        line= new Line2D.Double(right.x,bottom.y,left.x,top.y);
        for(Iterator<Point2D> iter = new LineIterator(line); iter.hasNext();) {
            current =iter.next();
            if ((c[(int)current.getX()][(int)current.getY()]) == Color.BLACK){
            	bottom_right.x = (int)current.getX();
            	bottom_right.y = (int)current.getY();
            	break;
            }         
        }
        line= new Line2D.Double(left.x,bottom.y,right.x,top.y);
        for(Iterator<Point2D> iter = new LineIterator(line); iter.hasNext();) {
            current =iter.next();
            if ((c[(int)current.getX()][(int)current.getY()]) == Color.BLACK){
            	bottom_left.x = (int)current.getX();
            	bottom_left.y = (int)current.getY();
            	break;
            }         
        }
        line= new Line2D.Double(right.x,top.y,left.x,bottom.y);
        for(Iterator<Point2D> iter = new LineIterator(line); iter.hasNext();) {
            current = iter.next();
            if ((c[(int)current.getX()][(int)current.getY()]) == Color.BLACK){
            	top_right.x = (int)current.getX();
            	top_right.y = (int)current.getY();
            	break;
            }         
        }
    }
    
    Boolean isEqual(double n1, double n2, int window){ //Adjusting isEqual method, to check the equality up to a range
    	Double d = n1 - n2;
    	int temp = d.intValue();
    	if (-window < temp && temp < window)
    		return true;
    	return false;
    }
     
    Boolean areOtherCorners(){ //Traverse image from TOP-RIGHT and BOTTOM-LEFT corners to find other possible vertices
    	begin:
    	for (int x = img.getWidth()-1; x > 0 ; x--){
            for (int y = 0; y < img.getHeight()-1; y++) {
                if (c[x][y] == Color.BLACK && x > -1) {
                	top.x = x;
                	top.y = y;
                    break begin;
                }
           }
        }
    	begin:
    	for (int x = 0; x < img.getWidth()-1 ; x++){
            for (int y = img.getHeight()-1; y  > 0; y--) {
                if (c[x][y] == Color.BLACK && x > -1) {
                	bottom.x = x;
                	bottom.y = y;
                    break begin;
                }
           }
        }
    	
    	if (!isEqual(top.x, left.x, 10) || !isEqual(top.y, left.y, 10) && !isEqual(bottom.x, right.x, 10) || !isEqual(bottom.y, right.y, 10)){
    		return true;
    				
    	} else {
    			
    	}
    	
    	return false;
    }
    public BufferedImage getBnWImg() { //CHange colored image to black and white
    	int height = img.getHeight();
    	int width = img.getWidth();
    	c = new Color[width][height];
        for (int x = 0; x < width - 1; x++){
            for (int y = 0; y < height - 1; y++) { 
            	int clr = img.getRGB(x,y);
                int Red =(clr & 0x00ff0000)>> 16;
                int Green =(clr & 0x0000ff00)>>8;
                int Blue =clr & 0x000000ff;
                c[x][y] = new Color(Red, Green, Blue);
            }
        }
    	return removeColor();
    }
}
