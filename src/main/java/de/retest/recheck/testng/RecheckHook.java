package de.retest.recheck.testng;

import static de.retest.recheck.testng.Execute.execute;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

import de.retest.recheck.RecheckLifecycle;

/**
 * Administer the lifecycle of {@link RecheckLifecycle} objects of a test.
 */
public class RecheckHook implements IHookable {

	@Override
	public void run( final IHookCallBack callBack, final ITestResult testResult ) {
		startTest( testResult );
		callBack.runTestMethod( testResult );
		try {
			capTest( testResult );
		} finally {
			cap( testResult );
		}
	}

	private void startTest( final ITestResult result ) {
		final String testName = resolveName( result );
		final Object testInstance = result.getInstance();
		final Consumer<RecheckLifecycle> startTest = lifecycle -> lifecycle.startTest( testName );
		execute( startTest ).on( testInstance );
	}

	private String resolveName( final ITestResult result ) {
		final String parameters = concatParameters( result );
		return result.getName() + parameters;
	}

	private String concatParameters( final ITestResult result ) {
		final Object[] parameters = result.getParameters();
		if ( parameters == null || parameters.length == 0 ) {
			return "";
		}
		return Arrays.stream( parameters ) //
				.map( Objects::toString ) //
				.collect( Collectors.joining( "_", "_", "" ) );
	}

	private void capTest( final ITestResult result ) {
		final Object testInstance = result.getInstance();
		final Consumer<RecheckLifecycle> cap = RecheckLifecycle::capTest;
		execute( cap ).on( testInstance );
	}

	private void cap( final ITestResult result ) {
		final Object testInstance = result.getInstance();
		final Consumer<RecheckLifecycle> cap = RecheckLifecycle::cap;
		execute( cap ).on( testInstance );
	}

}
