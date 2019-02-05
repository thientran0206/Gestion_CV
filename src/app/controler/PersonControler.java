package app.controler;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.github.javafaker.Faker;

import app.dao.ActivityManager;
import app.dao.PersonManager;
import app.entities.Activity;
import app.entities.Nature;
import app.entities.Person;

@ManagedBean(name = "personne")
@SessionScoped
public class PersonControler {

	@EJB
	PersonManager pm;
	@EJB
	ActivityManager am;

	Person thePerson = new Person();

	public Person getAuthPerson() {
		return authPerson;
	}

	public void setAuthPerson(Person authPerson) {
		this.authPerson = authPerson;
	}

	Person authPerson = new Person();// pour la partie connection

	@PostConstruct
	public void init() {
		System.out.println("Create " + this);
		if (pm.findAllPersons().size() == 0) {
			Faker faker = new Faker(new Locale("fr"));
			Person p1 = new Person("TRAN", "TrungThien", "thientran@gmail.com", "www.thientran.com", new Date(), "123");
			pm.addPerson(p1);
			for (int i = 0; i < 100; i++) {
				String nom = faker.name().firstName();
				String prenom = faker.name().lastName();
				String email = faker.internet().safeEmailAddress(nom + prenom);
				Person p2 = new Person(prenom, nom, email, faker.internet().url(), faker.date().birthday(18, 65),
						"123");
				pm.addPerson(p2);
			}
			Activity activity = new Activity();
			activity.setYear(new Date());
			activity.setNature(Nature.PROJET);
			activity.setTitle("Développer un web de gestion CV");
			activity.setDescription("Projet de AA");
			activity.setWebAddress("https://github.com/thientran0206/Gestion_CV");
			activity.setOwner(pm.findPerson(p1.getId()));
			am.saveActivity(activity);
		}
	}

	/*
	 * *************************Person********************************************
	 */
	public List<Person> getPersonnes() {
		return pm.findAllPersons();
	}

	public Person getThePerson() {
		return thePerson;
	}

	public String show(Long n) {
		thePerson = pm.findPerson(n);
		return "showPerson?faces-redirect=true";
	}

	public String save() {
		pm.addPerson(thePerson);
		return "showPerson";
	}

	public String newPerson() {
		thePerson = new Person();
		return "editPerson?faces-redirect=true";
	}

	public List<Activity> getActivitiesPerson() {
		if (thePerson != null) {
			return pm.findActivitiesPerson(thePerson);
		}
		return null;
	}

	/* *************************Login******************************************** */
	public String login() {
		if (am.login(authPerson.getEmail(), authPerson.getPwd()) != null) {
			authPerson = am.getAuthPerson();
			return "accueil?faces-redirect=true";
		}
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Identifiants incorrects", "");
		FacesContext.getCurrentInstance().addMessage(null, fm);

		return null;
	}

	public String logOut() {
		authPerson = am.logout();
		return "accueil";
	}

	/*
	 * *******************************Activities************************************
	 * **
	 */

	private Activity activity = new Activity();

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String ajouter() {
		activity.setOwner(pm.findPerson(authPerson.getId()));
		am.saveActivity(activity);
		System.out.println(activity.getOwner());
		System.out.println(authPerson.getActivities());
		activity = new Activity();
		return "showPerson";
	}

}
