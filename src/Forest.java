import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Tree> trees;

    public Forest(String name) {
        this.name = name;
        this.trees = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public void cutTree(int index) {
        if (index >= 0 && index < trees.size()) {
            trees.remove(index);
        }
    }

    public void growForest() {
        for (Tree tree : trees) {
            tree.growthInOneYear();
        }
    }

    public List<Tree[]> reapTrees(double height) {
        int index;
        List<Tree[]> reapedTrees = new ArrayList<>();
        for (index = 0; index < trees.size(); index++) {
            if (trees.get(index).getHeight() > height) {
                Tree oldTree = trees.get(index);
                Tree newTree = new Tree(Species.values()[new Random().nextInt(Species.values().length)],
                        2010, 10 + new Random().nextDouble() * 10, 10 + new Random().nextDouble() * 10);
                trees.set(index, newTree);
                reapedTrees.add(new Tree[]{oldTree, newTree});
            }
        }
        return reapedTrees;
    }

} // end of forest class
