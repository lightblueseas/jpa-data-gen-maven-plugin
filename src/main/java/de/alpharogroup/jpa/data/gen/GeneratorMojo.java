package de.alpharogroup.jpa.data.gen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Mojo(name = "jpa-generator")
public class GeneratorMojo extends AbstractMojo
{

	@Getter
	@Setter
	@Parameter(property = "parent", defaultValue = "default-parent")
	private String parent;

	@Override
	public void execute() throws MojoExecutionException
	{
		getLog().info("Hello " + parent);
	}

}