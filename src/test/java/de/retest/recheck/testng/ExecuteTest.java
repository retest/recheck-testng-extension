package de.retest.recheck.testng;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.testng.annotations.Test;

import de.retest.recheck.RecheckLifecycle;

public class ExecuteTest {

	private static class DummyClass {

		private RecheckLifecycle someField;
		private RecheckLifecycle otherField;

	}

	@Test
	public void callConsumerForAllFields() throws Exception {
		@SuppressWarnings( "unchecked" )
		final Consumer<RecheckLifecycle> consumer = mock( Consumer.class );
		final DummyClass object = createDummyObject();

		Execute.execute( consumer ).on( object );

		verify( consumer ).accept( object.someField );
		verify( consumer ).accept( object.otherField );
	}

	private DummyClass createDummyObject() {
		final DummyClass object = new DummyClass();
		object.someField = mock( RecheckLifecycle.class );
		object.otherField = mock( RecheckLifecycle.class );
		return object;
	}
}
