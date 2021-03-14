/* Copyright (C) 2004 - 2008  db4objects Inc.  http://www.db4o.com

This file is part of the db4o open source object database.

db4o is free software; you can redistribute it and/or modify it under
the terms of version 2 of the GNU General Public License as published
by the Free Software Foundation and as clarified by db4objects' GPL 
interpretation policy, available at
http://www.db4o.com/about/company/legalpolicies/gplinterpretation/
Alternatively you can write to db4objects, Inc., 1900 S Norfolk Street,
Suite 350, San Mateo, CA 94403, USA.

db4o is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. */
package com.db4o.db4ounit.common.regression;

import com.db4o.db4ounit.common.assorted.SimpleObject;
import com.db4o.ext.ExtObjectContainer;

import db4ounit.Assert;
import db4ounit.extensions.Db4oClientServerTestCase;

public class SetRollbackTestCase extends Db4oClientServerTestCase {

	public static void main(String[] args) {
		new SetRollbackTestCase().runClientServer();
	}
	
	/*
	 * regression test: http://developer.db4o.com/forums/thread/29298.aspx
	 */
	public void testSetRollback() {
		ExtObjectContainer oc1 = openNewClient();
		ExtObjectContainer oc2 = openNewClient();
		try {
			for (int i = 0; i < 1000; i++) {
				SimpleObject obj1 = new SimpleObject("oc " + i, i);
				SimpleObject obj2 = new SimpleObject("oc2 " + i, i);
				oc1.store(obj1);
				oc2.store(obj2);
				oc2.rollback();
				obj2 = new SimpleObject("oc2.2 " + i, i);
				oc2.store(obj2);
			}
			oc1.commit();
			oc2.rollback();
			Assert.areEqual(1000, oc1.query(SimpleObject.class).size());
			Assert.areEqual(1000, oc2.query(SimpleObject.class).size());
		} finally {
			oc1.close();
			oc2.close();
		}
	}

}
