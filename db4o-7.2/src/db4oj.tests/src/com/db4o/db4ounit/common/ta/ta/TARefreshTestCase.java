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
package com.db4o.db4ounit.common.ta.ta;

import com.db4o.activation.*;
import com.db4o.db4ounit.common.ta.*;
import com.db4o.ext.*;
import com.db4o.query.*;

import db4ounit.*;
import db4ounit.extensions.*;
import db4ounit.extensions.fixtures.*;

public class TARefreshTestCase extends TransparentActivationTestCaseBase implements OptOutSolo {

    public static void main(String[] args) {
        new TARefreshTestCase().runClientServer();
    }
    
    private static final int ITEM_DEPTH = 10;

    protected void store() throws Exception {
        TAItem item = TAItem.newGraph(ITEM_DEPTH);
        store(item);
    }
    
    public void testRefresh() {
        ExtObjectContainer client1 = openNewClient();
        ExtObjectContainer client2 = openNewClient();
        TAItem item1 = queryRoot(client1);
        TAItem item2 = queryRoot(client2);

        TAItem next1 = item1;
        int value = 10;
        while (next1 != null) {
            Assert.areEqual(value, next1.value());
            next1 = next1.next();
            value --;
        }
        
        TAItem next2 = item2;
        value = 10;
        while (next2 != null) {
            Assert.areEqual(value, next2.value());
            next2 = next2.next();
            value --;
        }
        
        //update depth = 1
        item1.value(100);
        item1.next().value(200);
        client1.store(item1, 2);
        client1.commit();
        
        assertItemValue(100, item1);
        assertItemValue(200, item1.next());
        
        assertItemValue(10, item2);
        assertItemValue(9, item2.next());
        
        //refresh 0
        client2.refresh(item2, 0);
        assertItemValue(10, item2);
        assertItemValue(9, item2.next());
        
        //refresh 1
        client2.refresh(item2, 1);
        assertItemValue(100, item2);
        assertItemValue(9, item2.next());
        
        //refresh 2
        client2.refresh(item2, 2);
        assertItemValue(100, item2);
        assertItemValue(200, item2.next());
        
        next1 = item1;
        value = 1000;
        while (next1 != null) {
            next1.value(value);
            next1 = next1.next();
            value++;
        }
        client1.store(item1, 5);
        client1.commit();
        
        client2.refresh(item2, 5);
        next2 = item2;
        for (int i = 1000; i < 1005; i++) {
            assertItemValue(i, next2);
            next2 = next2.next();
        }
        client1.close();
        client2.close();
    }

	private void assertItemValue(final int expectedValue, TAItem item) {
		Assert.areEqual(expectedValue, item.passThroughValue());
		Assert.areEqual(expectedValue, item.value());
	}

    private TAItem queryRoot(ExtObjectContainer client) {
        Query query = client.query();
        query.constrain(TAItem.class);
        query.descend("_isRoot").constrain(new Boolean(true));
        return (TAItem)query.execute().next();
    }
    
    private ExtObjectContainer openNewClient() {
        return ((Db4oClientServerFixture) fixture()).openNewClient();
    }
    
    public static class TAItem extends ActivatableImpl {

        public int _value;

        public TAItem _next;

        public boolean _isRoot;
        
        public static TAItem newGraph(int depth) {
        	TAItem item = newTAItem(depth);
        	item._isRoot = true;
        	return item;
        }

        private static TAItem newTAItem(int depth) {
            if (depth == 0) {
                return null;
            }
            TAItem root = new TAItem();
            root._value = depth;
            root._next = newTAItem(depth - 1);
            return root;
        }
        
        public int passThroughValue() {
        	return _value;
        }

        public int value() {
            activate(ActivationPurpose.READ);
            return _value;
        }

        public void value(int value) {
            _value = value;
        }
        
        public TAItem next() {
            activate(ActivationPurpose.READ);
            return _next;
        }
    }
}
