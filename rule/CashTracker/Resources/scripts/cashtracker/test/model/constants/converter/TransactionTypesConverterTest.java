package cashtracker.test.model.constants.converter;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.converter.TransactionTypesConverter;


public class TransactionTypesConverterTest extends Assert {

	@Test
	public void test() throws Exception {
		TransactionTypesConverter tpc = new TransactionTypesConverter();

		List <TransactionType> tt = tpc.convertToEntityAttribute(new String[] { "E", "I", "T" });
		System.out.println(tt);

		String[] ts = tpc.convertToDatabaseColumn(tt);

		Arrays.stream(ts).forEach(System.out::println);
	}
}