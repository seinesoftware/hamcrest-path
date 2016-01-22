package ca.seinesoftware.hamcrest.path;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.hamcrest.Description;

class RegularFile extends PathMatcher {

	public RegularFile(LinkOption... linkOptions) {
		super(linkOptions);
	}

	@Override
	public void describeTo(Description description) {
		super.describeTo(description);
		description.appendText("a regular file");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		return Files.isRegularFile(path, linkOptions);
	}
}