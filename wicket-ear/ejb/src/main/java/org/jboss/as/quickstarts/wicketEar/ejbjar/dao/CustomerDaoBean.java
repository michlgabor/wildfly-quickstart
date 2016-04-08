package org.jboss.as.quickstarts.wicketEar.ejbjar.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Customer;

@Stateless
public class CustomerDaoBean implements CustomerDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Customer> getCustomers() {
		return em.createQuery("SELECT c FROM Customer c").getResultList();
	}

	@Override
	public Customer getCustomer(long id) {
		return em.find(Customer.class, id);
	}

	@Override
	public void addCustomer(String name, String email, String tel) {
		try {
			em.merge(new Customer(null, name, email, tel));
		} catch (PersistenceException e) {

		}
	}

	@Override
	public void remove(Customer objectModel) {
		try {
			Customer managed = em.merge(objectModel);
			em.remove(managed);
			em.flush();
		} catch (Exception e) {

		}
	}

	@Override
	public void updateCustomer(String name, String email, String tel) {
		// FIXME - Auto-generated method stub

	}

	@Override
	public void updateCustomer(long customerId, String name, String email,
			String tel) {
		Customer managed = getCustomer(customerId);
		em.persist(managed);
		managed.setName(name);
		managed.setEmail(email);
		managed.setTelNumber(tel);
		em.merge(managed);
	}
}
