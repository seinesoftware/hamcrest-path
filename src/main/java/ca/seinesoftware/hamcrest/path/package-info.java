
/**
 * Provides Hamcrest Matchers for testing whether {@link java.nio.file.Path
 * Path} objects correspond to files, directories, and/or symbolic links, and
 * whether or not the corresponding file system objects are readable, writable,
 * and/or executable.
 *
 * <p>
 * Note that the result of any test is immediately outdated. If a test indicates
 * the existence or accessibility of a file system object, there is no guarantee
 * that a subsequence access will succeed. Care should be taken when using this
 * method in security sensitive applications.
 *
 * @since 1.0
 * @author Arthur Neufeld &lt;aneufeld@seinesoftware.ca&gt;
 *
 */
package ca.seinesoftware.hamcrest.path;