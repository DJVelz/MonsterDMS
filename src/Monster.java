/**
 * Dereck Velez Matias
 * CEN 3024C - Software Development I
 * Monster.java
 * The Monster class contains the monsters' attributes
 */

class Monster {

    //Attributes
    private String name;
    private String wyvernType;
    private int health;
    private String weakness;
    private double lowWeight;
    private double highWeight;

    /**
     * This constructs monster objects
     * @param name The name of a monster
     * @param wyvernType Determines if the monster is a Flying, Fanged, or Brute type
     * @param health The amount of health a monster has
     * @param weakness What element the monster is weakest to
     * @param lowWeight The lowest weight encountered
     * @param highWeight The highest weight encountered
     */
    public Monster(String name, String wyvernType, int health, String weakness, double lowWeight, double highWeight) {
        this.name = name;
        this.wyvernType = wyvernType;
        this.health = health;
        this.weakness = weakness;
        this.lowWeight = lowWeight;
        this.highWeight = highWeight;
    }

    //Getters
    public String getName() {
        return name;
    }
    public String getWyvernType() {
        return wyvernType;
    }
    public int getHealth() {
        return health;
    }
    public String getWeakness() {
        return weakness;
    }
    public double getLowWeight() {
        return lowWeight;
    }
    public double getHighWeight() {
        return highWeight;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setWyvernType(String wyvernType) {
        this.wyvernType = wyvernType;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }
    public void setLowWeight(double lowWeight) {
        this.lowWeight = lowWeight;
    }
    public void setHighWeight(double highWeight) {
        this.highWeight = highWeight;
    }

    @Override
    public String toString() {
        return name + "\nWyvern Type: " + wyvernType + "\nTotal HP: " + health + "\nWeakness: " + weakness +
                "\nWeight Range: " + lowWeight + "-" + highWeight;
    }
}
