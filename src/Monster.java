class Monster {
    private String name;
    private String wyvernType;
    private int health;
    private String weakness;
    private double lowWeight;
    private double highWeight;

    public Monster(String name, String wyvernType, int health, String weakness, double lowWeight, double highWeight) {
        this.name = name;
        this.wyvernType = wyvernType;
        this.health = health;
        this.weakness = weakness;
        this.lowWeight = lowWeight;
        this.highWeight = highWeight;
    }

    public String getName() { return name; }
    public String getWyvernType() { return wyvernType; }
    public int getHealth() { return health; }
    public String getWeakness() { return weakness; }
    public double getLowWeight() { return lowWeight; }
    public double getHighWeight() { return highWeight; }

    public void setWyvernType(String wyvernType) { this.wyvernType = wyvernType; }
    public void setHealth(int health) { this.health = health; }
    public void setWeakness(String weakness) { this.weakness = weakness; }
    public void setLowWeight(double lowWeight) { this.lowWeight = lowWeight; }
    public void setHighWeight(double highWeight) { this.highWeight = highWeight; }

    @Override
    public String toString() {
        return name + " (" + wyvernType + ") - HP: " + health + ", Weakness: " + weakness +
                ", Weight: " + lowWeight + "-" + highWeight;
    }
}
