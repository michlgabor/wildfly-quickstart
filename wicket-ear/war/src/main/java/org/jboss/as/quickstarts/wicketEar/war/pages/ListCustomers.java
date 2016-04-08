/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.wicketEar.war.pages;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jboss.as.quickstarts.wicketEar.ejbjar.dao.CustomerDao;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Customer;

@SuppressWarnings("serial")
public class ListCustomers extends WebPage {

	// Inject the ContactDao using @Inject
	@Inject
	private CustomerDao customerDao;

	@Resource(name = "welcomeMessage")
	private String welcome;

	// Set up the dynamic behavior for the page, widgets bound by id
	public ListCustomers() {

		// Add the dynamic welcome message, specified in web.xml
		add(new Label("welcomeMessage", welcome));

		// Populate the table of contacts
		add(new ListView<Customer>("contacts", customerDao.getCustomers()) {

			@Override
			protected void populateItem(final ListItem<Customer> item) {
				final Customer customer = item.getModelObject();

				item.add(new Link<Customer>("nameLink", item.getModel()) {

					@Override
					public void onClick() {
						PageParameters parameters = new PageParameters();
						parameters.add("customer", customer.getId());
						setResponsePage(InsertCustomer.class, parameters);
					}
				}.add(new Label("name", customer.getName())));

				item.add(new Label("email", customer.getEmail()));
				item.add(new Label("tel", customer.getTelNumber()));
				item.add(new Link<Customer>("accounts", item.getModel()) {

					@Override
					public void onClick() {
						PageParameters pageParameters = new PageParameters();
						pageParameters.add("customer", customer.getId());
						setResponsePage(ListAccounts.class, pageParameters);
					}
				});
				item.add(new Link<Customer>("delete", item.getModel()) {

					@Override
					public void onClick() {
						customerDao.remove(customer);
						setResponsePage(ListCustomers.class);
					}
				});
			}
		});
	}
}
