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
public class Discrete {
    private String name;
    private ArrayList<Discrete> isA;

    public Discrete(String name) {
        this.name = name;
        isA = new ArrayList<>();
    }
    
    public void dispose() {
        isA.clear();
    }
    
    public void isA(Discrete d) {
        isA.add(d);
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ArrayList<Discrete> getIsA() {
        return isA;
    }

    public void setIsA(ArrayList<Discrete> isA) {
        this.isA = isA;
    }
}
