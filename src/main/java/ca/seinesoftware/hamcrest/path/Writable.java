package ca.seinesoftware.hamcrest.path;

import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Description;

class Writable extends PathMatcher {

	@Override
	public void describeTo(Description description) {
		super.describeTo(description);
		description.appendText("a writable file or directory");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		return Files.isWritable(path);
	}
}
