package ca.seinesoftware.hamcrest.path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

class SameFile extends TypeSafeMatcher<Path> {

	private final Path expected;

	public SameFile(final Path expected) {
		this.expected = expected;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(expected);
	}

	@Override
	protected boolean matchesSafely(final Path actual) {
		try {
			return Files.isSameFile(actual, expected);
		} catch (IOException e) {
			return false;
		}
	}
}
