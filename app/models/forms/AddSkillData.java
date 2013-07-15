package models.forms;

import play.data.validation.Constraints.Required;

public class AddSkillData {

    @Required
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
