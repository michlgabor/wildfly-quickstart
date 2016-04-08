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

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;
import org.jboss.as.quickstarts.wicketEar.ejbjar.dao.AccountDao;
import org.jboss.as.quickstarts.wicketEar.ejbjar.dao.CustomerDao;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Account;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.AccountType;

/**
 * 
 * @author Filippo Diotalevi
 */
@SuppressWarnings("serial")
public class InsertAccount extends WebPage {

	private static final String ACCOUNT_PATTERN = "[0-9]{8}(-[0-9]{8}){1,2}";
	private Form<Account> insertForm;
	private AccountType type = AccountType.CREDIT;
	private String number;

	@Inject
	private AccountDao accountDao;

	@Inject
	private CustomerDao customerDao;

	private Long customerId;
	private Long accountId;
	private final List<AccountType> types = Arrays.asList(AccountType.values());

	public InsertAccount(final PageParameters parameters) {

		add(new FeedbackPanel("feedback"));

		getCustomerParam(parameters);

		getAccountParam(parameters);

		if (accountId != null) {
			Account acc = accountDao.getAccount(accountId);
			type = acc.getType();
			number = acc.getNumber();
			customerId = acc.getCustomer().getId();
		}

		insertForm = new Form<Account>("insertForm") {
			@Override
			protected void onSubmit() {
				if (customerId != null && accountId == null) {
					accountDao.addAccount(type, number,
							customerDao.getCustomer(customerId));

				} else if (accountId != null) {
					accountDao.updateAccount(accountId, type, number,
							customerDao.getCustomer(customerId));
					parameters.add("customer", customerId);
				}
				setResponsePage(ListAccounts.class, parameters);
			}
		};

		insertForm.add(new DropDownChoice<AccountType>("type",
				new PropertyModel<AccountType>(this, "type"), types,
				new ChoiceRenderer<AccountType>()));

		insertForm.add(new RequiredTextField<String>("number",
				new PropertyModel<String>(this, "number")).add(
				new PatternValidator(ACCOUNT_PATTERN)).add(
				new IValidator<String>() {
					@Override
					public void validate(IValidatable<String> validatable) {
						if (isExistingAccount(validatable)) {
							validatable.error(new ValidationError()
									.setMessage("This is an existing account number"));
						}
					}

				}));

		add(insertForm);
	}

	private boolean isExistingAccount(IValidatable<String> validatable) {
		List<Account> accounts = accountDao.getAccounts(customerId);
		for (Account a : accounts) {
			if (a.getNumber().equals(validatable.getValue())) {
				return true;
			}
		}
		return false;
	}

	private void getAccountParam(final PageParameters parameters) {
		if (parameters.getNamedKeys().contains("account")) {
			accountId = parameters.get("account").toLong();
		}
	}

	private void getCustomerParam(final PageParameters parameters) {
		if (parameters.getNamedKeys().contains("customer")) {
			customerId = parameters.get("customer").toLong();
		}
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
