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
public class GA implements Runnable {
    private Individual population[];
    private int popSize;
    private int iterations;

    public GA(int popSize, int iterations) {
        this.population = null;
        this.popSize = popSize;
        this.iterations = iterations;
    }
    
    @Override
    public void run() {
        // INITIALISE population
        // EVALUATE population fitness
        // Iterate until termination condition
        //  SELECT parents (fitness proportionate)
        //  CROSSOVER
        //  MUTATION
        //  EVALUATE offspring fitness
        //  SELECT offspring for next generation (replace)
        
        population = new Individual[popSize];
        Arrays.fill(population, new Individual());
        
        int popFitness = 0;
        for(Individual i : population) {
            popFitness += i.fitness();
        }
        System.out.println("initial popFitness: " + popFitness);
        
        Individual population2[] = new Individual[popSize];
        Individual population3[] = new Individual[popSize];
        Individual population4[] = new Individual[popSize];
        for(int i = 0; i < iterations; i++) {
            int T = 2;
            int tournament[] = new int[T];
            for (int j = 0; j < popSize; j++) {
                for (int k = 0; k < T; k++) {
                    tournament[k] = (int)(Math.random() * popSize);
                }
                
                if (population[tournament[0]].getFitness() > population[tournament[1]].getFitness()) {
                    population2[j] = population[tournament[0]];
                } else {
                    population2[j] = population[tournament[1]];
                }
            }
            
            // Probability: 0.6 to 0.9
            for(int j = 0; j < popSize; j += 2) {
                if(Math.random() < 0.6) {
                    Individual temp[] = crossover(population2[j], population2[j+1]);
                    population3[j] = temp[0];
                    population3[j+1] = temp[1];
                } else {
                    population3[j] = population2[j];
                    population3[j+1] = population2[j+1];
                }
            }
            
            // Probability: 1/pop size to 1/chromosome length
            for(int j = 0; j < popSize; j++) {
                if(Math.random() < 1.0/popSize) {
                    population4[j] = mutation(population3[j]);
                } else {
                    population4[j] = population3[j];
                }
            }
            
            popFitness = 0;
            for(Individual j : population4) {
                popFitness += j.fitness();
            }
            System.out.println("iteration "+i+" popFitness: " + popFitness);
            
            population = population4;
        }
    }
    
    public Individual[] crossover(Individual a, Individual b) {
        Individual temp[] = new Individual[2];
        Arrays.fill(temp, new Individual());
        
        int point = (int)(Math.random() * a.getGenome().length);
        for(int i = 0; i < a.getGenome().length; i++) {
            if(i < point) {
                temp[0].getGenome()[i] = a.getGenome()[i];
                temp[1].getGenome()[i] = b.getGenome()[i];
            } else {
                temp[0].getGenome()[i] = b.getGenome()[i];
                temp[1].getGenome()[i] = a.getGenome()[i];
            }
        }
        
        return temp;
    }
    
    public Individual mutation(Individual a) {
        Individual temp = new Individual();
        System.arraycopy(a.getGenome(), 0, temp.getGenome(), 0, a.getGenome().length);
        
        int point = (int)(Math.random() * temp.getGenome().length);
        temp.getGenome()[point] = temp.getGenome()[point] == 0 ? 1 : 0;
        
        return temp;
    }
}
