package de.retest.recheck.testng;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InOrder;
import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.retest.recheck.RecheckLifecycle;

public class RecheckHookTest {

	private static class DummyTest {

		private RecheckLifecycle recheck;

	}

	private IHookCallBack callBack;
	private ITestResult testResult;
	private DummyTest testInstance;
	private String name;

	@BeforeMethod
	private void before() {
		callBack = mock( IHookCallBack.class );
		testResult = mock( ITestResult.class );
		testInstance = new DummyTest();
		testInstance.recheck = mock( RecheckLifecycle.class );
		name = "name";
		when( testResult.getName() ).thenReturn( name );
		when( testResult.getInstance() ).thenReturn( testInstance );
	}

	@Test
	public void callsCallBack() throws Exception {
		final RecheckHook hook = new RecheckHook();

		hook.run( callBack, testResult );

		verify( callBack ).runTestMethod( testResult );
	}

	@Test
	public void administersLifecycle() throws Exception {
		final InOrder inOrder = inOrder( callBack, testInstance.recheck );

		final RecheckHook hook = new RecheckHook();

		hook.run( callBack, testResult );

		inOrder.verify( testInstance.recheck ).startTest( name );
		inOrder.verify( callBack ).runTestMethod( testResult );
		inOrder.verify( testInstance.recheck ).capTest();
		inOrder.verify( testInstance.recheck ).cap();
	}

	@Test
	public void usesParametersInTestName() throws Exception {
		final String someParameter = "parameter-1";
		final String otherParameter = "parameter-2";
		final String combinedName = name + "_" + someParameter + "_" + otherParameter;
		final Object[] parameters = new String[] { someParameter, otherParameter };
		when( testResult.getParameters() ).thenReturn( parameters );

		final RecheckHook hook = new RecheckHook();

		hook.run( callBack, testResult );

		verify( testInstance.recheck ).startTest( combinedName );
	}

	@Test
	public void callCapAfterTestFailed() throws Exception {
		final AssertionError assertionError = new AssertionError(
				"You have to catch AssertionErrors generated by capTest in " + RecheckHook.class.getSimpleName() );
		doThrow( assertionError ).when( testInstance.recheck ).capTest();

		final RecheckHook hook = new RecheckHook();

		assertThatCode( () -> hook.run( callBack, testResult ) ).isEqualTo( assertionError );

		verify( testInstance.recheck ).cap();
	}
}
