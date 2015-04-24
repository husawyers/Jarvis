/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis;

import jarvis.module.Module;
import jarvis.module.SemanticsModule;
import jarvis.module.SpeechModule;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Hebron
 */
public class Jarvis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Initialise
        System.out.println("---JARVIS 1.0");
        SpeechModule mouth = new SpeechModule();
        SemanticsModule ears = new SemanticsModule();
        
        ArrayList<Module> modules = new ArrayList<>();
        modules.add(mouth);
        modules.add(ears);
        
        // Run in a input-process-outcome loop
        mouth.speak("Hello, I am Jarvis.");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            String output = ears.listen(input);
            mouth.speak(output);
            
            // Quit command
            if(input.equalsIgnoreCase("bye")) {
                mouth.speak("Bye bye.");
                break;
            }
        }
        
        // Cleanup
        System.out.println();
        for (Module module : modules) {
            module.shutdown();
        }
        modules.clear();
    }
}
