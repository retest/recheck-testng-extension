package de.retest.recheck.testng;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.testng.annotations.Test;

import de.retest.recheck.RecheckLifecycle;

public class ExecuteTest {

	private static class DummyClass {

		@SuppressWarnings( "unused" )
		public final Object publicObject = new Object();
		protected final RecheckLifecycle protectedLifecycle = mock( RecheckLifecycle.class );
		private final RecheckLifecycle privateLifecycle = mock( RecheckLifecycle.class );

	}

	@Test
	public void callConsumerForAllFields() throws Exception {
		@SuppressWarnings( "unchecked" )
		final Consumer<RecheckLifecycle> consumer = mock( Consumer.class );
		final DummyClass object = new DummyClass();

		Execute.execute( consumer ).on( object );

		verify( consumer ).accept( object.protectedLifecycle );
		verify( consumer ).accept( object.privateLifecycle );
	}

}
