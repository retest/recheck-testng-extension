package de.retest.recheck.testng;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.ITestResult;

import de.retest.recheck.RecheckLifecycle;

public class Execute {

	public void startTest( final ITestResult result ) {
		final String testName = resolveName( result );
		final Object onTestInstance = result.getInstance();
		final Consumer<RecheckLifecycle> startTest = r -> r.startTest( testName );
		execute( startTest, onTestInstance );
	}

	private String resolveName( final ITestResult result ) {
		final String parameters = concatParameters( result );
		return result.getName() + parameters;
	}

	private String concatParameters( final ITestResult result ) {
		final Object[] parameters = result.getParameters();
		if ( null == parameters ) {
			return "";
		}
		return Arrays.stream( parameters ).map( String::valueOf ).collect( Collectors.joining( "_", "_", "" ) );
	}

	public void capTest( final ITestResult result ) {
		final Object onTestInstance = result.getInstance();
		final Consumer<RecheckLifecycle> cap = RecheckLifecycle::capTest;
		execute( cap, onTestInstance );
	}

	public void execute( final Consumer<RecheckLifecycle> consumer, final Object testInstance ) {
		final Stream<Field> field = findRecheckField( testInstance );
		field.forEach( f -> execute( consumer, f, testInstance ) );
	}

	private void execute( final Consumer<RecheckLifecycle> consumer, final Field field, final Object testInstance ) {
		unlock( field );
		doExecute( consumer, field, testInstance );
		lock( field );
	}

	private void doExecute( final Consumer<RecheckLifecycle> consumer, final Field field, final Object testInstance ) {
		try {
			consumer.accept( (RecheckLifecycle) field.get( testInstance ) );
		} catch ( IllegalArgumentException | IllegalAccessException cause ) {
			throw new RuntimeException( cause );
		}
	}

	private Stream<Field> findRecheckField( final Object testInstance ) {
		return FindFields.matching( FindFields.isRecheckLifecycle ).on( testInstance.getClass() );
	}

	private void unlock( final Field field ) {
		field.setAccessible( true );
	}

	private void lock( final Field field ) {
		field.setAccessible( false );
	}
}
