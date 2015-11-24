package ca.coglinc.gradle.plugins.javacc;

import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.JavaCompile;

import groovy.lang.Closure;

public class DependencyConfigurationExtension {
	public static final String DEPENDENCYCONFIGURATIONEXTENSION_NAME = "DependancyConfiguration";
	
	/**
	 * This closure should have two arguments, 
	 * 		- TaskCollection\<JavaCompile\> : contain all the project's compile targets 
	 * 		- Task the javacc task 
	 * This closure need to return a TaskCollection\<JavaCompile\>. Theses tasks will depend on the javacc task.
	 * If this closure is not set, all the project's compile target depends on the javacc task
	 */
	public Closure<TaskCollection<JavaCompile>> compileTasksDependenciesForJavacc;
	/**
	 * This closure should have two arguments, 
	 * 		- TaskCollection\<JavaCompile\> : contain all the project's compile targets 
	 * 		- Task the jjtree task 
	 * This closure need to return a TaskCollection\<JavaCompile\>. Theses tasks will depend on the jjtree task and will include files generated by the jjtree task.
	 * If this closure is not set, all the project's compile target depends on the jjtree task
	 */
	public Closure<TaskCollection<JavaCompile>> compileTasksDependenciesForJjTree;
}
