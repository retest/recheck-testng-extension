package de.retest.recheck.testng;

class DummyClass extends SuperClass implements DummyInterface {

	public String somePublicField;
	public String otherPublicField;
	@SuppressWarnings( "unused" )
	private String somePrivateField;
	public Object nonMatchingField;
}
