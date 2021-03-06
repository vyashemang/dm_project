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
package com.db4o.db4ounit.common.concurrency;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.db4ounit.common.persistent.*;
import com.db4o.ext.*;
import com.db4o.query.*;

import db4ounit.*;
import db4ounit.extensions.*;

public class IndexedByIdentityTestCase extends Db4oClientServerTestCase {
	public static void main(String[] args) {
		new IndexedByIdentityTestCase().runConcurrency();
	}

	public Atom atom;

	static final int COUNT = 10;

	protected void configure(Configuration config) {
		config.objectClass(this).objectField("atom").indexed(true);
	    config.objectClass(IndexedByIdentityTestCase.class).cascadeOnUpdate(true);

	}

	protected void store() {
		for (int i = 0; i < COUNT; i++) {
			IndexedByIdentityTestCase ibi = new IndexedByIdentityTestCase();
			ibi.atom = new Atom("ibi" + i);
			store(ibi);
		}
	}

	public void concRead(ExtObjectContainer oc) {
		for (int i = 0; i < COUNT; i++) {
			Query q = oc.query();
			q.constrain(Atom.class);
			q.descend("name").constrain("ibi" + i);
			ObjectSet objectSet = q.execute();
			Assert.areEqual(1, objectSet.size());
			Atom child = (Atom) objectSet.next();
			q = oc.query();
			q.constrain(IndexedByIdentityTestCase.class);
			q.descend("atom").constrain(child).identity();
			objectSet = q.execute();
			Assert.areEqual(1, objectSet.size());
			IndexedByIdentityTestCase ibi = (IndexedByIdentityTestCase) objectSet.next();
			Assert.areSame(child, ibi.atom);
		}

	}

	public void concUpdate(ExtObjectContainer oc, int seq) throws Exception {
		Query q = oc.query();
		q.constrain(IndexedByIdentityTestCase.class);
		ObjectSet os = q.execute();
		Assert.areEqual(COUNT, os.size());
		while (os.hasNext()) {
			IndexedByIdentityTestCase idi = (IndexedByIdentityTestCase) os.next();
			idi.atom.name = "updated" + seq;
			oc.store(idi);
			Thread.sleep(100);
		}
	}

	public void checkUpdate(ExtObjectContainer oc) {
		Query q = oc.query();
		q.constrain(IndexedByIdentityTestCase.class);
		ObjectSet os = q.execute();
		Assert.areEqual(COUNT, os.size());
		String expected = null;
		while (os.hasNext()) {
			IndexedByIdentityTestCase idi = (IndexedByIdentityTestCase) os.next();
			if (expected == null) {
				expected = idi.atom.name;
				Assert.isTrue(expected.startsWith("updated"));
				Assert.isTrue(expected.length() > "updated".length());
			}
			Assert.areEqual(expected, idi.atom.name);
		}
	}

}
