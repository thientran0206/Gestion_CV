package app.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;


import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.dao.ActivityManager;
import app.dao.PersonManager;
import app.entities.Activity;
import app.entities.Nature;
import app.entities.Person;

public class TestActivityManager {
	static EJBContainer container;
	static ActivityManager activityManager;
	
	static PersonManager personManager;

	@BeforeClass
	public static void beforeAll() throws NamingException {
		final String name = "java:global/liste_CV/ActivityManager";
		final String name1 = "java:global/liste_CV/PersonManager";
		container = EJBContainer.createEJBContainer();
		activityManager = (ActivityManager) container.getContext().lookup(name);
		personManager = (PersonManager) container.getContext().lookup(name1);
	}

	@AfterClass
	public static void afterAll() {
		container.close();
	}

	@Before
	public void beforeTest() {
		// pour plus tard
	}

	@After
	public void afterTest() {
		// pour plus tard
	}
	@Test
	public void testSaveActivity() {
		Activity activity = new Activity();
		activity.setYear(new Date());
		activity.setNature(Nature.EXPERIENCES_PROFESSIONNELLES);
		activity.setTitle("Développeur application Hybride");
		activity.setDescription("Analyse des besoins");
		activity.setWebAddress("https://github.com/aminenasseh/Gestion-Cvs");
		Person person = new Person();
		Person findPerson = new Person();
		person.setId(1);
		findPerson = personManager.findPerson(person.getId());
		activity.setOwner(findPerson);
		activityManager.saveActivity(activity);
		System.out.println(activityManager.findActivities());
}
	@Test
	public void testFindOneActivity() {
		Activity findActivity = activityManager.findOneActivity("Développeur application Hybride");
		assertEquals("Développeur application Hybride", findActivity.getTitle());
}
	@Test
	public void testFindActivity() {
		assertNotNull(activityManager.findActivities());
		assertEquals(1, activityManager.findActivities().size());
}
	@Test
	 public void testdeleteOneActivity() {
	 Activity activity = activityManager.findOneActivity("Développeur application Hybride");
	 String titleActivity = activity.getTitle();
	 activityManager.deleteOneActivity(titleActivity);
}
}
