package Interface;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	
    JLabel meta = new JLabel();
    private BufferedImage img;
    private int x1, y1, x2, y2;
    private int imgY, imgX;
    private Rectangle rect = new Rectangle(0,0,0,0);
    Boolean cropping = false;
    
    public ImagePanel() {
    	super(new BorderLayout());
        this.addMouseListener(this);
        this.addMouseMotionListener(this);  
    }
    
    
    public void setImage(BufferedImage img)
    {
        this.img = img;
	    Point frameBegin = this.getLocationOnScreen();
    	imgX = frameBegin.x + ((this.getWidth() - this.img.getWidth())/2);
    	imgY = frameBegin.y + ((this.getHeight() - this.img.getHeight())/2) -8;
    	add(meta, BorderLayout.PAGE_END);
    }

    public BufferedImage getImage()
    {
        return this.img;
    }


	@Override
	public void mouseDragged(MouseEvent evt) { //Start marking from crop
		cropping = true;
	}

	@Override
	public void mouseMoved(MouseEvent evt) { //Display cursor location on screen
    	PointerInfo a = MouseInfo.getPointerInfo();
    	Point bWIndow = a.getLocation();

    	int x = (int) bWIndow.getX() - imgX;
    	int y = (int) bWIndow.getY() - imgY;
    	meta.setText(x + " × " + y);
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent evt) { //Get starting point
		rect = null;
		
    	PointerInfo a = MouseInfo.getPointerInfo();
    	Point bWIndow = a.getLocation();

    	this.x1 = (int) bWIndow.getX() - imgX;
    	this.y1 = (int) bWIndow.getY() - imgY;
	       
	}

	@Override
	public void mouseReleased(MouseEvent evt) { //Get ending point
		PointerInfo a = MouseInfo.getPointerInfo();
		Point bWIndow = a.getLocation();
       this.x2 = (int) bWIndow.getX() - imgX;
       this.y2 = (int) bWIndow.getY() - imgY;

       if (cropping) {
           rect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
    	   cropping = false;
       }
       revalidate();
	}
	
    public BufferedImage crop(){
    	try{
 	   		this.img = cropImage(rect);
 	   	} catch (Exception e){
 	   		
 	   	}    	
    	return this.img;
    }
    
    
    private BufferedImage cropImage(Rectangle rect) {
        BufferedImage dest = img.getSubimage(rect.x + 1, rect.y + 1, rect.width - 1, rect.height - 1); //Get cropped subimage 
        return dest; 
     }
    

}
