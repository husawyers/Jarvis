/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis.module;

import java.util.ArrayList;

/**
 *
 * @author Hebron
 */
public class SemanticsModule extends Module {
    ArrayList<Discrete> discreteObjects;

    public SemanticsModule() {
        System.out.println("Initialising semantics module...");
        
        this.discreteObjects = new ArrayList<>();
        
        System.out.println("done");
    }
    
    @Override
    public void shutdown() {
        System.out.println("Shutting down semantics module...");
        
        for (Discrete discreteObject : discreteObjects) {
            discreteObject.dispose();
        }
        discreteObjects.clear();
        
        System.out.println("done");
    }
    
    // Simple first-order logic language processing
    public String listen(String what) {
        String o = null;
        
        String tokens[] = what.split(" ");
        // what is A
        if(tokens.length == 3 && tokens[0].equalsIgnoreCase("what") && tokens[1].equalsIgnoreCase("is")) {
            Discrete a = find(tokens[2].toLowerCase());
            if(a.getIsA().isEmpty()) {
                o = "I don't know";
            } else {
                // A is B
                if(a.getIsA().size() == 1) {
                    o = a.getName() + " is " + a.getIsA().get(0).getName();
                }
                // A is B and C
                else if(a.getIsA().size() == 2) {
                    o = a.getName() + " is " + a.getIsA().get(0).getName() + " and " + a.getIsA().get(1).getName();
                }
                // A is B, C, ..., and Z
                else {
                    ArrayList<Discrete> isARef = a.getIsA();
                    o = a.getName() + " is " + isARef.get(0).getName();
                    for (int i = 1; i < isARef.size() - 1; i++) {
                        o += ", " + isARef.get(i).getName();
                    }
                    o += "and " + isARef.get(isARef.size() - 1).getName();
                }
            }
        }
        // A is B
        if(tokens.length == 3 && tokens[1].equalsIgnoreCase("is")) {
            Discrete a = find(tokens[0]);
            Discrete b = find(tokens[2]);
            a.isA(b);
        }
        
        return o;
    }
    
    public Discrete find(String name) {
        Discrete d = null;
        
        // Find discrete object
        for (Discrete discreteObject : discreteObjects) {
            if(discreteObject.getName().equalsIgnoreCase(name)) {
                d = discreteObject;
            }
        }
        
        // If not found make a new discrete object
        if(d == null) {
            d = new Discrete(name);
            discreteObjects.add(d);
        }
        
        return d;
    }
}
