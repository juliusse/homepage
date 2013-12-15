package info.seltenheim.homepage.models.forms;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class RegisterFormData {

	@Required
    @Email
	private String email;
	
	@Required
	private String password;

	public RegisterFormData() {
		
	}
	
	public RegisterFormData(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
