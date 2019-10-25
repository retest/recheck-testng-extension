package de.retest.recheck.testng;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.retest.recheck.RecheckLifecycle;

public class RecheckHookTest {

	private static class DummyTest {

		private RecheckLifecycle someSuperSuperClassField;

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
		testInstance.someSuperSuperClassField = mock( RecheckLifecycle.class );
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
		final RecheckHook hook = new RecheckHook();

		hook.run( callBack, testResult );

		verify( testInstance.someSuperSuperClassField ).startTest( name );
		verify( testInstance.someSuperSuperClassField ).capTest();
		verify( testInstance.someSuperSuperClassField ).cap();
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

		verify( testInstance.someSuperSuperClassField ).startTest( combinedName );
	}
}
