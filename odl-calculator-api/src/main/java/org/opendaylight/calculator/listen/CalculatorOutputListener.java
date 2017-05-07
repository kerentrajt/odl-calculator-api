/*
 * Copyright (c) 2015 - 2016 Keren inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.calculator.listen;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.genius.datastoreutils.AsyncDataTreeChangeListenerBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.odl.calculator.operational.api.api.rev170507.OdlCalculatorOpenrationalApi;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorOutputListener extends AsyncDataTreeChangeListenerBase<OdlCalculatorOpenrationalApi, CalculatorOutputListener>
implements AutoCloseable {

	private static final Logger LOG = LoggerFactory.getLogger(CalculatorOutputListener.class);
	private DataBroker databroker;

	public CalculatorOutputListener() {
		super(OdlCalculatorOpenrationalApi.class,CalculatorOutputListener.class);
	}

	@Override
	protected void add(InstanceIdentifier<OdlCalculatorOpenrationalApi> key,
			OdlCalculatorOpenrationalApi dataObjectModification) {
		LOG.info("the calculation result is - {}",dataObjectModification.getCalculatorOutput() );
	}

	@Override
	protected CalculatorOutputListener getDataTreeChangeListener() {
		return this;
	}

	@Override
	protected InstanceIdentifier<OdlCalculatorOpenrationalApi> getWildCardPath() {
		return InstanceIdentifier.builder(OdlCalculatorOpenrationalApi.class).build();
	}

	@Override
	protected void remove(InstanceIdentifier<OdlCalculatorOpenrationalApi> key,
			OdlCalculatorOpenrationalApi dataObjectModification) {		
	}
	public void start() {
		LOG.info("{} started.", CalculatorOutputListener.class.getSimpleName());
		registerListener(LogicalDatastoreType.CONFIGURATION, databroker);
	}

	@Override
	protected void update(InstanceIdentifier<OdlCalculatorOpenrationalApi> key,
			OdlCalculatorOpenrationalApi dataObjectModificationBefore,
			OdlCalculatorOpenrationalApi dataObjectModificationAfter){		
	}
	
}
