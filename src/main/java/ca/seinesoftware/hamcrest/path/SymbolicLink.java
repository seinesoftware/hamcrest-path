package ca.seinesoftware.hamcrest.path;

import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Description;

class SymbolicLink extends PathMatcher {

	@Override
	public void describeTo(Description description) {
		description.appendText("a symbolic link");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		return Files.isSymbolicLink(path);
	}
}