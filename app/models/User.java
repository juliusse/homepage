package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.db.ebean.Model;

@Entity
@Table(name="users")
public class User extends Model {

    private static final Model.Finder<Long,User> find = new Model.Finder<Long,User>(Long.class, User.class);

    @Id
	private Long id;
	private String username;
	private String email;
	@JsonIgnore
	private String password;	

	public User() {
        
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static List<User> findAll() {
        return find.all();
    }


    public static User findById(Long id) {
        return find.byId(id);
    }

    public static User findByUsername(final String username) {
        return find.where().ieq("username", username).findUnique();
    }
    
    public static User findByEmail(final String email) {
    	return find.where().ieq("email", email).findUnique();
    }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
