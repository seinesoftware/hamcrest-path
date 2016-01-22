package ca.seinesoftware.hamcrest.path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Description;

class Hidden extends PathMatcher {

	@Override
	public void describeTo(Description description) {
		super.describeTo(description);
		description.appendText("a hidden file or directory");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		try {
			return Files.isHidden(path);
		} catch (IOException e) {
			return false;
		}
	}
}
