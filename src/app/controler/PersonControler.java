package app.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.fluttercode.datafactory.impl.DataFactory;

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
			for (int i = 0; i < 20; i++) {
				String nom = faker.name().firstName();
				String prenom = faker.name().lastName();
				String email = faker.internet().safeEmailAddress(nom + prenom);
				Person p2 = new Person(prenom, nom, email, faker.internet().url(), faker.date().birthday(18, 65),
						"123");
				pm.addPerson(p2);
				Nature[] nature = Nature.values();
				int nombre = faker.number().numberBetween(2, 8);
				for (int p = 0; p < nombre; p++) {
					Activity activity = new Activity();
					DataFactory df = new DataFactory();
					Date minDate = df.getDate(2000, 1, 1);
					Date maxDate = new Date();
					Random generator = new Random();
					activity.setYear(faker.date().between(minDate, maxDate));
					activity.setNature(nature[generator.nextInt(nature.length)]);
					activity.setTitle(faker.job().title());
					activity.setDescription(faker.job().keySkills());
					activity.setWebAddress(faker.internet().url());
					activity.setOwner(pm.findPerson(p2.getId()));
					am.saveActivity(activity);
				}
			}

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

	/* *************************Login&&Logout******************************************** */
	public String login() {
		if (am.login(authPerson.getEmail(), authPerson.getPwd()) != null) {
			authPerson = am.getAuthPerson();
			thePerson = pm.findPerson(authPerson.getId());
			return "showPerson?faces-redirect=true";
		}
		FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Identifiants incorrects", "");
		FacesContext.getCurrentInstance().addMessage(null, fm);

		return null;
	}

	public String logOut() {
		System.out.println(" <=============== Déconnexion ================>");
		authPerson = new Person();
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
		thePerson = pm.findPerson(authPerson.getId());//pour mise a jour nouvelle activite
		return "showPerson";
	}
	
	/* *************************Search******************************************** */
	
	private List<Person> resultSearch = new ArrayList<Person>();
	
	public List<Person> getResultSearch() {
		return resultSearch;
	}

	public void setResultSearch(List<Person> resultSearch) {
		this.resultSearch = resultSearch;
	}

	public String showResultSearch() {
		setResultSearch(pm.search(thePerson.getName()));
		return "search?faces-redirect=true";
	}
	
	/* *************************Modify Activity******************************************** */

	public String modify(Integer i) {
		activity=am.findActivityById(i);
		return "modifyActivity?faces-redirect=true";
	}
	public void modifyActivity() {
		activity=am.updateActivity(activity);
	}
	
	
	
}
