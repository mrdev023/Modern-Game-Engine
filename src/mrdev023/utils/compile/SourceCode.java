package mrdev023.utils.compile;

import java.io.*;
import java.net.*;

import javax.tools.*;
import javax.tools.JavaFileObject.*;

public class SourceCode extends SimpleJavaFileObject {
    private String contents = null;

    public SourceCode(String className, String contents) throws Exception {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = contents;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }
}