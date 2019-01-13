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

import app.dao.PersonManager;
import app.entities.Person;

public class TestPersonManager {

	static EJBContainer container;
	@EJB
    PersonManager pm;
	static PersonManager dao;

	
	@Before
    public void setUp() throws Exception {
        EJBContainer.createEJBContainer().getContext().bind("inject", this);
    }

    @After
    public void tearDown() throws Exception {
        EJBContainer.createEJBContainer().close();
    }
	

	@Test
	public void testFindById() {
		Person person = new Person("TRANT", "TrungTThien", "sds@ddddd", "ww.wssww", new Date(), "ttyt");
		pm.addPerson(person);
		System.out.println(pm.findPerson(person.getId()));
	}
	@Test
	public void testFindAllAndRemove() {
		Person person1 = new Person("Amin","Daher","ad@ddd","www.sss",new Date(),"aaa");
		Person person2 = new Person("TRAN", "TrungThien", "sds@ddd", "ww.www", new Date(), "ttt");
		pm.addPerson(person1);
		pm.addPerson(person2);
		System.out.println(pm.findAllPersons());
		
		pm.removePerson(person1);
		System.out.println(pm.findAllPersons());
	}
	
}
