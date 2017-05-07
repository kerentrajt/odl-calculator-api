/*
 * Copyright (c) 2015 - 2016 Keren inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.calculator.listen;

import org.opendaylight.genius.datastoreutils.AsyncDataTreeChangeListenerBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.odl.calculator.api.api.rev170507.OdlCalculatorApi;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class CalculatorOutputListener extends AsyncDataTreeChangeListenerBase<OdlCalculatorApi, CalculatorOutputListener>
implements AutoCloseable {

	public CalculatorOutputListener() {
		super(OdlCalculatorApi.class,CalculatorOutputListener.class);
	}

	@Override
	protected InstanceIdentifier<OdlCalculatorApi> getWildCardPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void remove(InstanceIdentifier<OdlCalculatorApi> key, OdlCalculatorApi dataObjectModification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update(InstanceIdentifier<OdlCalculatorApi> key, OdlCalculatorApi dataObjectModificationBefore,
			OdlCalculatorApi dataObjectModificationAfter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void add(InstanceIdentifier<OdlCalculatorApi> key, OdlCalculatorApi dataObjectModification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected CalculatorOutputListener getDataTreeChangeListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
