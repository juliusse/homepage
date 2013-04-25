package models;

import service.database.CouchDBDatabaseService;


public class User extends ModelBase {

	private String username;
	private String email;
	private String password;	

	public User() {
        setType("user");
    }

    public User(String username, String email, String password) {
        setType("user");
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static User findByUsername(final String username) {
        return CouchDBDatabaseService.getUserByName(username);//find.where().ieq("username", username).findUnique();
    }
    
    public static User findByEmail(final String email) {
    	return CouchDBDatabaseService.getUserByMail(email);
    }
    
    public static User findByNameAndPassword(final String username, final String password) {
        return CouchDBDatabaseService.getUserByNameAndPassword(username, password);
    }


    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
