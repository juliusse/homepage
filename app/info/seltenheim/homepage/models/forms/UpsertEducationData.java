package info.seltenheim.homepage.models.forms;

import play.data.validation.Constraints.Required;

public class UpsertEducationData extends UpsertPositionData {

    @Required
    private String degreeDe;
    @Required
    private String degreeEn;
    @Required
    private String score;

    public String getDegreeDe() {
        return degreeDe;
    }

    public void setDegreeDe(String degreeDe) {
        this.degreeDe = degreeDe;
    }

    public String getDegreeEn() {
        return degreeEn;
    }

    public void setDegreeEn(String degreeEn) {
        this.degreeEn = degreeEn;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
