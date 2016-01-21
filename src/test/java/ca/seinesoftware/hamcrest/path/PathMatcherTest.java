/**
 *
 */
package ca.seinesoftware.hamcrest.path;

import static ca.seinesoftware.hamcrest.path.PathMatcher.aDirectory;
import static ca.seinesoftware.hamcrest.path.PathMatcher.aRegularFile;
import static ca.seinesoftware.hamcrest.path.PathMatcher.executable;
import static ca.seinesoftware.hamcrest.path.PathMatcher.exists;
import static ca.seinesoftware.hamcrest.path.PathMatcher.readable;
import static ca.seinesoftware.hamcrest.path.PathMatcher.sameFile;
import static ca.seinesoftware.hamcrest.path.PathMatcher.symbolicLink;
import static ca.seinesoftware.hamcrest.path.PathMatcher.writable;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PathMatcherTest {

	// ========================================================================
	// Test fixtures
	// ========================================================================

	@ClassRule
	public static TemporaryFolder temporaryFolder = new TemporaryFolder();

	private static Path testFolder, testFile, noFile;

	@BeforeClass
	public static void beforeClass() throws IOException {
		testFolder = temporaryFolder.newFolder("folder").toPath();
		testFile = testFolder.resolve("test-file");
		noFile = testFolder.resolve("no-file");

		Files.write(testFile, Collections.singleton("Some text"), StandardCharsets.ISO_8859_1);
	}

	// ========================================================================
	// Exists
	// ========================================================================

	@Test
	public void testFolderExists() {
		assertThat(testFolder, exists());
	}

	@Test
	public void testFileExists() {
		assertThat(testFile, exists());
	}

	@Test
	public void noFileDoesNotExist() {
		assertThat(noFile, not(exists()));
	}

	@Test
	public void doesNotExistDescription() {
		String description = mismatchDescriptionFor(noFile, exists());
		assertThat(description,
				both(containsString("an existing filesystem entry")).and(containsString(" does not exist")));
	}

	// ========================================================================
	// Directory
	// ========================================================================

	@Test
	public void testFolderIsADirectory() {
		assertThat(testFolder, is(aDirectory()));
	}

	@Test
	public void testFileIsNotADirectory() {
		assertThat(testFile, is(not(aDirectory())));
	}

	@Test
	public void noFileIsNotADirectory() {
		assertThat(noFile, is(not(aDirectory())));
	}

	@Test
	public void isNotADirectoryDescription() {
		String description = mismatchDescriptionFor(noFile, aDirectory());
		assertThat(description, both(containsString("a directory")).and(containsString(" does not exist")));
	}

	@Test
	public void isFileNotADirectoryDescription() {
		String description = mismatchDescriptionFor(testFile, aDirectory());
		assertThat(description, both(containsString("a directory")).and(containsString(" is a readable, writable, "))
				.and(containsString("regular file")));
	}

	// ========================================================================
	// Regular File
	// ========================================================================

	@Test
	public void testFolderIsNotARegularFile() {
		assertThat(testFolder, is(not(aRegularFile())));
	}

	@Test
	public void testFileIsARegularFile() {
		assertThat(testFile, is(aRegularFile()));
	}

	@Test
	public void noFileIsNotARegularFile() {
		assertThat(noFile, is(not(aRegularFile())));
	}

	@Test
	public void isNotARegularFileDescription() {
		String description = mismatchDescriptionFor(noFile, aRegularFile());
		assertThat(description, both(containsString("a regular file")).and(containsString(" does not exist")));
	}

	@Test
	public void isDirectoryNotARegularFileDescription() {
		String description = mismatchDescriptionFor(testFolder, aRegularFile());
		assertThat(description, both(containsString("a regular file"))
				.and(containsString(" is a readable, writable, executable directory")));
	}

	// ========================================================================
	// Readable File/Directory
	// ========================================================================

	@Test
	public void testFolderIsReadable() {
		assertThat(testFolder, is(readable()));
	}

	@Test
	public void testFileIsReadable() {
		assertThat(testFile, is(readable()));
	}

	@Test
	public void noFileIsNotReadable() {
		assertThat(noFile, is(not(readable())));
	}

	@Test
	public void isNotReadableDescription() {
		String description = mismatchDescriptionFor(noFile, readable());
		assertThat(description,
				both(containsString("a readable file or directory")).and(containsString(" does not exist")));
	}

	// ========================================================================
	// Writable File/Directory
	// ========================================================================

	@Test
	public void testFolderIsWritable() {
		assertThat(testFolder, is(writable()));
	}

	@Test
	public void testFileIsWritable() {
		assertThat(testFile, is(writable()));
	}

	@Test
	public void noFileIsNotWritable() {
		assertThat(noFile, is(not(writable())));
	}

	@Test
	public void isNotWritableDescription() {
		String description = mismatchDescriptionFor(noFile, writable());
		assertThat(description,
				both(containsString("a writable file or directory")).and(containsString(" does not exist")));
	}

	// ========================================================================
	// Executable File/Directory
	// ========================================================================

	@Test
	public void testFolderIsExecutable() {
		assertThat(testFolder, is(executable()));
	}

	@Test
	public void testFileIsNotExecutable() {
		// Regular files in Windows are usually executable by default.
		assumeThat(System.getProperty("os.name"), not(startsWith("Windows")));

		assertThat(testFile, is(not(executable())));
	}

	@Test
	public void noFileIsNotExecutable() {
		assertThat(noFile, is(not(executable())));
	}

	@Test
	public void isNotExecutableDescription() {
		String description = mismatchDescriptionFor(noFile, executable());
		assertThat(description,
				both(containsString("an executable file or directory")).and(containsString(" does not exist")));
	}

	// ========================================================================
	// Symbolic Link
	// ========================================================================

	@Test
	public void tempFolderIsNotSymbolicLink() {
		assertThat(testFolder, is(not(symbolicLink())));
	}

	@Test
	public void testFileIsNotSymbolicLink() {
		assertThat(testFile, is(not(symbolicLink())));
	}

	@Test
	public void noFileIsNotSymbolicLink() {
		assertThat(noFile, is(not(symbolicLink())));
	}

	@Test
	public void isNotSymbolicLinkDescription() {
		String description = mismatchDescriptionFor(noFile, symbolicLink());
		assertThat(description, both(containsString("a symbolic link")).and(containsString(" does not exist")));
	}

	// ========================================================================
	// Same File
	// ========================================================================

	@Test
	public void isSameAsTestFile() {
		Path relative = Paths.get("..", "folder", "test-file");
		assertThat(testFolder.resolve(relative), is(sameFile(testFile)));
	}

	// ========================================================================
	// Build a description for a mismatch
	// ========================================================================

	private <T> String mismatchDescriptionFor(T actual, Matcher<T> matcher) {
		assertThat(matcher.matches(actual), is(false));

		StringDescription description = new StringDescription();
		description.appendText("Expected ");
		matcher.describeTo(description);
		description.appendText(" but ");
		matcher.describeMismatch(actual, description);
		return description.toString();
	}
}
