/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis.module.colourtracking;

import jarvis.module.Module;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Hebron
 */
public class ColourTrackingModule extends Module implements Runnable {
    private VideoCapture cap;
    private JFrame frame;
    private Panel panel;
    private boolean running;
    private final boolean val;
    
    public ColourTrackingModule() {
        System.out.println("Initialising colour tracking module...");
        
        // Load native library
        System.loadLibrary("opencv_java2410"); 
        
        // Initialise webcam
        cap = new VideoCapture(0);
        
        // Initialise window
        frame = new JFrame("untitled");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        panel = new Panel();
        frame.setContentPane(panel);
        frame.setVisible(true);
        running = true;
        
        val = true;
        
        System.out.println("done");
    }

    @Override
    public void shutdown() {
        System.out.println("Shutting down colour tracking module...");
        
        running = false;
        cap.release(); cap = null;
        frame.dispose(); frame = null;
        
        System.out.println("done");
    }

    @Override
    public void run() {
        while(running) {
            // Get webcam image
            Mat imageOriginal = new Mat();
            if(cap != null) cap.read(imageOriginal);
            // Convert from BGR to HSV
            Mat imageHSV = new Mat();
            Imgproc.cvtColor(imageOriginal, imageHSV, Imgproc.COLOR_BGR2HSV);
            // Threshold
            Mat imageThresholded = new Mat();
            Core.inRange(imageHSV, new Scalar(69, 128, 118), new Scalar(115, 255, 255), imageThresholded);
            // Remove small objects in the foreground 'morphological opening'
            Mat structure = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
            Imgproc.erode(imageThresholded, imageThresholded, structure);
            Imgproc.dilate(imageThresholded, imageThresholded, structure);
            // Remove holes in the foreground 'morphological closing'
            Imgproc.dilate(imageThresholded, imageThresholded, structure);
            Imgproc.erode(imageThresholded, imageThresholded, structure);
            
            Mat2Image mat2image = new Mat2Image();
            //if(cap != null) frame.getContentPane().getGraphics().drawImage(mat2image.getImage(imageOriginal), 0, 0, null);
            if(cap != null) frame.getContentPane().getGraphics().drawImage(mat2image.getImage(imageThresholded), 0, 0, null);
            if(cap != null) frame.repaint();
        }
    }
}
