package com.sangupta.resumemaker;

import com.sangupta.resumemaker.github.GitHubAnalyzer;

public class DummyTestClass {
	
	public static void main(String[] args) {
		GitHubAnalyzer analyzer = new GitHubAnalyzer();
		analyzer.createDatabase();
		
		analyzer.shutDownDatabase();
	}

}
