package ca.coglinc.gradle.plugins.javacc;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.file.RelativePath;
import org.gradle.api.tasks.TaskAction;
import org.javacc.jjtree.JJTree;

public class CompileJjTreeTask extends AbstractJavaccTask {
    public static final String TASK_NAME_VALUE = "compileJjtree";
    public static final String TASK_DESCRIPTION_VALUE = "Compiles JJTree files into JavaCC files";

    private static final String DEFAULT_INPUT_DIRECTORY = File.separator + "src" + File.separator + "main" + File.separator + "jjtree";
    private static final String DEFAULT_OUTPUT_DIRECTORY = File.separator + "generated" + File.separator + "jjtree";
    private static final String SUPPORTED_FILE_SUFFIX = ".jjt";

    public CompileJjTreeTask() {
        super(CompileJjTreeTask.DEFAULT_INPUT_DIRECTORY, CompileJjTreeTask.DEFAULT_OUTPUT_DIRECTORY, "**/*" + SUPPORTED_FILE_SUFFIX);
    }
    
    @TaskAction
    public void run() {
        getTempOutputDirectory().mkdirs();

        compileSourceFilesToTempOutputDirectory();
        copyCompiledFilesFromTempOutputDirectoryToOutputDirectory();
        copyNonJavaccFilesToOutputDirectory();
        FileUtils.deleteQuietly(getTempOutputDirectory());
    }

    @Override
    protected void augmentArguments(File inputDirectory, RelativePath inputRelativePath, ProgramArguments arguments) {
        arguments.add("JJTREE_OUTPUT_DIRECTORY", inputRelativePath.getFile(getTempOutputDirectory()).getParentFile().getAbsolutePath());
    }

    @Override
    protected String getProgramName() {
        return "JJTree";
    }

    @Override
    protected void invokeCompiler(ProgramArguments arguments) throws Exception {
        int errorCode = new JJTree().main(arguments.toArray());
        if (errorCode != 0) {
            throw new IllegalStateException("JJTree failed with error code: [" + errorCode + "]");
        }
    }

    @Override
    protected FileVisitor getJavaccSourceFileVisitor() {
        return new JavaccSourceFileVisitor(this);
    }

    @Override
    protected String supportedSuffix() {
        return SUPPORTED_FILE_SUFFIX;
    }
}
