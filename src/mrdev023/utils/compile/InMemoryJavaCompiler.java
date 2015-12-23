package mrdev023.utils.compile;

import java.util.*;

import javax.tools.*;

import mrdev023.*;

public class InMemoryJavaCompiler {
    static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

    public static Class<?> compile(String className, String sourceCodeInText) throws Exception {
//    	System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_51\\bin");
        SourceCode sourceCode = new SourceCode(className, sourceCodeInText);
        CompiledCode compiledCode = new CompiledCode(className);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceCode);
        DynamicClassLoader cl = new DynamicClassLoader(ClassLoader.getSystemClassLoader());
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        ExtendedStandardJavaFileManager fileManager = new ExtendedStandardJavaFileManager(javac.getStandardFileManager(diagnostics, null, null),compiledCode, cl);
        JavaCompiler.CompilationTask task = javac.getTask(null, fileManager, null, null, null, compilationUnits);
        boolean result = task.call();
        if(!result)System.out.println(diagnostics.getDiagnostics());
        if(cl != null)
        return cl.loadClass(className);
        else return null;
    }
}
