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
package org.jboss.as.quickstarts.wicketEar.ejbjar.dao;

import java.util.List;

import javax.ejb.Local;

import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Account;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.AccountType;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Customer;

@Local
public interface AccountDao {

	public List<Account> getAccounts();

	public List<Account> getAccounts(Long id);

	public Account getAccount(Long id);

	public void addAccount(AccountType type, String number, Customer customer);

	public void remove(Account modelObject);

	public void updateAccount(Long accountId, AccountType type, String number,
			Customer customer);

}
