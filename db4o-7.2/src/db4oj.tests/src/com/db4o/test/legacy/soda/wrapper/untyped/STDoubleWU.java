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
package com.db4o.test.legacy.soda.wrapper.untyped;


import com.db4o.query.*;
import com.db4o.test.legacy.soda.*;

public class STDoubleWU implements STClass{
	
	public static transient SodaTest st;
	
	Object i_double;
	
	public STDoubleWU(){
	}
	
	private STDoubleWU(double a_double){
		i_double = new Double(a_double);
	}
	
	public Object[] store() {
		return new Object[]{
			new STDoubleWU(0),
			new STDoubleWU(0),
			new STDoubleWU(1.01),
			new STDoubleWU(99.99),
			new STDoubleWU(909.00)
		};
	}
	
	public void testEquals(){
		Query q = st.query();
		q.constrain(new STDoubleWU(0));  
		
		// Primitive default values are ignored, so we need an 
		// additional constraint:
		q.descend("i_double").constrain(new Double(0));
		Object[] r = store();
		st.expect(q, new Object[] {r[0], r[1]});
	}
	
	public void testGreater(){
		Query q = st.query();
		q.constrain(new STDoubleWU(1));
		q.descend("i_double").constraints().greater();
		Object[] r = store();
		st.expect(q, new Object[] {r[2], r[3], r[4]});
	}
	
	public void testSmaller(){
		Query q = st.query();
		q.constrain(new STDoubleWU(1));
		q.descend("i_double").constraints().smaller();
		Object[] r = store();
		st.expect(q, new Object[] {r[0], r[1]});
	}
	
	public void testGreaterOrEqual(){
		Query q = st.query();
		q.constrain(store()[2]);
		q.descend("i_double").constraints().greater().equal();
		Object[] r = store();
		st.expect(q, new Object[] {r[2], r[3], r[4]});
	}
	
	public void testGreaterAndNot(){
		Query q = st.query();
		q.constrain(new STDoubleWU());
		Query val = q.descend("i_double");
		val.constrain(new Double(0)).greater();
		val.constrain(new Double(99.99)).not();
		Object[] r = store();
		st.expect(q, new Object[] {r[2], r[4]});
	}
	
}

