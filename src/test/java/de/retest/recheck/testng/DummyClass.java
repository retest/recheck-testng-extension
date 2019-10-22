package de.retest.recheck.testng;

import de.retest.recheck.RecheckLifecycle;

class DummyClass extends SuperClass implements DummyInterface {

	public RecheckLifecycle somePublicField;
	public RecheckLifecycle otherPublicField;
	private RecheckLifecycle somePrivateField;
	public Object nonRecheckLifecyleField;
}
