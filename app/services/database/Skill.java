package services.database;

public class Skill {

    private String name;
    private double knowledge;

    public Skill() {

    }

    public Skill(String name, double knowledge) {
        super();
        this.name = name;
        this.knowledge = knowledge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(double knowledge) {
        this.knowledge = knowledge;
    }

}
