/*
 * Copyright (c) 2015 - 2016 Keren inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.calculator.listen;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.genius.datastoreutils.AsyncDataTreeChangeListenerBase;
import org.opendaylight.genius.mdsalutil.MDSALUtil;
import org.opendaylight.yang.gen.v1.urn.opendaylight.calculator.parser.api.rev170507.ParserApi;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.odl.calculator.api.api.rev170507.OdlCalculatorApi;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.odl.calculator.operational.api.api.rev170507.OdlCalculatorOpenrationalApi;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.odl.calculator.operational.api.api.rev170507.OdlCalculatorOpenrationalApiBuilder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class ParserListener extends AsyncDataTreeChangeListenerBase<ParserApi, ParserListener>
		implements AutoCloseable {

	private DataBroker dataBroker;

	public ParserListener(DataBroker dataBroker) {
		super(ParserApi.class, ParserListener.class);
		this.dataBroker = dataBroker;
	}

	public void start() {
		registerListener(LogicalDatastoreType.CONFIGURATION, dataBroker);
	}

	private int doLogic(List<String> s) {
		if (s == null || s.size() == 0) {
			return 0;
		}
		int curr = 0;
		String op = "+";
		for (int i = 0; i < s.size(); i += 2) {
			switch (op) {
			case "+":
				curr += Integer.parseInt(s.get(i));
				break;
			case "-":
				curr -= Integer.parseInt(s.get(i));
				break;
			case "*":
				curr *= Integer.parseInt(s.get(i));
				break;
			case "/":
				curr /= Integer.parseInt(s.get(i));
				break;

			default:
				break;
			}
		}
		return curr;
	}

	@Override
	protected InstanceIdentifier<ParserApi> getWildCardPath() {
		return InstanceIdentifier.builder(ParserApi.class).build();
	}

	@Override
	protected void remove(InstanceIdentifier<ParserApi> key, ParserApi dataObjectModification) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void update(InstanceIdentifier<ParserApi> key, ParserApi dataObjectModificationBefore,
			ParserApi dataObjectModificationAfter) {
		calculate(dataObjectModificationAfter);

	}

	@Override
	protected void add(InstanceIdentifier<ParserApi> key, ParserApi dataObjectModification) {
		calculate(dataObjectModification);
	}

	private void calculate(ParserApi dataObjectModification) {
		List<String> collect = dataObjectModification.getQuery().stream().map(x -> x.getObject())
				.collect(Collectors.toList());
		int res = doLogic(collect);

		OdlCalculatorOpenrationalApi data = new OdlCalculatorOpenrationalApiBuilder()//
				.setCalculatorOutput(String.format("%s", res)).build();
		MDSALUtil.syncWrite(dataBroker, LogicalDatastoreType.OPERATIONAL,
				InstanceIdentifier.builder(OdlCalculatorOpenrationalApi.class).build(), data);
	}

	@Override
	protected ParserListener getDataTreeChangeListener() {
		return this;
	}

}
