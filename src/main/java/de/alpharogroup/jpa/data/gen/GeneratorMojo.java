package de.alpharogroup.jpa.data.gen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Mojo(name = "jpa-generator")
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
public class GeneratorMojo extends AbstractMojo
{

	@Parameter(property = "parentName", required = true)
	String parentName;

	@Parameter(property = "dataProjectName")
	String dataProjectName;

	@Parameter(property = "dataProjectName")
	String dataProjectVersion;

	@Parameter(property = "basePackageName", required = true)
	String basePackageName;

	@Parameter(property = "persistenceunitName", required = true)
	String persistenceunitName;

	@Parameter(property = "dataProjectParentVersion")
	String dataProjectParentVersion;

	@Override
	public void execute() throws MojoExecutionException
	{
		getLog().info("Hello " + parentName);
		getLog().info("Hello " + dataProjectName);
		getLog().info("Hello " + dataProjectVersion);
		getLog().info("Hello " + basePackageName);
		getLog().info("Hello " + dataProjectParentVersion);
		getLog().info("Hello " + persistenceunitName);

//		try
//		{
//			GeneratorExtensions.generatePomFiles();
//			GeneratorExtensions.generateRepositoryClasses(false);
//		}
//		catch (final Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new MojoExecutionException("", e);
//		}
	}

}