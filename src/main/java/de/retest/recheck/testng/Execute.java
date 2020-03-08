package de.retest.recheck.testng;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.retest.recheck.RecheckLifecycle;
import de.retest.recheck.util.ReflectionUtilities;

/**
 * Search fields matching {@link #recheckLifecycle} {@link Predicate} and call the consumer for the field values of the
 * given object.
 */
public class Execute {

	private static final Predicate<Field> recheckLifecycle =
			field -> RecheckLifecycle.class.isAssignableFrom( field.getType() );

	private final Consumer<RecheckLifecycle> consumer;

	private Execute( final Consumer<RecheckLifecycle> consumer ) {
		this.consumer = consumer;
	}

	/**
	 * Create an executor to call the consumer with all instances of the later given object.
	 *
	 * @param consumer
	 *            {@link Consumer} to be executed for all fields matching {@link #recheckLifecycle} {@link Predicate}
	 */
	public static Execute execute( final Consumer<RecheckLifecycle> consumer ) {
		return new Execute( consumer );
	}

	/**
	 * Call {@link #consumer} on all field values matching {@link #recheckLifecycle} {@link Predicate}.
	 *
	 * @param object
	 *            object to retrieve fields from and call {@link #consumer} on
	 */
	public void on( final Object object ) {
		final Stream<Field> field = findRecheckFields( object );
		field.forEach( f -> execute( f, object ) );
	}

	private void execute( final Field field, final Object testInstance ) {
		unlock( field );
		doExecute( field, testInstance );
		lock( field );
	}

	private void doExecute( final Field field, final Object testInstance ) {
		try {
			consumer.accept( (RecheckLifecycle) field.get( testInstance ) );
		} catch ( IllegalArgumentException | IllegalAccessException cause ) {
			throw new RuntimeException( cause );
		}
	}

	private Stream<Field> findRecheckFields( final Object testInstance ) {
		return ReflectionUtilities.getAllFields( testInstance.getClass() ).stream().filter( recheckLifecycle );
	}

	private void unlock( final Field field ) {
		field.setAccessible( true );
	}

	private void lock( final Field field ) {
		field.setAccessible( false );
	}
}
