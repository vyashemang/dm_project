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
package com.db4o.db4ounit.common.btree;

import com.db4o.foundation.*;

import db4ounit.Assert;


public class ExpectingVisitor implements Visitor4{
    
    private static final boolean DEBUG = false;
    
    private final Object[] _expected;
    
    private final boolean _obeyOrder;
    
    private final Collection4 _unexpected = new Collection4();
    
    private boolean _ignoreUnexpected;
    
    private int _cursor;
    
    private static final Object FOUND = new Object() {
    	public String toString() {
    		return "[FOUND]";
    	}
    };
    
    public ExpectingVisitor(Object[] results, boolean obeyOrder, boolean ignoreUnexpected){
        _expected = new Object[results.length];
        System.arraycopy(results, 0, _expected, 0, results.length);
        _obeyOrder = obeyOrder;
        _ignoreUnexpected = ignoreUnexpected;
    }
    
    public ExpectingVisitor(Object[] results){
        this(results, false, false);
    }
    
    public ExpectingVisitor(Object singleObject){
        this(new Object[] { singleObject });
    }

    /**
     * Expect empty
     */
    public ExpectingVisitor(){
        this(new Object[0]);
    }

    public void visit(Object obj) {
        if(_obeyOrder){
            visitOrdered(obj);
        }else{
            visitUnOrdered(obj);
        }
    }
    
    private void visitOrdered(Object obj){
        if(_cursor < _expected.length){
            if(areEqual(_expected[_cursor], obj)){
                ods("Expected OK: " + obj.toString());
                _expected[_cursor] = FOUND;
                _cursor ++;
                return;
            }
        }
        unexpected(obj);
    }

	private void unexpected(Object obj) {
		if(_ignoreUnexpected){
			return;
		}
		_unexpected.add(obj);
        ods("Unexpected: " + obj);
	}
    
    private void visitUnOrdered(Object obj){
        for (int i = 0; i < _expected.length; i++) {
            final Object expectedItem = _expected[i];
			if(areEqual(obj, expectedItem)){
                ods("Expected OK: " + obj);
                _expected[i] = FOUND;
                return;
            }
        }
        unexpected(obj);
    }

	private boolean areEqual(Object obj, final Object expectedItem) {
		return expectedItem == obj
		|| (expectedItem != null
			&& obj != null
			&& expectedItem.equals(obj));
	}
    
    private static void ods(String message) {
        if(DEBUG){
            System.out.println(message);
        }
    }
    
    public void assertExpectations(){
    	if (_unexpected.size() > 0) {
        	Assert.fail("UNEXPECTED: " + _unexpected.toString());
        }
        for (int i = 0; i < _expected.length; i++) {
            Assert.areSame(FOUND, _expected[i]);
        }
    }
    
}
