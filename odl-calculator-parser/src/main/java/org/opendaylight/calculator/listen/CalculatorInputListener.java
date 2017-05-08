/*
 * Copyright (c) 2015 - 2016 Keren inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.calculator.listen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.genius.datastoreutils.AsyncDataTreeChangeListenerBase;
import org.opendaylight.genius.mdsalutil.MDSALUtil;
import org.opendaylight.yang.gen.v1.urn.opendaylight.calculator.parser.api.rev170507.ParserApi;
import org.opendaylight.yang.gen.v1.urn.opendaylight.calculator.parser.api.rev170507.ParserApiBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.calculator.parser.api.rev170507.parser.api.Query;
import org.opendaylight.yang.gen.v1.urn.opendaylight.calculator.parser.api.rev170507.parser.api.QueryBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.odl.calculator.api.api.rev170507.OdlCalculatorApi;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorInputListener extends AsyncDataTreeChangeListenerBase<OdlCalculatorApi, CalculatorInputListener>
		implements AutoCloseable {

	private static final Logger LOG = LoggerFactory.getLogger(CalculatorInputListener.class);
	private DataBroker databroker;

	public CalculatorInputListener(DataBroker databroker) {
		super(OdlCalculatorApi.class, CalculatorInputListener.class);
		this.databroker = databroker;
	}

	@Override
	protected void add(InstanceIdentifier<OdlCalculatorApi> key, OdlCalculatorApi dataObjectModification) {
		String query = dataObjectModification.getInput();
		insertQuery(query);
	}

	@Override
	protected CalculatorInputListener getDataTreeChangeListener() {
		return this;
	}

	@Override
	protected InstanceIdentifier<OdlCalculatorApi> getWildCardPath() {
		return InstanceIdentifier.builder(OdlCalculatorApi.class).build();
	}

	@Override
	protected void remove(InstanceIdentifier<OdlCalculatorApi> key, OdlCalculatorApi dataObjectModification) {
	}

	public void start() {
		LOG.info("{} started.", CalculatorInputListener.class.getSimpleName());
		registerListener(LogicalDatastoreType.CONFIGURATION, databroker);
	}

	@Override
	protected void update(InstanceIdentifier<OdlCalculatorApi> key, OdlCalculatorApi dataObjectModificationBefore,
			OdlCalculatorApi dataObjectModificationAfter) {
		String query = dataObjectModificationAfter.getInput();
		insertQuery(query);
	}

	public static ArrayList<String> parseQuery(String query) {
		ArrayList<String> res = new ArrayList<String>();
		/*if (!query.matches("([1-9][0-9]*)(([+-*\]([1-9][0-9]*))*)")) {
			res.add("0");
			return res;
		}*/
		String currInteger = "";
		String cleanQuery = query.replaceAll("\\s+", "");
		char currChar;
		for (int i = 0; i < cleanQuery.length(); i++) {
			currChar = cleanQuery.charAt(i);
			if (isOperator(currChar)) {
				res.add(currInteger);
				currInteger = "";
				res.add(String.valueOf(currChar));
				continue;
			}
			currInteger += String.valueOf(currChar);
		}
		res.add(currInteger);
		return res;
	}

	public static boolean isOperator(char Suspect) {
		switch (Suspect) {
		case '+':
			return true;
		case '-':
			return true;
		case '*':
			return true;
		case '/':
			return true;
		default:
			return false;
		}
	}

	public void insertQuery(String query) {
		LOG.info("Parsing an input");
		ArrayList<String> parsedQuery = parseQuery(query);
		List<Query> list = parsedQuery.stream().map(x -> new QueryBuilder().setObject(x).build())
				.collect(Collectors.toList());

		ParserApi dataObject = new ParserApiBuilder().setQuery(list).build();
		MDSALUtil.syncWrite(databroker, LogicalDatastoreType.CONFIGURATION,
				InstanceIdentifier.builder(ParserApi.class).build(), dataObject);
	}

}
