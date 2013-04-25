package models;



public class Skill {

    private Long id;
    
    private String name;
    private double knowledge;
    private SkillGroup skillGroup;
    
    public Skill() {
        
    }
    
    public Skill(String name, double knowledge, SkillGroup skillGroup) {
        super();
        this.name = name;
        this.knowledge = knowledge;
        this.skillGroup = skillGroup;
    }
    
    public Skill(String name, double knowledge) {
        this(name,knowledge,null);
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SkillGroup getSkillGroup() {
        return skillGroup;
    }

    public void setSkillGroup(SkillGroup skillGroup) {
        this.skillGroup = skillGroup;
    }
     
    
}
