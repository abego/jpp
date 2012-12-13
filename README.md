jpp - A Simple Java Preprocessor for Ant
========================================

About jpp
---------

jpp is an Ant task for preprocessing text files, especially Java source
files.

Its main purpose is to allow “conditional compilation”. Other features
commonly provided by text preprocessors, like inclusion of files or
macro expansion, are not supported.

Ant provides the jpp task with properties that control the preprocessing
of the files. Using conditional directives parts of the text file can be
excluded in the resulting file.

Syntax
------

    #if [!]propertyName 
        ...text 1...
    [#else
        ...text 2...]
    #endif

*(’[ … ]’ encloses optional parts).*

Preprocessing
-------------

jpp analyses a file’s text line-by-line and writes the result to the
destination file.

Here the details of the preprocessing:

-   When the `#if` condition is true the lines following the `#if` (i.e.
    "`...text 1...`") are copied to the destination file, otherwise the
    lines following the `#else` (i.e. "`...text 2...`") are copied.
-   All lines containing a jpp directive (`#if`, `#else`, `#endif`) are
    removed.
-   All lines outside an `#if...#endif` block are copied to the
    destination file.
-   `#if` … `#endif` blocks can be nested, i.e. they can appear in the
    “if” and “else” areas of an outer `#if` … `#endif` block.

An `#if` condition is true if either

-   a single name is specified (e.g. "`#if FOO`") and the property with
    that name holds the value `true`, or
-   a negate operator `!` and a name is given (e.g.
    "`#if                 !FOO`") and no property with that name is
    defined or the property does not hold the value `true`.

Using jpp directives in Java source files
-----------------------------------------

When using jpp directives in Java source files the directives must be
included in Java comments to avoid compile errors.

You may either use single line comments, e.g. as in:

    //#if FOO
        ... regular Java text
    //#endif

or block comments like:

    /*#if FOO
        ... text in comment ...
    #endif*/

When using the block comment approach the text inside the directive
block will not be compiled in the original file. However when the
condition is satisfied the resulting file will contain the text without
the wrapping comment, i.e. it will be compiled.

Examples
--------

    /*#if FOO
        ... text in comment ...
    #else*/
        ... text outside comment ...
    //#endif

    //#if !FOO
    ... text to be excluded when FOO is true ...
    //#endif

Ant integration
---------------

    <project name="YourProjectName" ... >

        <!-- Register the jpp ant task -->
        <taskdef resource="jpp.xml" classpath="lib/jpp/1.0.1/jpp-1.0.1.jar" />
        ...
        
        <!-- defined properties to control the preprocessing -->
        <property name="NO_FOO" value="true" />
        <property name="WITH_BLA" value="true" />
        ...
        
        <!-- call the jpp tool inside a target -->
        <target name="preprocess">
            <jpp destdir="src-gen" readonly="true" verbose="false">
                <fileset dir="src/" includes="**/*.java" excludesfile="config/excludes.txt" />
                <fileset dir="src/" includesfile="config/includes.txt" />
            </jpp>
        </target>
        ...
            
    </project>

License
-------

jpp is distributed under a BSD license of [abego Software][].

Links
-----

Sources: [https://github.com/abego/jpp][]

  [abego Software]: http://www.abego-software.de/index.html
  [https://github.com/abego/jpp]: https://github.com/abego/jpp
