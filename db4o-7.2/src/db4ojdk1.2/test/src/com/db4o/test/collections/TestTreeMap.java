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
package com.db4o.test.collections;

import java.util.*;

import com.db4o.*;
import com.db4o.test.*;

public class TestTreeMap {
	
	TreeMap tm;
	
	public void configure(){
		Db4o.configure().objectClass(this).cascadeOnUpdate(true);
		Db4o.configure().objectClass(this).cascadeOnDelete(true);
	}
	
	public void store(){
		Test.deleteAllInstances(this);
		Test.deleteAllInstances(new Atom());
		Test.deleteAllInstances(new com.db4o.config.Entry());
		TestTreeMap ttm = new TestTreeMap();
		ttm.tm = new TreeMap();
		ttm.tm.put("t1", new Atom("t1"));
		ttm.tm.put("t2", new Atom("t2"));
		Test.store(ttm);
	}
	
	public void test(){
		com.db4o.config.Entry checkEntries = new com.db4o.config.Entry();
		TestTreeMap ttm = (TestTreeMap)Test.getOne(this);
		Test.ensure(ttm.tm.size() == 2);
		Test.ensure(ttm.tm.get("t1").equals(new Atom("t1")));
		Test.ensure(ttm.tm.get("t2").equals(new Atom("t2")));
		ttm.tm.put("t2", new Atom("t3"));
		Test.store(ttm);
		if(Test.COMPARE_INTERNAL_OK){
			Test.ensureOccurrences(checkEntries, 2);
			Test.commit();
			Test.ensureOccurrences(checkEntries, 2);
			Test.deleteAllInstances(this);
			Test.ensureOccurrences(checkEntries, 0);
			Test.rollBack();
			Test.ensureOccurrences(checkEntries, 2);
			Test.deleteAllInstances(this);
			Test.ensureOccurrences(checkEntries, 0);
			Test.commit();
			Test.ensureOccurrences(checkEntries, 0);
		}
	}
}
