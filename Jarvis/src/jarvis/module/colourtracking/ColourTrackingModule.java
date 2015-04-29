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
import org.opencv.core.Mat;
import org.opencv.highgui.*;

/**
 *
 * @author Hebron
 */
public class ColourTrackingModule extends Module implements Runnable {
    private VideoCapture cap;
    private JFrame frame;
    private Panel panel;
    private boolean running;
    private Mat2Image mat2image;
    
    public ColourTrackingModule() {
        System.out.println("Initialising colour tracking module...");
        
        // Load native library
        System.loadLibrary("opencv_java2410"); 
        
        // Initialise webcam
        cap = new VideoCapture(0);
        mat2image = new Mat2Image();
        
        // Initialise window
        frame = new JFrame("untitled");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        panel = new Panel();
        frame.setContentPane(panel);
        frame.setVisible(true);
        running = true;
        
        System.out.println("done");
    }

    @Override
    public void shutdown() {
        System.out.println("Shutting down colour tracking module...");
        
        cap.release();
        frame.dispose();
        running = false;
        
        System.out.println("done");
    }

    @Override
    public void run() {
        while(running) {
            cap.read(mat2image.mat);
            frame.getContentPane().getGraphics().drawImage(mat2image.getImage(mat2image.mat), 0, 0, null);
            frame.repaint();
        }
    }
}
