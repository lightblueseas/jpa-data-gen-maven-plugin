package de.alpharogroup.jpa.data.gen;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

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

	@Component
	PluginDescriptor descriptor;

	// @Parameter(defaultValue = "${project}", required = true, readonly = true)
	@Component
	MavenProject project;

	protected void addDependenciesToClasspath(final String artifactId)
	{
		for (final Artifact artifact :
			project.getDependencyArtifacts())
		{
			if (artifact.getArtifactId().equals(artifactId))
			{
				try
				{
					final URL url = artifact.getFile().toURI().toURL();
					final ClassRealm realm = descriptor.getClassRealm();
					realm.addURL(url);
				}
				catch (final MalformedURLException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}


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
			final URL[] runtimeUrls = new URL[elements.size()];
			for (int i = 0; i < elements.size(); i++)
			{
				final String element = elements.get(i);
				runtimeUrls[i] = new File(element).toURI().toURL();
			}
			final URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
				Thread.currentThread().getContextClassLoader());

			Thread.currentThread().setContextClassLoader(newLoader);


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