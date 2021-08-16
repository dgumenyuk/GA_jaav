import java.util.Arrays;
import java.util.Random;

public class Problem {

    private final boolean[][] solutions;  // solutions are encoded as a list of boolean values.

    private final int size;
    private final int[][] itemCharacteristics; // the set of items to select from
    private final int populationSize;

    private final Solution[] population;

    private final int MUTATION_CHANCE = 10;  // mutation probability.

    /**
     * Method to generate initial random solutions.
     */
    private void onStartPopulation() {
        Random random = new Random();

        for (int i = 0; i < solutions.length; i++) {
            for (int j = 0; j < solutions[i].length; j++ ){
                solutions[i][j] = random.nextBoolean();
            }

        }
    }

    public Problem(int size, int[][] items, int populationSize) {

        this.size = size;
        this.itemCharacteristics = items;
        this.populationSize = populationSize;

        this.solutions = new boolean[populationSize][items.length];
        this.onStartPopulation();

        this.population = new Solution[populationSize];
        this.initializeValues();
    }


    /**
     * Method to apply the genetic search for a given number of iteration.
     * @param iteration the number of generation to run the algorithm for.
     */

    public void startGeneticAlgorithm(int iteration){
        Random rand = new Random();
        int record = 0;
        int numOfSteps = 0;


        for (int i = 0; i < iteration; i++){
            //  selecting parents

            int parent1_id = tournamentSelection();
            int parent2_id = tournamentSelection();

            boolean[][] children = crossbreeding(solutions[parent1_id], solutions[parent2_id]);

            boolean[] child1 = new boolean[solutions[0].length];
            boolean[] child2 = new boolean[solutions[0].length];

            //mutation
            if(rand.nextInt(100) < MUTATION_CHANCE){
                boolean[] mutChild1 = mutation(children[0]);
                child1 = mutChild1;

                boolean[] mutChild2 = mutation(children[1]);
                child2 = mutChild2;
            }

            int toKillId1 = population[0].id;
            int toKillId2 = population[1].id;
            solutions[toKillId1] = child1;
            solutions[toKillId2] = child2;

            recalculateValues(toKillId1, toKillId2);


            if(population[populationSize - 1].value > record){
                record = population[populationSize -1].value;
                int recordSize = population[populationSize - 1].size;
                numOfSteps = i;
                System.out.println("|Generation: " + i + "|Best sack valu: " + record + "|Size: " + recordSize + "|");
            }


        }
    }

    /**
     * Method to perform the binary tournament selection.
     * @return  the index of the selected individual.
     */
    private int tournamentSelection(){

        Random rand = new Random();
        int random1 = rand.nextInt(solutions.length);
        int random2 = rand.nextInt(solutions.length);

        if (population[random1].value >=  population[random2].value){
            return  random1;
        }else {
            return random2;
        }
    }

    /**
     * Method to perform the mutation of a given individual.
     * @param child the individual to mutate.
     * @return mutated individual.
     */
    private boolean[] mutation(boolean[] child){
        boolean[] newChild = child.clone();
        Random random = new Random();

        int randomGenId = random.nextInt(child.length);
        newChild[randomGenId] = !child[randomGenId];

        return newChild;
    }

    /**
     * Method to perform the one point crossover.
     * @param first the first parent.
     * @param second the second parent.
     * @return the list containing two children.
     */
    private boolean[][] crossbreeding(boolean[] first, boolean[] second){
        boolean[] child1 = new boolean[first.length];
        boolean[] child2 = new boolean[second.length];

        Random random = new Random();
        int crossPoint = random.nextInt(first.length-1);

        System.arraycopy(first, 0, child1, 0, crossPoint);
        System.arraycopy(second, 0, child2, 0, crossPoint);

        System.arraycopy(second, crossPoint, child1, crossPoint, second.length - crossPoint);
        System.arraycopy(first, crossPoint, child2, crossPoint, first.length - crossPoint);

        boolean[][] result = new boolean[2][first.length];
        result[0] = child1;
        result[1] = child2;

        return result;
    }

    /**
     * Method to recalculate the new fitness value of the individual.
     * @param first_index index of the first new child.
     * @param second_index index of the second new child.
     */
    private void recalculateValues(int first_index, int second_index){
        int value = 0;
        int size = 0;

        for (int i = 0; i < solutions[first_index].length; i++){
            if (solutions[first_index][i]){
                    value += itemCharacteristics[i][0];
                    size += itemCharacteristics[i][1];
            }
        }

        if (size > this.size){
            value = 0;  // discard the individuals that don't meet the requirements.
        }

        population[0] = new Solution(first_index, value, size);

        value = 0;
        size = 0;

        for (int i = 0; i < solutions[second_index].length; i++){
            if (solutions[second_index][i]){
                value += itemCharacteristics[i][0];
                size += itemCharacteristics[i][1];
            }

        }

        if (size > this.size){
            value = 0;
        }


        population[1] = new Solution(second_index, value, size);

        Arrays.sort(this.population);

    }

    /**
     * Method to calculate the fitness values of all the individuals in the population.
     */
    private void initializeValues() {

        Arrays.fill(population, null);

        for (int i = 0; i < solutions.length; i++) {
            int value = 0;
            int size = 0;
            for (int j = 0; j < solutions[i].length; j++) {
                if (solutions[i][j]) {
                    value += itemCharacteristics[j][0];
                    size += itemCharacteristics[j][1];
                }
            }

            if (size > this.size){
                value = 0;  // discard the individuals not satisfying the requirement.
            }

            population[i] = new Solution(i, value, size);
        }

        Arrays.sort(this.population);
    }


    /**
     * Class to represent the candidate solutions.
     */
    private static class Solution implements Comparable<Solution> {
        int value;
        int id;
        int size;


        public Solution(int i, int value, int size) {
            this.id = i;
            this.value = value;
            this.size = size;
        }

        @Override
        public int compareTo(Solution o) {
            return Integer.compare(this.value, o.value);
        }


        @Override
        public String toString() {
            return "Solution: " + this.id + " " + this.value + " " + this.size;

        }
    }
}