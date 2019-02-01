package app.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import app.entities.Activity;
import app.entities.Person;

@Stateless
public class PersonManager {

	@PersistenceContext(unitName = "myTestDatabaseUnit")
	private EntityManager em;

	@PostConstruct
	public void init() {
		System.out.println("init " + this + " with " + em);
	}

	public Person addPerson(Person p) {
		if (em.find(Person.class, p.getId()) == null) {
			em.persist(p);
			System.err.println("addPerson witdh id=" + p.getId());
		} else
			em.merge(p);
		return p;
	}

	public Person findPerson(long id) {
		System.err.println("findPerson witdh id=" + id);
		return em.find(Person.class, id);
	}

	public List<Person> findAllPersons() {
		TypedQuery<Person> q = em.createQuery("FROM Person", Person.class);
		return q.getResultList();
	}

	public Person updatePerson(Person p) {
		p = em.merge(p);
		return p;
	}

	public void removePerson(Person p) {
		em.remove(findPerson(p.getId()));
	}

	public List<Activity> findActivitiesPerson(Person person) {
		Query query = null;
		if (person.getId() != 0) {

			try {
				query = em.createQuery("SELECT a FROM Activity a WHERE a.person.id=" + person.getId() + "");
			} catch (Exception e) {
			}
			if (query != null) {
				return query.getResultList();
			}
		}
		return null;
	}

	public List<Activity> findActivityByTitle(String title) {
		Query query = null;
		try {
			query = em.createQuery(
					"SELECT p FROM Activity a, Person p WHERE a.title LIKE'%" + title + "%' AND p.id = a.owner.id");
		} catch (NoResultException e) {
			return null;
		}
		if (query != null) {
			return query.getResultList();
		}
		return null;
	}
}