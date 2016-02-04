Hamcrest-Path  [![Build Status](https://travis-ci.org/seinesoftware/hamcrest-path.svg?branch=master)](https://travis-ci.org/seinesoftware/hamcrest-path)
=============

Licensed under [BSD License][].

What is Hamcrest-Path?
----------------------
Hamcrest-Path is a [Java Hamcrest][] extension library,
which provides a suite of hamcrest-style matchers for file/directory existence and permissions.

Without the hamcrest-path library, testing whether a file is readable would need to be written as:

    Path path = Paths.get("/path/to/some/file");
    assertThat(Files.isReadable(path), is(true));

If the test failed, a non-informative error message would be generated stating
an accurate but cryptic message:

    Expected: is <true>, but: was <false>

With the hamcrest-path library, the test can be re-written as:

    Path path = Paths.get("/path/to/some/file");
    assertThat(path, is(readable()));

Not only is this more concise, but if the assertion fails, a more informative error message is generated:

    Expected: is a readable file or directory, but: </path/to/some/file> does not exist. 


Downloads
---------
You can obtain the hamcrest-path binaries from [maven central][], To include in your project:

A Maven project:

    <dependency>
	    <groupId>ca.seinesoftware</groupId>
	    <artifactId>hamcrest-path</artifactId>
	    <version>1.0.0</version>
	    <scope>test</scope>
	</dependency>

A project which uses ivy for dependency management:

    <dependency org="ca.seinesoftware" name="hamcrest-path" rev="1.0.0"/>

Usage
-----
The following code tests whether the user's home directory exists,
and is both readable and writable:

    import static ca.seinesoftware.hamcrest.path.PathMatcher.*;
    import static org.hamcrest.Matchers.*;
    import static org.junit.Assert.assertThat;
    
    import java.nio.file.Path;
    import java.nio.file.Paths;
    
    import org.junit.Test;
    
    public class HomeTest {
        @Test
        public void testHomeDirectory() {
            Path home = Paths.get(System.getProperty("user.home"));
            assertThat(home, exists());
            assertThat(home, is(aDirectory());
            assertThat(home, is(both(readable()).and(writable())));
        }
    }

Other matchers include:
* __aRegularFile__ - Tests whether the file system entry is a regular file
* __symbolicLink__ - Tests whether the file system entry is a symbolic link
* __sameFile__ - Tests if two paths reference the same file system entry
* __executable__ - Test whether the user has execute permissions for the file system entry
* __hidden__ - Test whether the file system entry is hidden


Reporting Bugs/Issues
---------------------
If you find an issue with Java Hamcrest, please report it via the 
[GitHub issue tracker](https://github.com/seinesoftware/hamcrest-path/issues), 
after first checking that it hasn't been raised already. 


Acknowledgements
----------------
Thanks to the developers at [Java Hamcrest][].
Without their hardwork and core libraries there'd be nothing to be extend and we be stuck with old school,
non-declarative, non-reusable, assertions.


[BSD License]: http://opensource.org/licenses/BSD-3-Clause
[Maven central]: http://search.maven.org/#search%7Cga%7C1%7Cg%3Aca.seinesoftware
[Java Hamcrest]: http://github.com/hamcrest/JavaHamcrest
