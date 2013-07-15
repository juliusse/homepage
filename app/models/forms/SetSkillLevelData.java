package models.forms;

import play.data.validation.Constraints.Required;

public class SetSkillLevelData {

    @Required
    public double level;

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }
}
