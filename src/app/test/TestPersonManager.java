package app.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.dao.PersonManager;
import app.entities.Person;

public class TestPersonManager {

	static EJBContainer container;
	static PersonManager dao;

	@BeforeClass
	public static void beforeAll() throws NamingException {
		final String name = "java:global/liste_CV/PersonManager";
		container = EJBContainer.createEJBContainer();
		dao = (PersonManager) container.getContext().lookup(name);
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
	public void testInject() {
		Assert.assertNotNull(dao);
	}

	/*@Test
	public void testFindById() {
		Person person = new Person("TRANT", "TrungTThien", "sds@ddddd", "ww.wssww", new Date(), "ttyt");
		dao.addPerson(person);
		System.out.println(dao.findPerson(person.getId()));
	}*/
	@Test
	public void testFindAllAndRemove() {
		Person person1 = new Person("Amin","Daher","ad@ddd","www.sss",new Date(),"aaa");
		Person person2 = new Person("TRAN", "TrungThien", "sds@ddd", "ww.www", new Date(), "ttt");
		dao.addPerson(person1);
		dao.addPerson(person2);
		System.out.println(dao.findAllPersons());
		
		dao.removePerson(person1);
		System.out.println(dao.findAllPersons());
	}
}
