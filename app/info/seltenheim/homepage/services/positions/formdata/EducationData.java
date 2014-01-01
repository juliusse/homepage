package info.seltenheim.homepage.services.positions.formdata;

import play.data.validation.Constraints.Required;

public class EducationData extends PositionData {

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
