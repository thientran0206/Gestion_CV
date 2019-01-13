package app.dao;

import java.util.List;

import javax.ejb.ApplicationException;
import javax.ejb.Stateful;
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

@Stateful
@TransactionManagement(TransactionManagementType.CONTAINER)
@ApplicationException(rollback = true)
public class ActivityManager {

	@PersistenceContext(unitName = "myTestDatabaseUnit")
	private EntityManager em;
	
	private Person authPerson = new Person();
	
	public List<Activity> findActivities() {
		TypedQuery<Activity> q = em.createQuery("FROM Activity", Activity.class);
		return q.getResultList();
}
	public Activity findOneActivity(String title) {
		Query query = null;
		try {
			query = em.createQuery("SELECT a FROM Activity a WHERE a.title ='" + title + "'");
		} catch (NoResultException e) {
			return null;
		}
		if (query != null) {
			return (Activity) query.getSingleResult();
		}
		return null;
}
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void saveActivity(Activity activity) {
		em.persist(activity);
		System.err.println("addPerson witdh id=" + activity.getIdActivity());
}
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateActivity(Person person) {
		if (person != null) {
			authPerson = person;
			em.merge(authPerson);
			em.flush();
		} else {
			System.out.println("Erreur lors de la modification");
		}
}
	public void deleteOneActivity(String title) {
		Activity activity = findOneActivity(title);
		if (activity != null) {
			em.remove(activity);
		}
}
	
}
