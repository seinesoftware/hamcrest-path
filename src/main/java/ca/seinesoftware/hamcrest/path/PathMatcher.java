package ca.seinesoftware.hamcrest.path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * PathMatcher
 *
 * @author Arthur Neufeld &lt;aneufeld@seinesoftware.ca&gt;
 */
public abstract class PathMatcher extends TypeSafeMatcher<Path> {

	// ========================================================================
	// Constructor
	// ========================================================================

	protected final LinkOption[] linkOptions;

	protected PathMatcher(LinkOption... options) {
		linkOptions = options;
	}

	// ========================================================================
	// describeTo
	// ========================================================================

	@Override
	public void describeTo(Description description) {
		for (LinkOption option : linkOptions) {
			switch (option) {
			case NOFOLLOW_LINKS:
				description.appendText("a non-symbolic link to ");
				break;
			}
		}
	}

	// ========================================================================
	// describeMismatchSafely
	// ========================================================================

	@Override
	protected void describeMismatchSafely(Path path, Description description) {
		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			description.appendValue(path).appendText(" is a ");
			if (Files.isSymbolicLink(path)) {
				description.appendText("symbolic link to a ");
			}

			if (!Files.isReadable(path)) {
				description.appendText("un");
			}
			description.appendText("readable, ");

			if (!Files.isWritable(path)) {
				description.appendText("un");
			}
			description.appendText("writable, ");

			if (!Files.isExecutable(path)) {
				description.appendText("un");
			}
			description.appendText("executable");

			try {
				if (Files.isHidden(path)) {
					description.appendText(", hidden");
				}
			} catch (IOException e) {
			}

			if (Files.isDirectory(path)) {
				description.appendText(" directory");
			} else if (Files.isRegularFile(path)) {
				description.appendText(" regular file");
			} else {
				description.appendText(" non-existent entry");
			}
		} else if (Files.notExists(path, linkOptions)) {
			description.appendValue(path).appendText(" does not exist");
		} else {
			description.appendText("file system status for ").appendValue(path).appendText(" cannot be determined");
		}
	}

	// ========================================================================
	// Factories
	// ========================================================================

	/**
	 * Create a matcher that matches if the examined {@link Path} can be
	 * determined to exist.
	 * <p>
	 * By default, symbolic links are followed. If the option
	 * {@link LinkOption#NOFOLLOW_LINKS NOFOLLOW_LINKS} is present then symbolic
	 * links are not followed.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), exists());
	 * </pre>
	 *
	 * @param options
	 *            - options indicating how symbolic links are handled
	 * @return {@code true} if the file exists; {@code false} if the file does
	 *         not exist or its existence cannot be determined.
	 */
	@Factory
	public static Matcher<Path> exists(LinkOption... options) {
		return new Exists(options);
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * directory.
	 * <p>
	 * By default, symbolic links are followed. If the option
	 * {@link LinkOption#NOFOLLOW_LINKS NOFOLLOW_LINKS} is present then symbolic
	 * links are not followed.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(aDirectory()));
	 * </pre>
	 *
	 * @param options
	 *            - options indicating how symbolic links are handled
	 * @return {@code true} if the path is a directory; {@code false} if the
	 *         path does not exist, is not a directory, or it cannot be
	 *         determined if the path is a directory or not.
	 */
	@Factory
	public static Matcher<Path> aDirectory(LinkOption... options) {
		return new Directory(options);
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a regular
	 * file.
	 * <p>
	 * By default, symbolic links are followed. If the option
	 * {@link LinkOption#NOFOLLOW_LINKS NOFOLLOW_LINKS} is present then symbolic
	 * links are not followed.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(not(aRegularFile())));
	 * </pre>
	 *
	 * @param options
	 *            - options indicating how symbolic links are handled
	 * @return {@code true} if the path is a regular file; {@code false} if the
	 *         path does not exist, is not a regular file, or it cannot be
	 *         determined if the path is a regular file or not.
	 */
	@Factory
	public static Matcher<Path> aRegularFile(LinkOption... options) {
		return new RegularFile(options);
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a symbolic
	 * link.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(executable()));
	 * </pre>
	 *
	 * @return {@code true} if the path is a symbolic link; {@code false} if the
	 *         path does not exist, is not a symbolic link, or it cannot be
	 *         determined if the path is a symbolic link or not.
	 */
	@Factory
	public static Matcher<Path> symbolicLink() {
		return new SymbolicLink();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a readable.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(readable()));
	 * </pre>
	 *
	 * @return {@code true} if the path exists and is readable; {@code false} if
	 *         the path does not exist, read access would be denied because the
	 *         Java virtual machine has insufficient privileges, or access
	 *         cannot be determined
	 */
	@Factory
	public static Matcher<Path> readable() {
		return new Readable();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a writable.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(writable()));
	 * </pre>
	 *
	 * @return {@code true} if the path exists and is writable; {@code false} if
	 *         the path does not exist, write access would be denied because the
	 *         Java virtual machine has insufficient privileges, or access
	 *         cannot be determined
	 */
	@Factory
	public static Matcher<Path> writable() {
		return new Writable();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * executable. The semantics may differ when checking access to a directory.
	 * For example, on UNIX systems, checking for execute access checks that the
	 * Java virtual machine has permission to search the directory in order to
	 * access file or subdirectories.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(executable()));
	 * </pre>
	 *
	 * @return {@code true} if the path exists and is executable; {@code false}
	 *         if the path does not exist, execute access would be denied
	 *         because the Java virtual machine has insufficient privileges, or
	 *         access cannot be determined
	 */
	@Factory
	public static Matcher<Path> executable() {
		return new Executable();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>hidden</em>. The exact definition of hidden is platform or provider
	 * dependent. On UNIX for example a file is considered to be hidden if its
	 * name begins with a period character ('.'). On Windows a file is
	 * considered hidden if it isn't a directory and the DOS
	 * {@link DosFileAttributes#isHidden hidden} attribute is set.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(not(hidden())));
	 * </pre>
	 *
	 * @return {@code true} if the path exists and is hidden; {@code false} if
	 *         the path does not exist, the file is not hidden, or access cannot
	 *         be determined
	 */
	@Factory
	public static Matcher<Path> hidden() {
		return new Hidden();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} describes the
	 * same file system object as another {@link Path}, even if relative paths
	 * descend into and out of sub-directories, or symbolic links are used to
	 * arrive at the file via another path.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp/../tmp"), is(sameFile(Paths.get("/tmp"))));
	 * </pre>
	 *
	 * @return {@code true} if, and only if, the two paths locate the same file
	 */
	@Factory
	public static Matcher<Path> sameFile(Path expected) {
		return new SameFile(expected);
	}

}
