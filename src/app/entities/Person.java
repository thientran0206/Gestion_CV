package app.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity(name = "Person")
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic(optional = false)
	@Column(name = "first_name", length = 200)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "name", length = 200)
	private String name;

	@Basic(optional = false)
	@Column(name = "email", length = 200)
	private String email;

	@Basic(optional = false)
	@Column(name = "web_site", length = 200)
	private String webSite;

	@Basic(optional = false)
	@Column(name = "pwd", length = 200)
	private String pwd;

	@Basic()
	@Temporal(TemporalType.DATE)
	@Column(name = "birth_day")
	private Date birthDay;

	public Person(String firstName, String name, String email, String webSite, Date birthDay, String pwd) {
		super();
		this.firstName = firstName;
		this.name = name;
		this.email = email;
		this.webSite = webSite;
		this.birthDay = birthDay;
		this.pwd = pwd;
	}

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY, mappedBy = "owner")
	private Set<Activity> activities = new HashSet<Activity>();

	@PreUpdate
	public void beforeUpdate() {
		System.err.println("PreUpdate of " + this);
	}

	@PostUpdate
	public void afterUpdate() {
		System.err.println("PostUpdate of " + this);
	}

	public Person() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "Person(id=" + getId() + "," + firstName + "," + name + "," + email + "," + webSite + "," + birthDay
				+ ","  + pwd + ")";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

}
