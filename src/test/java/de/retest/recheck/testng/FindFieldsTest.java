package de.retest.recheck.testng;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FindFieldsTest {

	private static Predicate<Field> stringClass = f -> String.class.isAssignableFrom( f.getType() );
	private Map<String, Class<?>> matchingFields;

	@BeforeMethod
	public void before() {
		matchingFields = new LinkedHashMap<>();
	}

	@Test
	public void findMatchingFieldsOfClass() throws Exception {
		matchingFields.put( "somePublicField", DummyClass.class );
		matchingFields.put( "otherPublicField", DummyClass.class );
		matchingFields.put( "somePrivateField", DummyClass.class );
		matchingFields.put( "someSuperClassField", SuperClass.class );
		matchingFields.put( "someSuperSuperClassField", SuperSuperClass.class );
		matchingFields.put( "someInterfaceField", DummyInterface.class );
		matchingFields.put( "someSuperClassInterfaceField", SuperClassInterface.class );
		final List<Field> on = FindFields.matching( stringClass ).on( DummyClass.class ).collect( Collectors.toList() );
		assertThat( on ).containsOnlyElementsOf( fields() );
	}

	@Test
	public void doesNotFindNonMatchingFields() throws Exception {
		assertThat( FindFields.matching( stringClass ).on( DummyClass.class ) )
				.doesNotContain( getField( DummyClass.class, "nonMatchingField" ) );
	}

	@Test
	public void doesNotFailOnTestWithoutFields() throws Exception {
		FindFields.matching( stringClass ).on( EmptyClass.class );
	}

	@Test
	public void doesNotFindFieldsTwice() throws Exception {
		matchingFields.put( "someSuperSuperClassField", SuperSuperClass.class );
		final List<Field> fields = FindFields.matching( stringClass ).on( SuperSuperClass.class ).collect( toList() );
		assertThat( fields ).containsOnlyElementsOf( fields() );
		assertThat( fields ).hasSize( 1 );
	}

	private List<Field> fields() throws NoSuchFieldException, SecurityException {
		return matchingFields.entrySet().stream().map( this::toField ).collect( toList() );
	}

	private Field toField( final Entry<String, Class<?>> entry ) {
		return getField( entry.getValue(), entry.getKey() );
	}

	private Field getField( final Class<?> clazz, final String field ) {
		try {
			return clazz.getDeclaredField( field );
		} catch ( NoSuchFieldException | SecurityException e ) {
			throw new RuntimeException( e );
		}
	}

}
