package de.retest.recheck.testng;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class RecheckHook implements IHookable {

	@Override
	public void run( final IHookCallBack callBack, final ITestResult testResult ) {
		new Execute().startTest( testResult );
		callBack.runTestMethod( testResult );
		new Execute().capTest( testResult );
	}

}
