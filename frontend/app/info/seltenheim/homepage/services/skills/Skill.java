package info.seltenheim.homepage.services.skills;

public class Skill implements Comparable<Skill> {

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

    @Override
    public int compareTo(Skill o) {
        if (this.getKnowledge() > o.getKnowledge()) {
            return 1;
        } else if (this.getKnowledge() < o.getKnowledge()) {
            return -1;
        } else {
            return 0;
        }
    }
}
