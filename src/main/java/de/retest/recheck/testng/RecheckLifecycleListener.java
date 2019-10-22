package de.retest.recheck.testng;

import java.util.function.Consumer;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import de.retest.recheck.RecheckLifecycle;

public class RecheckLifecycleListener implements IInvokedMethodListener, ITestListener, ISuiteListener {

	@Override
	public void beforeInvocation( final IInvokedMethod method, final ITestResult testResult ) {
		if ( method.getTestMethod().isAfterClassConfiguration() ) {
			cap( testResult );
		}
	}

	@Override
	public void onFinish( final ISuite suite ) {
		ISuiteListener.super.onFinish( suite );
	}

	@Override
	public void onTestSuccess( final ITestResult result ) {
		cap( result );
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage( final ITestResult result ) {
		cap( result );
	}

	@Override
	public void onTestFailedWithTimeout( final ITestResult result ) {
		cap( result );
	}

	@Override
	public void onTestFailure( final ITestResult result ) {
		cap( result );
	}

	@Override
	public void onTestSkipped( final ITestResult result ) {
		cap( result );
	}

	private void cap( final ITestResult result ) {
		final Object onTestInstance = result.getInstance();
		final Consumer<RecheckLifecycle> cap = RecheckLifecycle::cap;
		new Execute().execute( cap, onTestInstance );
	}

	@Override
	public void onFinish( final ITestContext context ) {
		context.getPassedTests().getAllResults().forEach( this::cap );
	}
}
