package javacc.compilation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class ThePluginCompilesJjtreeToExpectedDirectoryStory {
    private static final String CLEAN = "clean";
    private static final String COMPILE_JJ_TREE = "compileJJTree";

    @Test
    public void givenAMultiProjectBuildWithJJTreeWhenExecuteCompileJJTreeTaskThenTheFilesAreGeneratedInTheDefaultDirectory()
        throws URISyntaxException, IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("multiprojectBuildWithJJTree");
        steps.whenTasks(CLEAN, ":subprojects/subproject1:compileJJTree").execute();

        String buildDirectory = "subprojects" + File.separator + "subproject1" + File.separator + "build" + File.separator + "generated";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }

    @Test
    public void givenAMultiProjectBuildWithJJTreeThatConfiguresTheInputOutputDirectoriesWhenExecuteCompileJJTreeTaskThenTheFilesAreGeneratedInTheDefaultDirectory()
        throws URISyntaxException, IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("multiprojectBuildWithJJTreeAndWithConfiguredInputsOutputs");
        steps.whenTasks(CLEAN, ":subprojects/subproject1:compileJJTree").execute();

        String buildDirectory = "subprojects" + File.separator + "subproject1" + File.separator + "build" + File.separator + "output";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }

    @Test
    public void givenASimpleJJTreeProjectWhenExecuteCompileJJTreeTaskThenTheFilesAreGeneratedInTheDefaultDirectory() throws URISyntaxException,
        IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("simpleJJTreeTest");
        steps.whenTasks(CLEAN, COMPILE_JJ_TREE).execute();

        String buildDirectory = "build" + File.separator + "generated";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }
    
    @Test
    public void givenASimpleJJTreeProjectWithPackagesWhenExecuteCompileJJTreeTaskThenTheFilesAreGeneratedInThePackageDirectory() throws URISyntaxException,
        IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("simpleJJTreeTestWithPackages");
        steps.whenTasks(CLEAN, COMPILE_JJ_TREE).execute();

        String buildDirectory = "build" + File.separator + "generated";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree" + File.separator + "ast");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");
        
        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree" + File.separator + "test" + File.separator + "pkg");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }

    @Test
    public void givenASimpleJJTreeProjectAndJavaccArgumentsProvidedWhenExecuteCompileJJTreeTaskThenTheFilesAreGeneratedInTheDefaultDirectory()
        throws URISyntaxException, IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("simpleJJTreeTestWithArguments");
        steps.whenTasks(CLEAN, COMPILE_JJ_TREE).execute();

        String buildDirectory = "build" + File.separator + "generated";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree");
        steps.andAssertFileWasGenerated("grammar.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }

    @Test
    public void givenASimpleJJTreeProjectThatConfiguresTheInputOutputDirectoriesWhenExecuteCompileJJTreeTaskThenTheFilesAreGeneratedInTheConfiguredDirectory()
        throws URISyntaxException, IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("simpleJJTreeTestWithConfiguredInputsOutputs");
        steps.whenTasks(CLEAN, COMPILE_JJ_TREE).execute();

        steps.thenAssertOutputDirectoryExists("build" + File.separator + "output");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileWasGenerated("HelloTreeConstants.java");
        steps.andAssertFileWasGenerated("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists("build" + File.separator + "generated" + File.separator + "javacc");
    }
    
    @Test
    public void givenASimpleJJTreeProjectWhenExecuteCompileJJTreeTaskThenTheFilesThatDoNotHaveACorrespondingCustomAstClassAreGeneratedInTheDefaultDirectory()
        throws URISyntaxException, IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("simpleJJTreeTestWithCustomAstClasses");
        steps.whenTasks(CLEAN, COMPILE_JJ_TREE).execute();

        String buildDirectory = "build" + File.separator + "generated";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileExistsButWasNotGenerated("HelloTreeConstants.java");
        steps.andAssertFileDoesNotExist("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }
    
    @Test
    public void givenASimpleJJTreeProjectWhenRerunCompileJJTreeTaskThenTheFilesThatDoNotHaveACorrespondingCustomAstClassAreGeneratedInTheDefaultDirectory()
        throws URISyntaxException, IOException {
        
        CompilationSteps steps = new CompilationSteps();

        steps.givenAProjectNamed("simpleJJTreeTestWithCustomAstClasses");
        steps.whenTasks(CLEAN, COMPILE_JJ_TREE).execute();
        steps.whenTasks(COMPILE_JJ_TREE).withArguments("--rerun-tasks").execute();

        String buildDirectory = "build" + File.separator + "generated";

        steps.thenAssertOutputDirectoryExists(buildDirectory + File.separator + "jjtree");
        steps.andAssertFileWasGenerated("JJTreeOutputTest.jj");
        steps.andAssertFileExistsButWasNotGenerated("HelloTreeConstants.java");
        steps.andAssertFileDoesNotExist("JJTHelloState.java");
        steps.andAssertFileWasGenerated("Node.java");
        steps.andAssertFileWasGenerated("SimpleNode.java");

        steps.thenAssertOutputDirectoryDoesNotExists(buildDirectory + File.separator + "javacc");
    }
}
