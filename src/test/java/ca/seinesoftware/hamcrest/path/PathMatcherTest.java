/**
 *
 */
package ca.seinesoftware.hamcrest.path;

import static ca.seinesoftware.hamcrest.path.PathMatcher.*;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static org.hamcrest.Matchers.*;
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

	private static Path testFolder, testFile, noFile, linkFile, linkNoFile, hiddenFile;

	@BeforeClass
	public static void beforeClass() throws IOException {
		testFolder = temporaryFolder.newFolder("folder").toPath();

		testFile = testFolder.resolve("test-file");
		Files.write(testFile, Collections.singleton("Some text"), StandardCharsets.ISO_8859_1);
		testFile.toFile().setWritable(false);

		noFile = testFolder.resolve("no-file");

		try {
			linkFile = Files.createSymbolicLink(testFolder.resolve("link-file"), testFile);
		} catch (IOException e) {
		}

		try {
			linkNoFile = Files.createSymbolicLink(testFolder.resolve("link-no-file"), noFile);
		} catch (IOException e) {
		}

		hiddenFile = testFolder.resolve(".hidden");
		Files.write(hiddenFile, Collections.singleton("Hidden"), StandardCharsets.ISO_8859_1);
		try {
			Files.setAttribute(hiddenFile, "dos:hidden", Boolean.TRUE);
		} catch (UnsupportedOperationException | IOException e) {
		}
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
	public void linkFileExists() {
		assumeThat(linkFile, notNullValue());

		assertThat(linkFile, exists(NOFOLLOW_LINKS));
		assertThat(linkFile, exists());
	}

	@Test
	public void linkNoFileDoesNotExists() {
		assumeThat(linkNoFile, notNullValue());

		assertThat(linkNoFile, exists(NOFOLLOW_LINKS));
		assertThat(linkNoFile, not(exists()));
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
		String description = mismatchDescriptionFor(noFile, aDirectory(NOFOLLOW_LINKS));
		assertThat(description,
				both(containsString("a non-symbolic link to a directory")).and(containsString(" does not exist")));
	}

	@Test
	public void isFileNotADirectoryDescription() {
		String description = mismatchDescriptionFor(hiddenFile, aDirectory());
		assertThat(description, both(containsString("a directory")).and(containsString(" is a readable, writable, "))
				.and(containsString("hidden regular file")));
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
		String description = mismatchDescriptionFor(noFile, aRegularFile(NOFOLLOW_LINKS));
		assertThat(description,
				both(containsString("a non-symbolic link to a regular file")).and(containsString(" does not exist")));
	}

	@Test
	public void isDirectoryNotARegularFileDescription() {
		String description = mismatchDescriptionFor(testFolder, aRegularFile());
		assertThat(description, both(containsString("a regular file"))
				.and(containsString(" is a readable, writable, executable directory")));
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
	public void linkFileIsSymbolicLink() {
		assumeThat(linkFile, notNullValue());
		assertThat(linkFile, is(symbolicLink()));
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
	public void testFileIsNotWritable() {
		assertThat(testFile, is(not(writable())));
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
	// Hidden File/Directory
	// ========================================================================

	@Test
	public void testFolderIsNotHidden() {
		assertThat(testFolder, is(not(hidden())));
	}

	@Test
	public void testFileIsNotHidden() {
		assertThat(testFile, is(not(hidden())));
	}

	@Test
	public void hiddenFileIsHidden() {
		assertThat(hiddenFile, is(hidden()));
	}

	@Test
	public void noFileIsNotHidden() {
		assertThat(noFile, is(not(hidden())));
	}

	@Test
	public void isNotHiddenDescription() {
		String description = mismatchDescriptionFor(noFile, hidden());
		assertThat(description,
				both(containsString("a hidden file or directory")).and(containsString(" does not exist")));
	}

	// ========================================================================
	// Same File
	// ========================================================================

	@Test
	public void isSameAsTestFile() {
		Path relative = Paths.get("..", "folder", "test-file");
		assertThat(testFolder.resolve(relative), is(sameFile(testFile)));
	}

	@Test
	public void isNotSameFileAsNoFile() {
		assertThat(noFile, is(not(sameFile(testFile))));
	}

	@Test
	public void isNotSameFileDescription() {
		String description = mismatchDescriptionFor(noFile, sameFile(testFile));
		assertThat(description, both(containsString("\\test-file> but was <")).and(containsString("\\no-file>")));
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
