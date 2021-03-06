package app.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

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

	Person authPerson = new Person();// pour la partie connexion

	@PostConstruct
	public void init() {
		System.out.println("Create " + this);
		if (pm.findAllPersons().size() == 0) {
			Faker faker = new Faker(new Locale("en"));//Generer les informations avec Faker
			for (int i = 0; i < 20; i++) {
				String nom = faker.name().lastName();
				String prenom = faker.name().firstName();
				String email = faker.internet().safeEmailAddress(nom + prenom);
				Person p2 = new Person(prenom, nom, prenom+"." + nom+"@amu.fr","www."+prenom + nom +".com", faker.date().birthday(18, 65),
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
					activity.setWebAddress("www."+p2.getName()+p2.getFirstName()+".com");
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
	//Afficher le profil de la personne via son id
	public String show(Long n) {
		thePerson = pm.findPerson(n);
		return "showPerson?faces-redirect=true";
	}
	//pour ajouter nouvelle personne
	public String save() {
		pm.addPerson(thePerson);
		return "showPerson";
	}
	//pour mise a jour la personne
	public void savePerson() {
		thePerson=pm.updatePerson(thePerson);
		
	
	}
	//Creation la nouvelle personne
	public String newPerson() {
		thePerson = new Person();
		return "editPerson?faces-redirect=true";
	}
	//chercher toutes les activites de la personne
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
	 * *******************************Activity************************************
	 * **
	 */

	private Activity activity = new Activity();

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	//creer une instance de l'activite
	public void newActivity() {
		activity=new Activity();
	}
	// ajouter une activite
	public String ajouter() {
		activity.setOwner(pm.findPerson(authPerson.getId()));
		am.saveActivity(activity);
		System.out.println(activity.getOwner());
		System.out.println(authPerson.getActivities());
		thePerson = pm.findPerson(authPerson.getId());//pour mise a jour nouvelle activite
		activity = new Activity();
		return "showPerson";
	}
	/*
	 * *******************************Activities************************************
	 * **
	 */
	private List<Activity> activities = new ArrayList<Activity>();
	// pour chercher tout les activites
	public String getAllActivities() {
		activities=am.findActivities();
		return "listCV?faces-redirect=true";
		
	}
	
	
	public List<Activity> getActivities() {
		return activities;
	}

	public List<Activity> setActivities(List<Activity> activities) {
		this.activities = activities;
		return activities;
	}
	
	
	/* *************************Recherche******************************************** */
	
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
	
	/* *************************Modifier une Activite******************************************** */
	
	
	public String modify(Integer i) {
		activity=am.findActivityById(i);
		System.out.println(activity);
		return "modifyActivity?faces-redirect=true";
	}
	
	public void modifyActivity() {
		System.out.println(activity);
		activity = am.updateActivity(activity);
		thePerson = pm.findPerson(authPerson.getId());//pour si on clique le boutton quit dans modif il va mettre a jour profil person 
		
	}

	
	
}
