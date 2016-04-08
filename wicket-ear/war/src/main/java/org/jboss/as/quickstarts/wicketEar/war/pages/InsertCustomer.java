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

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.PatternValidator;
import org.jboss.as.quickstarts.wicketEar.ejbjar.dao.CustomerDao;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Customer;

@SuppressWarnings("serial")
public class InsertCustomer extends WebPage {

	@Inject
	private CustomerDao customerDao;
	private Customer customer;
	private long customerId;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Form<Customer> insertForm;

	private String name;
	private String email;
	private String tel;

	public InsertCustomer() {

		insertForm = new Form<Customer>("insertForm") {

			@Override
			protected void onSubmit() {
				customerDao.addCustomer(name, email, tel);
				setResponsePage(ListCustomers.class);
			}
		};

		initForm();
	}

	public InsertCustomer(final PageParameters parameters) {

		insertForm = new Form<Customer>("insertForm") {

			@Override
			protected void onSubmit() {
				customerDao.updateCustomer(customerId, name, email, tel);
				setResponsePage(ListCustomers.class);
			}
		};

		initForm();
		fillFieldsWithCurrentCustomer(parameters);
	}

	private void initForm() {
		add(new FeedbackPanel("feedback"));

		insertForm.add(new RequiredTextField<String>("name",
				new PropertyModel<String>(this, "name")));
		insertForm.add(new RequiredTextField<String>("email",
				new PropertyModel<String>(this, "email"))
				.add(new PatternValidator(EMAIL_PATTERN)));
		insertForm.add(new RequiredTextField<String>("tel",
				new PropertyModel<String>(this, "tel")));
		add(insertForm);
	}

	private void fillFieldsWithCurrentCustomer(final PageParameters parameters) {
		if (!parameters.get("customer").isEmpty()) {
			customerId = parameters.get("customer").toLong();
			customer = customerDao.getCustomer(customerId);
			name = customer.getName();
			email = customer.getEmail();
			tel = customer.getTelNumber();
		}
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getTel() {
		return tel;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
