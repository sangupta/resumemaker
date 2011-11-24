package com.sangupta.resumemaker.velocity.directives;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.pegdown.PegDownProcessor;

public class MarkdownDirective extends AbstractDirective {

	@Override
	public String getName() {
		return "markdown";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		String value = null;
		value = (String) getArgument(node, context, 0);
		
		if(value != null) {
			PegDownProcessor processor = new PegDownProcessor();
			String html = processor.markdownToHtml(value);
			writer.write(html);
		}
		
		return true;
	}

}
