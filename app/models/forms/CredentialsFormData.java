package models.forms;

import play.data.validation.Constraints;

public class CredentialsFormData {

    @Constraints.Required
    private String username;
    @Constraints.Required
    private String password;

    public CredentialsFormData() {
    }

    public CredentialsFormData(String emailOrName, String password) {
        this.username = emailOrName;
        this.password = password;
    }

    

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
