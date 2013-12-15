package info.seltenheim.homepage.models.forms;

import play.data.validation.Constraints.Required;

public class AddSkillGroupData {

    @Required
    public String nameDe;
    @Required
    public String nameEn;

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

}
