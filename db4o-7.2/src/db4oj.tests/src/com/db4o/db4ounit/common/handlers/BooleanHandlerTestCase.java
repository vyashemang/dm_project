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
package com.db4o.db4ounit.common.handlers;

import com.db4o.internal.handlers.*;
import db4ounit.Assert;

public class BooleanHandlerTestCase extends TypeHandlerTestCaseBase {
	
    public static void main(String[] arguments) {
        new BooleanHandlerTestCase().runSolo();
    }
    
    public static class Item {
    	public Boolean _boolWrapper;
    	public boolean _bool;
    	
    	public Item(Boolean boolWrapper, boolean bool){
    		_boolWrapper = boolWrapper;
    		_bool = bool;
    	}
    	
    	public boolean equals(Object obj) {
        	if(obj == this){
        		return true;
        	}
        	if (!(obj instanceof Item)) {
        		return false;
			}
        	Item other = (Item)obj;
        	return (other._bool == this._bool) 
        			&& this._boolWrapper.equals(other._boolWrapper);
    	}
    	
    	public String toString() {
    		return "[" + _bool + "," + _boolWrapper + "]";
    	}
    }
    
    private BooleanHandler booleanHandler() {
        return new BooleanHandler();
    }

	public void testReadWriteTrue(){
		doTestReadWrite(Boolean.TRUE);
	}
	
	public void testReadWriteFalse(){
		doTestReadWrite(Boolean.FALSE);
	}
	
	public void doTestReadWrite(Boolean b){
	    MockWriteContext writeContext = new MockWriteContext(db());
	    booleanHandler().write(writeContext, b);
	    
	    MockReadContext readContext = new MockReadContext(writeContext);
	    Boolean res = (Boolean)booleanHandler().read(readContext);
	    
	    Assert.areEqual(b, res);
	}
	
    public void testStoreObject() throws Exception{
        Item storedItem = new Item(Boolean.FALSE, true);
        doTestStoreObject(storedItem);
    }


}
