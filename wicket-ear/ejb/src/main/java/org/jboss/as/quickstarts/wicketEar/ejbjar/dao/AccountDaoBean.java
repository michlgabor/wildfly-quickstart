package org.jboss.as.quickstarts.wicketEar.ejbjar.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Account;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.AccountType;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Customer;

@Stateless
public class AccountDaoBean implements AccountDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Account> getAccounts() {
		return em.createQuery("SELECT a FROM Account a").getResultList();
	}

	@Override
	public Account getAccount(Long id) {
		return em.find(Account.class, id);
	}

	@Override
	public void addAccount(AccountType type, String number, Customer customer) {
		try {
			em.merge(new Account(null, type, number, customer));
		} catch (PersistenceException e) {

		}
	}

	@Override
	public void remove(Account modelObject) {
		Account managed = em.merge(modelObject);
		em.remove(managed);
		em.flush();
	}

	@Override
	public void updateAccount(Long accountId, AccountType type, String number,
			Customer customer) {
		Account managed = getAccount(accountId);
		em.persist(managed);
		managed.setType(type);
		managed.setNumber(number);
		managed.setCustomer(customer);
		em.merge(managed);

	}

	@Override
	public List<Account> getAccounts(Long id) {
		List<Account> list = em.createQuery(
				"SELECT a FROM Account a WHERE customer.id=" + id)
				.getResultList();
		return list;

	}
}
