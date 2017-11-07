package de.alpharogroup.jpa.data.gen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "jpa-generator")
public class GeneratorMojo extends AbstractMojo
{

	@Parameter(property = "parent", defaultValue = "my parent")
	private String parent;

	@Override
	public void execute() throws MojoExecutionException
	{
		getLog().info("Hello " + parent);
	}

	public String getParent()
	{
		return parent;
	}

	public void setParent(final String parent)
	{
		this.parent = parent;
	}

}