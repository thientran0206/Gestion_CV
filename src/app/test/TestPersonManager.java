package app.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.dao.ActivityManager;
import app.dao.PersonManager;
import app.entities.Person;

public class TestPersonManager {

	static EJBContainer container;
	static PersonManager personManager;

	@BeforeClass
	public static void beforeAll() throws NamingException {
		final String name = "java:global/liste_CV/ActivityManager";
		final String name1 = "java:global/liste_CV/PersonManager";
		container = EJBContainer.createEJBContainer();
		//activityManager = (ActivityManager) container.getContext().lookup(name);
		personManager = (PersonManager) container.getContext().lookup(name1);
	}
	@Before
    public void setUp() throws Exception {
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
    }

    @After
    public void tearDown() throws Exception {
        EJBContainer.createEJBContainer().close();
    }
	

	/*@Test
	public void testFindById() {
		Person person = new Person("TRANT", "TrungTThien", "sds@ddddd", "ww.wssww", new Date(), "ttyt");
		person.setId(1);
		pm.addPerson(person);
		//assertEquals(1, pm.findPerson(person.getId()).getName());
		System.out.println("Id de la personne:"+pm.findPerson(person.getId()));
	}*/
	@Test
	public void testFindAllAndRemove() {
		Person person1 = new Person("Amin","Daher","ad@ddd","www.sss",new Date(),"aaa");
		Person person2 = new Person("TRAN", "TrungThien", "sds@ddd", "ww.www", new Date(), "ttt");
		personManager.addPerson(person1);
		personManager.addPerson(person2);
		System.out.println(personManager.findAllPersons());
		
		personManager.removePerson(person1);
		System.out.println(personManager.findAllPersons());
	}
}
