package Interface;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Identifier.ShapeIdentifier;


public class GUIManager extends JPanel
						implements ActionListener {
	JButton openButton, webCamButton, identifyButton, cropButton;
	ImagePanel imgViewer = new ImagePanel();
    JFileChooser fc;
    //JTextArea resultViewer = new JTextArea();
    static BufferedImage srcImage;
    ImageIcon imgIco;

    public GUIManager() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.


        //Create a file chooser
        fc = new JFileChooser();


        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Télécharger");
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        webCamButton = new JButton("Prendre un photo via WebCam");
        webCamButton.addActionListener(this);
        
        identifyButton = new JButton("Validate");
        identifyButton.addActionListener(this);
        identifyButton.setEnabled(false);
        
        cropButton = new JButton("Couper");
        cropButton.addActionListener(this);
        cropButton.setEnabled(false);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(webCamButton);
        buttonPanel.add(identifyButton);
        buttonPanel.add(cropButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_END);
        add(imgViewer, BorderLayout.CENTER);
        
    }

    public void actionPerformed(ActionEvent e) {

        
        if (e.getSource() == openButton) { //Open button handler
            int returnVal = fc.showOpenDialog(GUIManager.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File file = fc.getSelectedFile();
                try {
                	srcImage = ImageIO.read(file);
                	setImage();
                    identifyButton.setEnabled(true);
                    cropButton.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

            } 
        } else if (e.getSource() == webCamButton)  {   //Webcam button handler      		
                new GetWebcamImage();
                srcImage = GetWebcamImage.captureFrame();
                setImage();
                identifyButton.setEnabled(true);
                cropButton.setEnabled(true);
        } else if (e.getSource() == identifyButton) { //Validate button handler
            if (srcImage != null) {        		
            	ShapeIdentifier identify = new ShapeIdentifier(srcImage);
        		srcImage = identify.getBnWImg();
        		setImage();
        		String result = identify.identifyShape();
        		JOptionPane.showMessageDialog(null, result,"Result", JOptionPane.INFORMATION_MESSAGE);
            }
        }	else if (e.getSource() == cropButton) { //Crop button handler
            if (srcImage != null) {        		
        		srcImage = imgViewer.crop();
        		setImage();
            }
        }

    }
    
    private void setImage(){
    	imgViewer.removeAll(); //clean viewer
    	imgViewer.setImage(srcImage); //set new image
    	imgViewer.add(new JLabel(new ImageIcon(srcImage))); //diplay new image
    	revalidate(); //update UI
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Shape Finder");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new GUIManager());

        //Display the window.
        frame.setSize(600,600);

        frame.setVisible(true);
    }

    public static void showUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}

