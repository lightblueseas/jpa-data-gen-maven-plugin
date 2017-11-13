package de.alpharogroup.jpa.data.gen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import de.alpharogroup.gen.src.generator.GeneratorExtensions;
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

	@Parameter(property = "absoluteProjectPath", defaultValue=".")
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
			GeneratorExtensions.generatePomFiles();
			GeneratorExtensions.generateRepositoryClasses(true);
		}
		catch (final Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MojoExecutionException("", e);
		}
	}

}