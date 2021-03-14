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
package com.db4o.db4ounit.common.ta.nonta;

import com.db4o.db4ounit.common.ta.*;
import com.db4o.ext.*;
import com.db4o.query.*;

import db4ounit.*;
import db4ounit.extensions.*;
import db4ounit.extensions.fixtures.*;

public class NonTARefreshTestCase extends TransparentActivationTestCaseBase
        implements OptOutSolo {

    public static void main(String[] args) {
        new NonTARefreshTestCase().runClientServer();
    }
    
    private static final int ITEM_DEPTH = 10;

    private Class _class;
    
    protected void store() throws Exception {
        TAItem item = TAItem.newTAItem(ITEM_DEPTH);
        item._isRoot = true;
        _class = item.getClass();
        store(item);
    }
    
    public void testRefresh() {
        ExtObjectContainer client1 = openNewClient();
        ExtObjectContainer client2 = openNewClient();
        TAItem item1 = (TAItem) retrieveInstance(client1);
        TAItem item2 = (TAItem) retrieveInstance(client2);

        assertDescendingRange(10, item1);
        
        assertDescendingRange(10, item2);

        item1.value(100);
        item1.next().value(200);
        client1.store(item1, 2);
        client1.commit();
        
        Assert.areEqual(100, item1.value());
        Assert.areEqual(200, item1.next().value());
        
        Assert.areEqual(10, item2.value());
        Assert.areEqual(9, item2.next().value());
        
        //refresh 0
        client2.refresh(item2, 0);
        Assert.areEqual(10, item2.value());
        Assert.areEqual(9, item2.next().value());
        
        //refresh 1
        client2.refresh(item2, 1);
        Assert.areEqual(100, item2.value());
        Assert.areEqual(9, item2.next().value());
        
        //refresh 2
        client2.refresh(item2, 2);
        Assert.areEqual(100, item2.value());
        Assert.areEqual(200, item2.next().value());
        
        updateAscendingWithRange(item1, 1000);
        client1.store(item1, 5);
        client1.commit();
        
        client2.refresh(item2, 5);
        TAItem next2 = item2;
        for (int i = 1000; i < 1005; i++) {
            Assert.areEqual(i, next2.value());
            next2 = next2.next();
        }
        client1.close();
        client2.close();
    }

	private void updateAscendingWithRange(TAItem item, int startingValue) {
		TAItem current = item;
        while (current != null) {
            current.value(startingValue);
            current = current.next();
            startingValue++;
        }
	}

	private void assertDescendingRange(int startingValue, TAItem item) {
		TAItem current = item;        
        while (current != null) {
            Assert.areEqual(startingValue, current.value());
            current = current.next();
            startingValue --;
        }
	}

    private Object retrieveInstance(ExtObjectContainer client) {
        Query query = client.query();
        query.constrain(_class);
        query.descend("_isRoot").constrain(new Boolean(true));
        return query.execute().next();
    }
    
    private ExtObjectContainer openNewClient() {
        return ((Db4oClientServerFixture) fixture()).openNewClient();
    }
    
    public static class TAItem {

        public int _value;

        public TAItem _next;

        public boolean _isRoot;

        public static TAItem newTAItem(int depth) {
            if (depth == 0) {
                return null;
            }
            TAItem root = new TAItem();
            root._value = depth;
            root._next = newTAItem(depth - 1);
            return root;
        }

        public int value() {
            return _value;
        }

        public void value(int value) {
            _value = value;
        }
        
        public TAItem next() {
            return _next;
        }
    }
}
