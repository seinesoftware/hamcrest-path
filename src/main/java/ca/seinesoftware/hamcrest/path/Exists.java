package ca.seinesoftware.hamcrest.path;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.hamcrest.Description;

class Exists extends PathMatcher {

	public Exists(LinkOption... linkOptions) {
		super(linkOptions);
	}

	@Override
	public void describeTo(Description description) {
		super.describeTo(description);
		description.appendText("an existing filesystem entry");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		return Files.exists(path, linkOptions);
	}
}
