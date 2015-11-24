package ca.coglinc.gradle.plugins.javacc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import groovy.lang.Closure;

public class JavaToJavaccDependencyActionTest {
    private Project project;

    @Before
    public void createProject() {
        project = ProjectBuilder.builder().build();
    }

    private void applyJavaccPluginToProject() {
        Map<String, String> pluginNames = new HashMap<String, String>(1);
        pluginNames.put("plugin", "ca.coglinc.javacc");
        project.apply(pluginNames);
    }

    private void applyJavaPluginToProject() {
        Map<String, String> pluginNames = new HashMap<String, String>(1);
        pluginNames.put("plugin", "java");
        project.apply(pluginNames);
    }
    
    private void addDependencyConfigurationExtensionClosuresToRemoveDependencies() {
    	DependencyConfigurationExtension extension = (DependencyConfigurationExtension) project.getExtensions().getByName(DependencyConfigurationExtension.DEPENDENCYCONFIGURATIONEXTENSION_NAME);
    	extension.compileTasksDependenciesForJavacc = new Closure<TaskCollection<JavaCompile>>(this) {
			private static final long serialVersionUID = 1L;
			@Override
			public TaskCollection<JavaCompile> call(Object... arg0) {
				@SuppressWarnings("unchecked")
				TaskCollection<JavaCompile> initialCollection = (TaskCollection<JavaCompile>) arg0[0];
				return initialCollection.matching(new Spec<JavaCompile>() {
					@Override
					public boolean isSatisfiedBy(JavaCompile arg0) {
						return false;
					}
				});
			}    		
		};
		extension.compileTasksDependenciesForJjTree = new Closure<TaskCollection<JavaCompile>>(this) {
			private static final long serialVersionUID = 1L;
			@Override
			public TaskCollection<JavaCompile> call(Object... arg0) {
				@SuppressWarnings("unchecked")
				TaskCollection<JavaCompile> initialCollection = (TaskCollection<JavaCompile>) arg0[0];
				return initialCollection.matching(new Spec<JavaCompile>() {
					@Override
					public boolean isSatisfiedBy(JavaCompile arg0) {
						return false;
					}
				});
			}    		
		};		
    }

    @Test
    public void compileJavaDependsOnCompileJavaccAfterExecutionWhenJavaPluginApplied() {
        applyJavaccPluginToProject();
        applyJavaPluginToProject();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Set<Object> dependencies = task.getDependsOn();
            Assert.assertTrue(dependencies.contains(project.getTasks().findByName(CompileJavaccTask.TASK_NAME_VALUE)));
        }
    }
    
    @Test
    public void compileJavaDoNotDependsOnCompileJavaccAfterExecutionWhenJavaPluginAppliedAndClosureRemoveDependencies() {
        applyJavaccPluginToProject();
        applyJavaPluginToProject();
        addDependencyConfigurationExtensionClosuresToRemoveDependencies();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Set<Object> dependencies = task.getDependsOn();
            Assert.assertTrue(!dependencies.contains(project.getTasks().findByName(CompileJavaccTask.TASK_NAME_VALUE)));
        }
    }

    @Test
    public void generatedJavaFilesFromCompileJavaccAreAddedToMainJavaSourceSet() {
        applyJavaccPluginToProject();
        applyJavaPluginToProject();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();
        final File outputDirectory = new File(getClass().getResource("/javacc/testgenerated").getFile());
        CompileJavaccTask compileJavaccTask = (CompileJavaccTask) project.getTasks().findByName(CompileJavaccTask.TASK_NAME_VALUE);
        compileJavaccTask.setOutputDirectory(outputDirectory);

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Assert.assertTrue(task.getSource().contains(new File(outputDirectory, "someSourceFile.txt")));
        }
    }
    
    @Test
    public void generatedJavaFilesFromCompileJavaccAreNotAddedToMainJavaSourceSetWhenClosureRemoveDependencies() {
        applyJavaccPluginToProject();
        applyJavaPluginToProject();
        addDependencyConfigurationExtensionClosuresToRemoveDependencies();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();
        final File outputDirectory = new File(getClass().getResource("/javacc/testgenerated").getFile());
        CompileJavaccTask compileJavaccTask = (CompileJavaccTask) project.getTasks().findByName(CompileJavaccTask.TASK_NAME_VALUE);
        compileJavaccTask.setOutputDirectory(outputDirectory);

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Assert.assertTrue(!task.getSource().contains(new File(outputDirectory, "someSourceFile.txt")));
        }
    }

    @Test
    public void generatedJavaccFilesFromCompileJJTreeAreAddedToCompileJavaccSourceSet() {
        applyJavaccPluginToProject();
        applyJavaPluginToProject();

        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();
        final File inputDirectory = new File(getClass().getResource("/jjtree/input").getFile());
        final File outputDirectory = new File(getClass().getResource("/jjtree/testgenerated").getFile());
        CompileJjTreeTask compileJJTreeTask = (CompileJjTreeTask) project.getTasks().findByName(CompileJjTreeTask.TASK_NAME_VALUE);
        compileJJTreeTask.setInputDirectory(inputDirectory);
        compileJJTreeTask.setOutputDirectory(outputDirectory);

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Assert.assertTrue(task.getSource().contains(new File(outputDirectory, "someSourceFile.jj")));
        }

        TaskCollection<CompileJavaccTask> compileJavaccsTasks = project.getTasks().withType(CompileJavaccTask.class);
        for (CompileJavaccTask task : compileJavaccsTasks) {
            Assert.assertTrue(task.getSource().contains(new File(outputDirectory, "someSourceFile.jj")));
        }
    }

    @Test
    public void compileJavaDoesNotDependOnCompileJavaccWhenJavaccPluginNotApplied() {
        applyJavaPluginToProject();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Set<Object> dependencies = task.getDependsOn();
            Assert.assertFalse(dependencies.contains(project.getTasks().findByName(CompileJavaccTask.TASK_NAME_VALUE)));
        }
    }

    @Test
    public void compileJavaDoesNotDependOnCompileJJTreeWhenJavaccPluginNotApplied() {
        applyJavaPluginToProject();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        action.execute(project);

        TaskCollection<JavaCompile> javaCompilationTasks = project.getTasks().withType(JavaCompile.class);
        for (JavaCompile task : javaCompilationTasks) {
            Set<Object> dependencies = task.getDependsOn();
            Assert.assertFalse(dependencies.contains(project.getTasks().findByName(CompileJjTreeTask.TASK_NAME_VALUE)));
        }
    }

    @Test
    public void compileJavaccDependOnCompileJJTreeWhenInputDirectoryNotEmpty() {
        applyJavaPluginToProject();
        applyJavaccPluginToProject();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        final File inputDirectory = new File(getClass().getResource("/jjtree/input").getFile());
        CompileJjTreeTask compileJJTreeTask = (CompileJjTreeTask) project.getTasks().findByName(CompileJjTreeTask.TASK_NAME_VALUE);
        compileJJTreeTask.setInputDirectory(inputDirectory);

        action.execute(project);

        TaskCollection<CompileJavaccTask> compileJavaccTasks = project.getTasks().withType(CompileJavaccTask.class);
        for (CompileJavaccTask task : compileJavaccTasks) {
            Set<Object> dependencies = task.getDependsOn();
            Assert.assertTrue(dependencies.contains(project.getTasks().findByName(CompileJjTreeTask.TASK_NAME_VALUE)));
        }
    }

    @Test
    public void compileJavaccDoesNotDependOnCompileJJTreeWhenInputDirectoryEmpty() {
        applyJavaPluginToProject();
        applyJavaccPluginToProject();
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        final File inputDirectory = new File(getClass().getResource("/empty").getFile());
        CompileJjTreeTask compileJJTreeTask = (CompileJjTreeTask) project.getTasks().findByName(CompileJjTreeTask.TASK_NAME_VALUE);
        compileJJTreeTask.setInputDirectory(inputDirectory);

        action.execute(project);

        TaskCollection<CompileJavaccTask> compileJavaccTasks = project.getTasks().withType(CompileJavaccTask.class);
        for (CompileJavaccTask task : compileJavaccTasks) {
            Set<Object> dependencies = task.getDependsOn();
            Assert.assertFalse(dependencies.contains(project.getTasks().findByName(CompileJjTreeTask.TASK_NAME_VALUE)));
        }
    }

    @Test
    public void noInteractionsWithProjectIfJavaPluginNotApplied() {
        project = Mockito.mock(Project.class, Answers.RETURNS_MOCKS.get());
        JavaToJavaccDependencyAction action = new JavaToJavaccDependencyAction();

        action.execute(project);

        Mockito.verify(project).getPlugins();
        Mockito.verifyNoMoreInteractions(project);
    }
}
