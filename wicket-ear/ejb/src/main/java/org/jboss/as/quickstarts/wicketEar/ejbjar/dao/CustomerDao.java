package org.jboss.as.quickstarts.wicketEar.ejbjar.dao;

import java.util.List;

import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Customer;

public interface CustomerDao {

	public List<Customer> getCustomers();

	public Customer getCustomer(long id);

	public void addCustomer(String name, String email, String tel);

	public void updateCustomer(String name, String email, String tel);

	public void remove(Customer c);

	public void updateCustomer(long customerId, String name, String email,
			String tel);
}
