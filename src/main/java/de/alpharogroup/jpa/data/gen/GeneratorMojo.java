package de.alpharogroup.jpa.data.gen;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import de.alpharogroup.gen.src.generator.GeneratorExtensions;
import de.alpharogroup.gen.src.model.PomGenerationModelBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Mojo(name = "jpa-generator"
// , configurator="include-project-dependencies"
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class GeneratorMojo extends AbstractMojo
{

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	@Parameter(property = "absoluteProjectPath", defaultValue = ".")
	String absoluteProjectPath;

	@Parameter(property = "basePackageName", required = true)
	String basePackageName;

	@Parameter(property = "dataProjectName")
	String dataProjectName;

	@Parameter(property = "dataProjectParentVersion")
	String dataProjectParentVersion;

	@Parameter(property = "dataProjectName")
	String dataProjectVersion;

	@Parameter(property = "parentName", required = true)
	String parentName;

	@Parameter(property = "password", required = true)
	String password;

	@Parameter(property = "persistenceunitName", required = true)
	String persistenceunitName;

	@Override
	public void execute() throws MojoExecutionException
	{
		getLog().info("Parameter 'parentName' is:" + parentName);
		getLog().info("Parameter 'dataProjectName' is:" + dataProjectName);
		getLog().info("Parameter 'dataProjectVersion' is:" + dataProjectVersion);
		getLog().info("Parameter 'basePackageName' is:" + basePackageName);
		getLog().info("Parameter 'dataProjectParentVersion' is:" + dataProjectParentVersion);
		getLog().info("Parameter 'persistenceunitName' is:" + persistenceunitName);
		getLog().info("Parameter 'password' is:" + password);
		getLog().info("Parameter 'absoluteProjectPath' is:" + absoluteProjectPath);

		try
		{
			final Set<URL> urls = new HashSet<>();
			final List<String> elements = new ArrayList<>();

			elements.addAll(project.getTestClasspathElements());
			elements.addAll(project.getRuntimeClasspathElements());
			 elements.addAll(project.getCompileClasspathElements());
			 elements.addAll(project.getSystemClasspathElements());
			for (final String element : elements)
			{
				urls.add(new File(element).toURI().toURL());
			}

			final ClassLoader contextClassLoader = URLClassLoader.newInstance(
				urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());

			Thread.currentThread().setContextClassLoader(contextClassLoader);

		}
		catch (final DependencyResolutionRequiredException e)
		{
			throw new RuntimeException(e);
		}
		catch (final MalformedURLException e)
		{
			throw new RuntimeException(e);
		}


		final PomGenerationModelBean pomGenerationModelBean = PomGenerationModelBean.builder()
			.absoluteProjectPath(absoluteProjectPath).basePackageName(basePackageName)
			.dataProjectName(dataProjectName).dataProjectParentVersion(dataProjectParentVersion)
			.dataProjectVersion(dataProjectVersion).parentName(parentName).password(password)
			.persistenceunitName(persistenceunitName).build();

		getLog().info(pomGenerationModelBean.toString());
		try
		{
			GeneratorExtensions.generate(pomGenerationModelBean, true);
		}
		catch (final Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MojoExecutionException("", e);
		}
	}

}