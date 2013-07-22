package models;

import org.joda.time.DateTime;



public class Education extends Position {

    private String degree;
    private String score;
    private String scale;
    private String degreeNote;
    

    public Education(DateTime fromDate, DateTime toDate, String place, String website, String score, String scale, String title, String degree, String degreeNote) {
        super(fromDate,toDate,place,title,website);
        
        this.degree = degree;
        this.score = score;
        this.scale = scale;
        
        this.degreeNote = degreeNote;
        
    }

   

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDegreeNote() {
        return degreeNote;
    }

    public void setDegreeNote(String degreeNote) {
        this.degreeNote = degreeNote;
    }
    
    
}
