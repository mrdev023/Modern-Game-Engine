package mrdev023.utils.compile;

import java.io.*;

import javax.tools.*;

import mrdev023.*;

public class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private CompiledCode compiledCode;
    private DynamicClassLoader cl;

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param cl
     */
    protected ExtendedStandardJavaFileManager(JavaFileManager fileManager, CompiledCode compiledCode, DynamicClassLoader cl) {
        super(fileManager);
        this.compiledCode = compiledCode;
        this.cl = cl;
        this.cl.setCode(compiledCode);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return compiledCode;
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return cl;
    }
}