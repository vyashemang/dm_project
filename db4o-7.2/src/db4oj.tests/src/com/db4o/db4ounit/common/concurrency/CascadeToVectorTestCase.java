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

import java.util.*;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.db4ounit.common.persistent.*;
import com.db4o.ext.*;

import db4ounit.*;
import db4ounit.extensions.*;

public class CascadeToVectorTestCase extends Db4oClientServerTestCase {

	public static void main(String[] args) {
		new CascadeToVectorTestCase().runConcurrency();
	}

	public Vector vec;

	protected void configure(Configuration config) {
		config.objectClass(this).cascadeOnUpdate(true);
		config.objectClass(this).cascadeOnDelete(true);
		config.objectClass(Atom.class).cascadeOnDelete(false);
	}

	protected void store() {
		CascadeToVectorTestCase ctv = new CascadeToVectorTestCase();
		ctv.vec = new Vector();
		ctv.vec.addElement(new Atom("stored1"));
		ctv.vec.addElement(new Atom(new Atom("storedChild1"), "stored2"));
		store(ctv);
	}

	public void conc(ExtObjectContainer oc) {
		CascadeToVectorTestCase ctv = (CascadeToVectorTestCase) retrieveOnlyInstance(oc,CascadeToVectorTestCase.class);
		Enumeration i = ctv.vec.elements();
		while (i.hasMoreElements()) {
			Atom atom = (Atom) i.nextElement();
			atom.name = "updated";
			if (atom.child != null) {
				// This one should NOT cascade
				atom.child.name = "updated";
			}
		}
		oc.store(ctv);
	}

	public void check(ExtObjectContainer oc) {
		CascadeToVectorTestCase ctv = (CascadeToVectorTestCase) retrieveOnlyInstance(oc,
				CascadeToVectorTestCase.class);
		Enumeration i = ctv.vec.elements();
		while (i.hasMoreElements()) {
			Atom atom = (Atom) i.nextElement();
			Assert.areEqual("updated", atom.name);
			if (atom.child != null) {
				Assert.areEqual("storedChild1", atom.child.name);
			}
		}
	}

	public void concDelete(ExtObjectContainer oc, int seq) throws Exception {
		ObjectSet os = oc.query(CascadeToVectorTestCase.class);
		if (os.size() == 0) { // already deleted
			return;
		}
		Assert.areEqual(1, os.size());
		CascadeToVectorTestCase ctv = (CascadeToVectorTestCase) os.next();
		// wait for other threads
		Thread.sleep(500);
		oc.delete(ctv);
	}

	public void checkDelete(ExtObjectContainer oc) {
		// Cascade-On-Delete Test: We only want one atom to remain.
		assertOccurrences(oc, Atom.class, 1);
	}
}
