package com.sangupta.resumemaker.velocity.directives;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

public abstract class AbstractDirective extends Directive {

	protected Object getArgument(Node node, InternalContextAdapter context, int index) {
		if(node.jjtGetNumChildren() > index && node.jjtGetChild(index) != null) {
			return node.jjtGetChild(index).value(context);
		}
		
		return null;
	}

}
