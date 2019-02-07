package app.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;

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
		TypedQuery<Activity> query;
		if (person.getId() != 0) {

			query = em.createQuery("SELECT a FROM Activity a WHERE a.person.id= :iden", Activity.class);
			query.setParameter("iden", person.getId());

			return query.getResultList();
		}
		return null;
	}

	public List<Activity> findActivityByTitle(String title) {
		TypedQuery<Activity> query;
		query = em.createQuery("SELECT p FROM Activity a, Person p WHERE a.title = :title AND p.id = a.owner.id",
				Activity.class);
		query.setParameter("title", title);
		return query.getResultList();
	}

	public List<Person> search(String name) {
		TypedQuery<Person> q;
		q = em.createQuery("SELECT p1 FROM Person p1 WHERE p1.name = :nom ", Person.class);
		q.setParameter("nom", name);
		return q.getResultList();

	}
}