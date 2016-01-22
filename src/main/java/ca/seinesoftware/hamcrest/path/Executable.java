package ca.seinesoftware.hamcrest.path;

import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Description;

class Executable extends PathMatcher {

	@Override
	public void describeTo(Description description) {
		super.describeTo(description);
		description.appendText("an executable file or directory");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		return Files.isExecutable(path);
	}
}