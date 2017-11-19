/**
 * Copyright (C) 2017 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		for (final Artifact artifact : project.getDependencyArtifacts())
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

		Thread.currentThread().setContextClassLoader(getClassLoader(Thread.currentThread().getContextClassLoader()));

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


	protected ClassLoader getClassLoader(final ClassLoader parentClassLoader) throws MojoExecutionException
	{
		try
		{
			final List<String> classpathElements = project.getCompileClasspathElements();
			classpathElements.add(project.getBuild().getOutputDirectory());
			classpathElements.add(project.getBuild().getTestOutputDirectory());
			final URL urls[] = new URL[classpathElements.size()];

			for (int i = 0; i < classpathElements.size(); ++i)
			{
				urls[i] = new File(classpathElements.get(i)).toURI().toURL();
			}
			if(parentClassLoader != null) {
				return new URLClassLoader(urls, parentClassLoader);
			}
			return new URLClassLoader(urls, getClass().getClassLoader());
		}
		catch (final Exception e)// gotta catch em all
		{
			throw new MojoExecutionException("Couldn't create a classloader.", e);
		}
	}


	public void tryLoadProjectClasses()
	{
		try
		{
			final Set<URL> urls = new HashSet<>();
			final List<String> elements = new ArrayList<>();

			elements.addAll(project.getTestClasspathElements());
			elements.addAll(project.getRuntimeClasspathElements());
			elements.addAll(project.getCompileClasspathElements());
			elements.addAll(project.getSystemClasspathElements());
			final ClassRealm realm = descriptor.getClassRealm();

			for (final String element : elements)
			{
				final File elementFile = new File(element);
				realm.addURL(elementFile.toURI().toURL());
			}

		}
		catch (final DependencyResolutionRequiredException e)
		{
			throw new RuntimeException(e);
		}
		catch (final MalformedURLException e)
		{
			throw new RuntimeException(e);
		}
	}

}