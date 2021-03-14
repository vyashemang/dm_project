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
package com.db4o.internal.query.result;

import com.db4o.ext.*;
import com.db4o.foundation.*;
import com.db4o.query.QueryComparator;

/**
 * @exclude 
 */
public class StatefulQueryResult implements Iterable4 {
    
    private final QueryResult _delegate;
    private final Iterable4Adaptor _iterable;
    
    public StatefulQueryResult(QueryResult queryResult){
        _delegate = queryResult;
        _iterable = new Iterable4Adaptor(queryResult);
    }

    public Object get(int index) {
    	synchronized(lock()){
    		return _delegate.get(index);
    	}
    }
    
    public long[] getIDs() {
    	synchronized(lock()){
	    	long[] ids = new long[size()];
	        int i = 0;
	        final IntIterator4 iterator = _delegate.iterateIDs();
	        while (iterator.moveNext()) {
	        	ids[i++] = iterator.currentInt();
	        }
	        return ids;
    	}
    }

    public boolean hasNext() {
    	synchronized(lock()){
    		return _iterable.hasNext();
    	}
    }

    public Object next() {
    	synchronized(lock()){
    		return _iterable.next();
    	}
    }

    public void reset() {
    	synchronized(lock()){
    		_iterable.reset();
    	}
    }

    public int size() {
    	synchronized(lock()){
    		return _delegate.size();
    	}
    }

	public void sort(QueryComparator cmp) {
		synchronized(lock()){
			_delegate.sort(cmp);
		}
	}	
		
	public Object lock() {
		return _delegate.lock();
	}
	
	ExtObjectContainer objectContainer() {
		return _delegate.objectContainer();
	}
	
	public int indexOf(Object a_object) {	
		synchronized(lock()){
	        int id = (int)objectContainer().getID(a_object);
	        if(id <= 0){
	            return -1;
	        }
	        return _delegate.indexOf(id);
	    }
	}

	public Iterator4 iterateIDs() {
		synchronized(lock()){
			return _delegate.iterateIDs();
		}
	}

	public Iterator4 iterator() {
		synchronized(lock()){
			return _delegate.iterator();
		}
	}
}
