package ca.seinesoftware.hamcrest.path;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.hamcrest.Description;

class Directory extends PathMatcher {

	public Directory(LinkOption... linkOptions) {
		super(linkOptions);
	}

	@Override
	public void describeTo(Description description) {
		super.describeTo(description);
		description.appendText("a directory");
	}

	@Override
	protected boolean matchesSafely(Path path) {
		return Files.isDirectory(path, linkOptions);
	}
}