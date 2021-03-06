package ca.seinesoftware.hamcrest.path;

import static ca.seinesoftware.hamcrest.path.PathMatcher.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class HomeTest {
	@Test
	public void testHomeDirectory() {
		Path home = Paths.get(System.getProperty("user.home"));
		assertThat(home, exists());
		assertThat(home, is(aDirectory()));
		assertThat(home, is(both(readable()).and(writable())));
	}
}
