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
package com.db4o.instrumentation.core;

import EDU.purdue.cs.bloat.editor.ClassEditor;


/**
 * Composes a sequence of instrumentation steps.
 */
public class CompositeBloatClassEdit implements BloatClassEdit {

	private final BloatClassEdit[] _edits;
	private final boolean _ignoreFailure;
	
	public CompositeBloatClassEdit(BloatClassEdit[] edits) {
		this(edits, false);
	}

	public CompositeBloatClassEdit(BloatClassEdit[] edits, boolean ignoreFailure) {
		_edits = edits;
		_ignoreFailure = ignoreFailure;
	}

	public InstrumentationStatus enhance(ClassEditor ce, ClassLoader origLoader, BloatLoaderContext loaderContext) {
		InstrumentationStatus status = InstrumentationStatus.NOT_INSTRUMENTED;
		for (int editIdx = 0; editIdx < _edits.length; editIdx++) {
			InstrumentationStatus curStatus = _edits[editIdx].enhance(ce, origLoader, loaderContext);
			status = status.aggregate(curStatus, _ignoreFailure);
			if (!_ignoreFailure && !status.canContinue()) {
				break;
			}
		}
		return status;
	}

}
