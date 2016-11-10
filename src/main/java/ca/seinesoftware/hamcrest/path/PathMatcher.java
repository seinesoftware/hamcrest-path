package ca.seinesoftware.hamcrest.path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * PathMatcher
 * <p>
 * A static factory for creating {@link org.hamcrest.Matcher} instances for
 * testing whether {@link Path} objects correspond to file system objects, and
 * whether those objects are readable, writable, and/or executable.
 * <p>
 * For example:
 *
 * <pre>
 * import static ca.seinesoftware.hamcrest.path.PathMatcher.*;
 * import static org.hamcrest.Matchers.is;
 * import static org.junit.Assert.assertThat;
 *
 * import java.nio.file.Path;
 * import java.nio.file.Paths;
 *
 * import org.junit.Test;
 *
 * public class HomeTest {
 * 	  &#64;Test
 * 	  public void testHomeDirectory() {
 * 	      Path home = Paths.get(System.getProperty("user.home"));
 * 	      assertThat(home, exists());
 * 	      assertThat(home, is(aDirectory()));
 * 	      assertThat(home, is(both(readable()).and(writable())));
 * 	  }
 * }
 * </pre>
 *
 * <p>
 * <b>Note</b> that the result of any test is <em>immediately outdated</em>. If
 * a test indicates the existence or accessibility of a file system object,
 * there is no guarantee that a subsequence access will succeed. Care should be
 * taken when using these methods in security sensitive applications.
 *
 * @author Arthur Neufeld &lt;aneufeld@seinesoftware.ca&gt;
 */
public abstract class PathMatcher extends TypeSafeMatcher<Path> {

	// ========================================================================
	// Constructor
	// ========================================================================

	private final static LinkOption[] NO_OPTIONS = {};

	/**
	 * Options to indicate how symbolic links are handled. By default, symbolic
	 * links are followed. If the option {@link LinkOption#NOFOLLOW_LINKS
	 * NOFOLLOW_LINKS} is present then symbolic links are not followed.
	 */
	protected final LinkOption[] linkOptions;

	protected PathMatcher(final LinkOption... options) {
		linkOptions = options;
	}

	protected PathMatcher() {
		this(NO_OPTIONS);
	}

	// ========================================================================
	// describeTo
	// ========================================================================

	@Override
	public void describeTo(Description description) {
		for (final LinkOption option : linkOptions) {
			if (option == LinkOption.NOFOLLOW_LINKS) {
				description.appendText("a non-symbolic link to ");
			} else {
				throw new IllegalArgumentException("Unknown option: " + option);
			}
		}
	}

	// ========================================================================
	// describeMismatchSafely
	// ========================================================================

	@Override
	protected void describeMismatchSafely(final Path path, Description description) {
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
	 * determined to <em>exist</em>.
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
	 *            options indicating how symbolic links are handled
	 * @return {@code true} if the file exists; {@code false} if the file does
	 *         not exist or its existence cannot be determined.
	 */
	public static Matcher<Path> exists(final LinkOption... options) {
		return new Exists(options);
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>directory</em>.
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
	 *            options indicating how symbolic links are handled
	 * @return {@code true} if the path is a directory; {@code false} if the
	 *         path does not exist, is not a directory, or it cannot be
	 *         determined if the path is a directory or not.
	 */
	public static Matcher<Path> aDirectory(final LinkOption... options) {
		return new Directory(options);
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>regular file</em>.
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
	 *            options indicating how symbolic links are handled
	 * @return {@code true} if the path is a regular file; {@code false} if the
	 *         path does not exist, is not a regular file, or it cannot be
	 *         determined if the path is a regular file or not.
	 */
	public static Matcher<Path> aRegularFile(final LinkOption... options) {
		return new RegularFile(options);
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>symbolic link</em>.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(not(aSymbolicLink())));
	 * </pre>
	 *
	 * @return {@code true} if the path is a symbolic link; {@code false} if the
	 *         path does not exist, is not a symbolic link, or it cannot be
	 *         determined if the path is a symbolic link or not.
	 */
	public static Matcher<Path> aSymbolicLink() {
		return new SymbolicLink();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>symbolic link</em>.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp"), is(not(symbolicLink())));
	 * </pre>
	 *
	 * @return {@code true} if the path is a symbolic link; {@code false} if the
	 *         path does not exist, is not a symbolic link, or it cannot be
	 *         determined if the path is a symbolic link or not.
	 * @deprecated To be consistent with {@link #aRegularFile(LinkOption...)}
	 *             and {@link #aDirectory(LinkOption...)}, this was renamed to
	 *             {@link #aSymbolicLink()}
	 *
	 */
	@Deprecated
	public static Matcher<Path> symbolicLink() {
		return new SymbolicLink();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>readable</em>.
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
	public static Matcher<Path> readable() {
		return new Readable();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>writable</em>.
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
	public static Matcher<Path> writable() {
		return new Writable();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>executable</em>. The semantics may differ when checking access to a
	 * directory. For example, on UNIX systems, checking for execute access
	 * checks that the Java virtual machine has permission to search the
	 * directory in order to access files or subdirectories.
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
	public static Matcher<Path> executable() {
		return new Executable();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} is a
	 * <em>hidden</em>. The exact definition of hidden is platform or provider
	 * dependent. For example, on UNIX a file is considered to be hidden if
	 * its name begins with a period character ('.'), where as on Windows a
	 * file is considered hidden if it isn't a directory and the DOS
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
	public static Matcher<Path> hidden() {
		return new Hidden();
	}

	/**
	 * Create a matcher that matches if the examined {@link Path} describes the
	 * <em>same file system object</em> as a given {@link Path}. Two distinct
	 * paths can describe the same file system object if relative paths are used
	 * to descend into and/or out of sub-directories, or symbolic links are used
	 * to jump through the file system.
	 *
	 * <p>
	 * For example:
	 *
	 * <pre>
	 * assertThat(Paths.get("/tmp/../tmp"), is(sameFile(Paths.get("/tmp"))));
	 * </pre>
	 *
	 * @param expected
	 *            path to the expected file system object.
	 * @return {@code true} if, and only if, the two paths locate the same file
	 */
	public static Matcher<Path> sameFile(final Path expected) {
		return new SameFile(expected);
	}

}
