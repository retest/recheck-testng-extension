package de.retest.recheck.testng;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.stream.Stream;

import de.retest.recheck.RecheckLifecycle;
import de.retest.recheck.util.ReflectionUtilities;

/**
 * Searches {@link RecheckLifecycle} fields and executes the consumer for the field values of the given object.
 */
public class Execute {

	private final Consumer<RecheckLifecycle> consumer;

	private Execute( final Consumer<RecheckLifecycle> consumer ) {
		this.consumer = consumer;
	}

	/**
	 * Create an executor to call the consumer with all instances of the later given object.
	 *
	 * @param consumer
	 *            {@link Consumer} to be executed for all {@link RecheckLifecycle} fields.
	 * @return {@code Execute} instance with the given consumer.
	 */
	public static Execute execute( final Consumer<RecheckLifecycle> consumer ) {
		return new Execute( consumer );
	}

	/**
	 * Execute {@link #consumer} on all {@link RecheckLifecycle} fields.
	 *
	 * @param object
	 *            object to retrieve fields from and call {@link #consumer} on.
	 */
	public void on( final Object object ) {
		findRecheckLifecycleFields( object ).forEach( field -> execute( field, object ) );
	}

	private void execute( final Field field, final Object testInstance ) {
		unlock( field );
		doExecute( field, testInstance );
		lock( field );
	}

	private void doExecute( final Field field, final Object testInstance ) {
		try {
			consumer.accept( (RecheckLifecycle) field.get( testInstance ) );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new IllegalStateException( e );
		}
	}

	private Stream<Field> findRecheckLifecycleFields( final Object testInstance ) {
		return ReflectionUtilities.getAllFields( testInstance.getClass() ).stream().filter( this::isRecheckLifecycle );
	}

	private boolean isRecheckLifecycle( final Field field ) {
		return RecheckLifecycle.class.isAssignableFrom( field.getType() );
	}

	private void unlock( final Field field ) {
		field.setAccessible( true );
	}

	private void lock( final Field field ) {
		field.setAccessible( false );
	}
}
