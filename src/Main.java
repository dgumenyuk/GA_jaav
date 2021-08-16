/**
 *     Implementation of the genetic algorithm to solve the Knapsack problem:
 *     given a set of items, each with a weight and a value, determine the number
 *     of each item to include in a sack so that the total weight is less than or
 *     to a given limit and the total value is as large as possible.
 *     The goal of the algorithm is to maximize the fitness function - the total value of
 *     all the items in the sack.
 */

import java.util.Random;

public class Main {

    public static void main(String[] args) {


        final int amountOfThings = 2500; // number of possible items to put in the sack
        final int maxItemvalue = 100;
        final int maxItemSize = 100;
        final int knapsackSize = 100000;  // the maximum total size of the items in the sack
        final int populationSize = 500;
        final int iteration = 100000;  // number of generations in the genetic algorithm


        int [][] items = new int[amountOfThings][2];

        generateItems(items, maxItemvalue, maxItemSize);  // generate possible items randomly

        Problem myProblem = new Problem(knapsackSize, items, populationSize);
        myProblem.startGeneticAlgorithm(iteration);  // start the genetic algorithm
    }

    /**
     *     Function to fill the given array with random values from the given range.
     *      @param items 2D array to fill.
     *      @param maxValue maximum possible value of an item.
     *      @param  maxSize maximum possible size of an item.
     */
    public static void generateItems(int[][] items, int maxValue, int maxSize){
        Random random = new Random();
        for (int i = 0; i < items.length; i++){
            int random_value = random.nextInt(maxValue);
            int random_size = random.nextInt(maxSize);

            items[i][0] = random_value;
            items[i][1] = random_size;
        }
    }
}
