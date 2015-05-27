/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis.GA;

import java.util.Arrays;

/**
 *
 * @author Hebron
 */
public class Individual {
    private int fitness;
    private int genome[];

    public Individual() {
        this.fitness = 0;
        this.genome = new int[8];
        
        // Initialise individual with random values (binary string)
        for(int i = 0; i < genome.length; i++) {
            genome[i] = (int)(Math.random() * 100) % 2;
        }
    }

    public int fitness() {
        // Fitness: binary value
        fitness = 0;
        for(int i = 0; i < 8; i++) {
            fitness += genome[8-i-1] * Math.pow(2, i);
        }
        
        return fitness;
    }
    
    // Getters and Setters
    public int[] getGenome() {
        return genome;
    }

    public int getFitness() {
        return fitness;
    }
}
