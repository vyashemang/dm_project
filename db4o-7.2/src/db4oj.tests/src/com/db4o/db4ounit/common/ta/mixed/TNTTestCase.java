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
package com.db4o.db4ounit.common.ta.mixed;

import com.db4o.db4ounit.common.ta.*;

import db4ounit.*;

/**
 * @exclude
 */
public class TNTTestCase extends ItemTestCaseBase {
	
	public static void main(String[] args) {
		new TNTTestCase().runAll();
	}
	
	protected Object createItem() throws Exception {
		return new TNTItem(42);
	}

	protected void assertRetrievedItem(Object obj) throws Exception {
		TNTItem item = (TNTItem) obj;
		Assert.isNull(item.ntItem);
	}
		
	protected void assertItemValue(Object obj) throws Exception {
		TNTItem item = (TNTItem) obj;
		NTItem ntItem = item.value();
		Assert.isNotNull(ntItem);
		Assert.isNotNull(ntItem.tItem);
		Assert.areEqual(0, ntItem.tItem.value);
		Assert.areEqual(42, ntItem.tItem.value());
	}

}
