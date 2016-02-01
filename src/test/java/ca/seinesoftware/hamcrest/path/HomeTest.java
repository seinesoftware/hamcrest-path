package ca.seinesoftware.hamcrest.path;

import static ca.seinesoftware.hamcrest.path.PathMatcher.aDirectory;
import static ca.seinesoftware.hamcrest.path.PathMatcher.exists;
import static ca.seinesoftware.hamcrest.path.PathMatcher.readable;
import static ca.seinesoftware.hamcrest.path.PathMatcher.writable;
import static org.hamcrest.Matchers.is;
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
		assertThat(home, is(readable()));
		assertThat(home, is(writable()));
	}
}
