/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis.module.colourtracking;

import jarvis.module.Module;
import java.awt.Font;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JSlider;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 *
 * @author Hebron
 */
public class ColourTrackingModule extends Module implements Runnable {
    private VideoCapture cap;
    private JFrame frame;
    private Panel panel;
    private JSlider lowH, lowS, lowV, highH, highS, highV;
    private boolean running;
    
    private Mat imageOriginal;
    private Mat imageHSV;
    private Mat imageThresholded;
    private Mat2Image mat2image;
    
    public ColourTrackingModule() {
        System.out.println("Initialising colour tracking module...");
        
        // Load native library
        System.loadLibrary("opencv_java2410"); 
        
        // Initialise webcam
        cap = new VideoCapture(0);
        
        // Initialise window
        frame = new JFrame("untitled");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480+50);
        panel = new Panel();
        frame.setContentPane(panel);
        lowH = new JSlider(JSlider.HORIZONTAL, 0, 179, 0); panel.add(lowH);
        lowS = new JSlider(JSlider.HORIZONTAL, 0, 255, 0); panel.add(lowS);
        lowV = new JSlider(JSlider.HORIZONTAL, 0, 255, 0); panel.add(lowV);
        highH = new JSlider(JSlider.HORIZONTAL, 0, 179, 0); panel.add(highH);
        highS = new JSlider(JSlider.HORIZONTAL, 0, 255, 0); panel.add(highS);
        highV = new JSlider(JSlider.HORIZONTAL, 0, 255, 0); panel.add(highV);
        frame.setVisible(true);
        running = true;
        
        imageOriginal = new Mat();
        imageHSV = new Mat();
        imageThresholded = new Mat();
        mat2image = new Mat2Image();
        
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
            if(cap != null) cap.read(imageOriginal);
            // Convert from BGR to HSV
            Imgproc.cvtColor(imageOriginal, imageHSV, Imgproc.COLOR_RGB2HSV);
            // Threshold
            Core.inRange(imageHSV, new Scalar(lowH.getValue(), lowS.getValue(), lowV.getValue()), new Scalar(highH.getValue(), highS.getValue(), highV.getValue()), imageThresholded);
            // Remove small objects in the foreground 'morphological opening'
            Mat structure = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
            Imgproc.erode(imageThresholded, imageThresholded, structure);
            Imgproc.dilate(imageThresholded, imageThresholded, structure);
            // Remove holes in the foreground 'morphological closing'
            Imgproc.dilate(imageThresholded, imageThresholded, structure);
            Imgproc.erode(imageThresholded, imageThresholded, structure);
            
            // Get the spatial moment
//            Moments moments = Imgproc.moments(imageThresholded);
//            int area = (int)moments.get_m00();
            
            Imgproc.cvtColor(imageThresholded, imageThresholded, Imgproc.COLOR_GRAY2RGB);
            if(cap != null) frame.getContentPane().getGraphics().drawImage(mat2image.getImage(imageThresholded), 0, 50, null);
            if(cap != null) frame.repaint();
        }
    }
}
