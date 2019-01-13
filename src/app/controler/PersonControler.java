package app.controler;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import app.dao.ActivityManager;
import app.dao.PersonManager;
import app.entities.Activity;
import app.entities.Nature;
import app.entities.Person;

@ManagedBean(name = "person")
@SessionScoped
public class PersonControler {

	@EJB
    PersonManager pm;
	@EJB
    ActivityManager am;

    Person person = new Person();

    @PostConstruct
    public void init() {
        System.out.println("Create " + this);
        if (pm.findAllPersons().size() == 0) {
            Person p1 = new Person("TRAN", "TrungThien", "thientran@gmail.com", "www.thientran.com", new Date(), "123");
            pm.addPerson(p1);
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
    /* *************************Person******************************************** */
    public List<Person> getPersonnes() {
        return pm.findAllPersons();
    }
    public Person getThePerson() {
        return person;
    }
    public String show(Long n) {
        person = pm.findPerson(n);
        return "showPerson";
    }
    public String save() {
        pm.addPerson(person);
        return "showPerson";
    }
    public String newPerson() {
        person = new Person();
        return "editPerson?faces-redirect=true";
    }
    
    public List<Activity> getActivitiesPerson() {
		if (person != null) {
			return pm.findActivitiesPerson(person);
		}
		return null;
}
    /* ********************************************************************* */
}
