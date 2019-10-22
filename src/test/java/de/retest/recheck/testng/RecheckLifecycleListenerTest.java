package de.retest.recheck.testng;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.retest.recheck.RecheckLifecycle;

public class RecheckLifecycleListenerTest {

	private static final String testName = "testName";
	private ITestResult result;
	private RecheckDummy recheckDummy;
	private RecheckLifecycleListener listener;
	private IInvokedMethod method;

	private static class RecheckDummy {

		private RecheckLifecycle recheck;
		private Runnable someField;
	}

	private static class EmptyTest {

	}

	@BeforeMethod
	void beforeEach() {
		result = mock( ITestResult.class );
		recheckDummy = new RecheckDummy();
		recheckDummy.recheck = mock( RecheckLifecycle.class );
		recheckDummy.someField = mock( Runnable.class );
		method = mock( IInvokedMethod.class );
		configure( recheckDummy );
		configureTestMethod();
		when( result.getName() ).thenReturn( testName );

		listener = new RecheckLifecycleListener();
	}

	private void configure( final Object testInstance ) {
		when( result.getInstance() ).thenReturn( testInstance );
	}

	private void configureTestMethod() {
		when( method.isTestMethod() ).thenReturn( true );
	}

	@Test
	void startsTest() throws Exception {
		listener.beforeInvocation( method, result );

		verify( recheckDummy.recheck ).startTest( testName );
	}

	@Test
	void startsParameterizedTest() throws Exception {
		final String firstParameter = "first";
		final String secondParameter = "second";
		when( result.getParameters() ).thenReturn( new String[] { firstParameter, secondParameter } );
		listener.beforeInvocation( method, result );

		final String expectedTestName = testName + "_" + firstParameter + "_" + secondParameter;
		verify( recheckDummy.recheck ).startTest( expectedTestName );
	}

	@Test
	void capsTest() throws Exception {
		listener.afterInvocation( method, result );

		verify( recheckDummy.recheck ).capTest();
	}

	@Test
	void capsOnSuccess() throws Exception {
		listener.onTestSuccess( result );

		verify( recheckDummy.recheck ).cap();
	}

	@Test
	void callsNothingOnOtherMembers() throws Exception {
		listener.beforeInvocation( method, result );
		listener.afterInvocation( method, result );

		verifyZeroInteractions( recheckDummy.someField );
	}

	@Test
	void doesNotFailOnEmptyTest() throws Exception {
		configure( new EmptyTest() );

		listener.beforeInvocation( method, result );
		listener.afterInvocation( method, result );
	}
}
