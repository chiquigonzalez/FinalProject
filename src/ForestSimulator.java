import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ForestSimulator {
private static Scanner scanner = new Scanner(System.in);
private static ArrayList<Forest> forests = new ArrayList<>();
private static Random random = new Random();

    public static void main(String[] args) {

        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");

        if (args.length == 0) {
            System.out.println("No forests to manage. Exiting program.");
            return;
        } // exit once all forests are managed

        int forestIndex = 0;

        try {
            Forest forest = loadForest(args[forestIndex]);
            forests.add(forest);
            System.out.println("Initializing from " + args[forestIndex]);
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error initializing forest from file: " + args[forestIndex] + ".csv");
            e.printStackTrace();
        }

        while (true) {
            Forest presentForest = forests.get(forestIndex);
            System.out.print("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it : ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty()) continue;
            char choice = input.charAt(0);

            switch (choice) {
                case 'P':
                    System.out.println();
                    printForestInfo(presentForest);
                    System.out.println();
                    break;
                case 'A':
                    addOneRandomTree(presentForest);
                    System.out.println();
                    break;
                case 'C':
                    cutTree(presentForest);
                    System.out.println();
                    break;
                case 'G':
                    presentForest.growForest();
                    System.out.println();
                    break;
                case 'R':
                    reapTrees(presentForest);
                    System.out.println();
                    break;
                case 'S':
                    saveForest(presentForest);
                    System.out.println();
                    break;
                case 'L':
                    System.out.print("Enter forest name: ");
                    String filename = scanner.nextLine().trim(); // ask for which forest to load next
                    Forest newForest = loadNewForest(filename);

                    if (newForest != null) {
                        forests.set(forestIndex, newForest);
                    } else {
                        System.out.println("Old forest retained");
                    }

                    System.out.println();
                    break;
                case 'N':
                    System.out.println("Moving to the next forest");
                    int newForestIndex = (forestIndex + 1) % args.length; // calculate the index of the next forest
                    while (true) {
                        if (newForestIndex >= args.length) {
                            System.out.println("No more forests to load. Returning to the first forest.");
                            newForestIndex = 0;
                        } // go back to first forest if needed
                        System.out.println("Initializing from " + args[newForestIndex]);

                        try {
                            Forest nextForest = loadForest(args[newForestIndex]);
                            forests.add(nextForest);
                            forestIndex = forests.size() - 1; // update forestIndex
                            System.out.println();
                            break;
                        } catch (Exception e) {
                            System.out.println("Error opening/reading " + args[newForestIndex] + ".csv");
                            newForestIndex = (newForestIndex + 1) % args.length; // move to the next forest
                            if (newForestIndex == forestIndex) { // if we've tried all forests and none could be loaded
                                System.out.println("No valid forests could be loaded. Exiting program.");
                                return;
                            } // input validation for forest name
                        }
                    }
                    break;
                case 'X':
                    System.out.println();
                    System.out.println("Exiting the Forestry Simulation");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break; // if user does not enter valid option, must make user try again
            }
        } // end of switch method

    } // end of main method

private static void printForestInfo(Forest forest) {
    ArrayList<Tree> trees = forest.getTrees();
    System.out.println("Forest name: " + forest.getName());
    double totalHeight = 0;
    int index = 0;

    for (index = 0; index < trees.size(); index++) {
        Tree tree = trees.get(index);
        totalHeight += tree.getHeight();
        System.out.printf("  %d %-7s %d %7.2f' %5.1f%%\n", index, tree.getSpecies(), tree.getYearPlanted(), tree.getHeight(), tree.getGrowthRate());
    }

    double averageHeight;
    averageHeight = trees.size() > 0 ? totalHeight / trees.size() : 0;
    System.out.printf("There are %d trees, with an average height of %.2f\n", trees.size(), averageHeight);
} // end of printForestInfo method

private static void addOneRandomTree(Forest forest) {
    Tree tree = new Tree(Species.values()[random.nextInt(Species.values().length)],
                2010, 10 + random.nextDouble() * 10, 10 + random.nextDouble() * 10);
    forest.addTree(tree);
} // end of addOneRandomTree method

private static void cutTree(Forest forest) {
    while (true) {
        System.out.print("Tree number to cut down: ");
        String input = scanner.nextLine();
        try {
            int index = Integer.parseInt(input);
            if (index >= 0 && index < forest.getTrees().size()) {
                forest.cutTree(index);
                return; // return after successful operation
            } else {
                System.out.println("Tree number " + index + " does not exist");
                return;
            } // ensure tree number exists in array
        } catch (NumberFormatException e) {
            System.out.println("That is not an integer");
        } // make sure input is a number
    }
} // end of cutTree method

private static void reapTrees(Forest forest) {
    while (true) {
        System.out.print("Height to reap from: ");
        String input = scanner.nextLine();
        try {
            double height = Double.parseDouble(input);
            List<Tree[]> reapedTrees = forest.reapTrees(height);
            for (Tree[] reapedTree : reapedTrees) {
                System.out.printf("Reaping the tall tree %-7s %d %7.2f' %5.1f%%\n", reapedTree[0].getSpecies(), reapedTree[0].getYearPlanted(), reapedTree[0].getHeight(), reapedTree[0].getGrowthRate());
                System.out.printf("Replaced with new tree %-7s %d %7.2f' %5.1f%%\n", reapedTree[1].getSpecies(), reapedTree[1].getYearPlanted(), reapedTree[1].getHeight(), reapedTree[1].getGrowthRate());
            } // replace the old tree with a new tree using a different index
            return;
        } catch (NumberFormatException e) {
            System.out.println("That is not an integer");
        } // ensure input is a number
    }
} // end of reapTrees method

private static Forest loadForest(String filename) throws IOException {
    Forest forest = new Forest(filename.replace(".csv", ""));
    try (BufferedReader reader = new BufferedReader(new FileReader("src/" + filename + ".csv"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            int yearPlanted;
            double height;
            double growthRate;

            String[] parts = line.split(",");
            Species species = Species.valueOf(parts[0].toUpperCase());

            yearPlanted= Integer.parseInt(parts[1]);
            height = Double.parseDouble(parts[2]);
            growthRate = Double.parseDouble(parts[3]);

            forest.addTree(new Tree(species, yearPlanted, height, growthRate));
        }
    }
    return forest;
} // end of loadFprest method

private static void saveForest(Forest forest) {
    String filename = forest.getName() + ".db";

    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
        out.writeObject(forest);
    } catch (IOException e) {
        System.out.println("Error saving the forest to file: " + e.getMessage());
    }
} // end of saveForest method

private static Forest loadNewForest(String filename) {
    Forest forest = null;
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename + ".db"))) {
        forest = (Forest) in.readObject();
        System.out.println("Forest '" + filename + "' has been loaded.");
    } catch (IOException e) {
        System.out.println("Error opening/reading " + filename + ".db");
    } catch (ClassNotFoundException e) {
        System.out.println("Error in data format: " + e.getMessage());
    } // make sure all input is correct formatting
    return forest;
    }
} // end of loadNewForest method