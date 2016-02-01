Hamcrest-Path  [![Build Status](https://travis-ci.org/seinesoftware/hamcrest-path.svg?branch=master)](https://travis-ci.org/seinesoftware/hamcrest-path)
=============

Licensed under [BSD License][].

What is Hamcrest-Path?
----------------------
Hamcrest-Path is a [hamcrest][] extension library,
which provides a suite of hamcrest-style matchers for file/directory existence and permissions.


Downloads
---------
You can obtain the hamcrest-path binaries from [maven central][], or download them automatically in maven using:

	<dependencies>
	    <dependency>
	        <groupId>ca.seinesoftware</groupId>
	        <artifactId>hamcrest-path</artifactId>
	        <version>1.0.0</version>
	        <scope>test</scope>
	    </dependency>
	</dependencies>

Usage
-----
The following code tests whether the user's home directory exists,
is readable, and is writable:

    import static ca.seinesoftware.hamcrest.path.PathMatcher.*;
    import static org.hamcrest.Matchers.is;
    import static org.junit.Assert.assertThat;

    import java.nio.file.Path;
    import java.nio.file.Paths;

    import org.junit.Test;

    public class HomeTest {
        @Test
        public void testHomeDirectory() {
            Path home = Paths.get(System.getProperty("user.home"));
            assertThat(home, exists());
            assertThat(home, is(readable()));
            assertThat(home, is(writable()));
        }
    }

Reporting Bugs/Issues
---------------------
If you find an issue with Java Hamcrest, please report it via the 
[GitHub issue tracker](https://github.com/seinesoftware/hamcrest-path/issues), 
after first checking that it hasn't been raised already. 

[BSD License]: http://opensource.org/licenses/BSD-3-Clause
[Maven central]: http://search.maven.org/#search%7Cga%7C1%7Cg%3Aca.seinesoftware
[hamcrest]: https://github.com/hamcrest/JavaHamcrest
