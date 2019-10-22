package de.retest.recheck.testng;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FindFieldsTest {

	private Map<String, Class<?>> recheckFields;

	@BeforeMethod
	public void before() {
		recheckFields = new LinkedHashMap<>();
	}

	@Test
	public void findRecheckFieldsOfClass() throws Exception {
		recheckFields.put( "somePublicField", DummyClass.class );
		recheckFields.put( "otherPublicField", DummyClass.class );
		recheckFields.put( "somePrivateField", DummyClass.class );
		recheckFields.put( "someSuperClassField", SuperClass.class );
		recheckFields.put( "someSuperSuperClassField", SuperSuperClass.class );
		recheckFields.put( "someInterfaceField", DummyInterface.class );
		recheckFields.put( "someSuperClassInterfaceField", SuperClassInterface.class );
		final List<Field> on = FindFields.matching( FindFields.isRecheckLifecycle ).on( DummyClass.class )
				.collect( Collectors.toList() );
		assertThat( on ).containsOnlyElementsOf( fields() );
	}

	@Test
	public void doesNotFindNonRecheckLifecycleFields() throws Exception {
		assertThat( FindFields.matching( FindFields.isRecheckLifecycle ).on( DummyClass.class ) )
				.doesNotContain( getField( DummyClass.class, "nonRecheckLifecyleField" ) );
	}

	@Test
	public void doesNotFailOnTestWithoutFields() throws Exception {
		FindFields.matching( FindFields.isRecheckLifecycle ).on( EmptyClass.class );
	}

	@Test
	public void doesNotFindFieldsTwice() throws Exception {
		recheckFields.put( "someSuperSuperClassField", SuperSuperClass.class );
		final List<Field> fields =
				FindFields.matching( FindFields.isRecheckLifecycle ).on( SuperSuperClass.class ).collect( toList() );
		assertThat( fields ).containsOnlyElementsOf( fields() );
		assertThat( fields ).hasSize( 1 );
	}

	private List<Field> fields() throws NoSuchFieldException, SecurityException {
		return recheckFields.entrySet().stream().map( this::toField ).collect( toList() );
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
