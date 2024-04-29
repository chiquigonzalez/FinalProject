import java.io.Serializable;
public class Tree implements Serializable {
    private static final long serialVersionUID = 1L;
    private Species species;
    private int yearPlanted;
    private double height;
    private double growthRate;

    public Tree(Species species, int yearPlanted, double height, double growthRate) {
        this.species = species;
        this.yearPlanted = yearPlanted;
        this.height = height;
        this.growthRate = growthRate;
    }


    public Species getSpecies() {
        return species;
    }

    public int getYearPlanted() {
        return yearPlanted;
    }

    public double getHeight() {
        return height;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void growthInOneYear() {
        height += height * growthRate / 100;
    }

} // end of single tree class
