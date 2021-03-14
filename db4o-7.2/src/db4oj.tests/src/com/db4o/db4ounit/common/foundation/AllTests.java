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
package com.db4o.db4ounit.common.foundation;


import com.db4o.foundation.*;

import db4ounit.ReflectionTestSuiteBuilder;
import db4ounit.ConsoleTestRunner;
import db4ounit.TestSuiteBuilder;


public class AllTests implements TestSuiteBuilder {
	
	public Iterator4 iterator() {
		return new ReflectionTestSuiteBuilder(new Class[] {
			Algorithms4TestCase.class,
			ArrayIterator4TestCase.class,
			Arrays4TestCase.class,
			BitMap4TestCase.class,
			BlockingQueueTestCase.class,
			Collection4TestCase.class,
			CompositeIterator4TestCase.class,
			DynamicVariableTestCase.class,
			CoolTestCase.class,
			Hashtable4TestCase.class,
			IntArrayListTestCase.class,
			IntMatcherTestCase.class,
			Iterable4AdaptorTestCase.class,
			IteratorsTestCase.class,
			NoDuplicatesQueueTestCase.class,
			NonblockingQueueTestCase.class,
			Path4TestCase.class,
			SortedCollection4TestCase.class,
			Stack4TestCase.class,
			TreeKeyIteratorTestCase.class,
			TreeNodeIteratorTestCase.class,
			BufferTestCase.class,
		}).iterator();	
	}
	
	public static void main(String[] args) {
		new ConsoleTestRunner(AllTests.class).run();
	}

}
