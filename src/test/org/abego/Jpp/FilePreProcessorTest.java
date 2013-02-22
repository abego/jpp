package org.abego.Jpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

public class FilePreProcessorTest {
	private static Logger logger = Logger.getLogger("org.abego.Jpp");

	private String getFileContent(File file) throws FileNotFoundException {
		return new Scanner(file).useDelimiter("\\A").next();
	}

	@Test
	public void test_ifElseEndif_Else() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("ifElseEndif.txt", "bla\nbar\nbla\n", properties);
	}

	@Test
	public void test_ifElseEndif_Else_withProperty() throws URISyntaxException,
			IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "false");

		helper("ifElseEndif.txt", "bla\nbar\nbla\n", properties);
	}

	@Test
	public void test_ifElseEndif_If() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifElseEndif.txt", "bla\nfoo\nbla\n", properties);
	}

	@Test
	public void test_ifNegateElseEndif_Else() throws URISyntaxException,
			IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("ifNegateElseEndif.txt", "bla\nfoo\nbla\n", properties);
	}

	@Test
	public void test_ifNegateElseEndif_Else_withProperty()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "false");

		helper("ifNegateElseEndif.txt", "bla\nfoo\nbla\n", properties);
	}

	@Test
	public void test_ifNegateElseEndif_If() throws URISyntaxException,
			IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifNegateElseEndif.txt", "bla\nbar\nbla\n", properties);
	}

	@Test
	public void test_ifNegateElseEndif_singleLineComments_Else()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("ifNegateElseEndif_singleLineComments.txt", "bla\nfoo\nbla\n",
				properties);
	}

	@Test
	public void test_ifNegateElseEndif_singleLineComments_Else_withProperty()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "false");

		helper("ifNegateElseEndif_singleLineComments.txt", "bla\nfoo\nbla\n",
				properties);
	}

	@Test
	public void test_ifNegateElseEndif_singleLineComments_If()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifNegateElseEndif_singleLineComments.txt", "bla\nbar\nbla\n",
				properties);
	}

	@Test
	public void test_ifEndif_Else() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("ifEndif.txt", "bla\nbla\n", properties);
	}

	@Test
	public void test_ifEndif_Else_withProperty() throws URISyntaxException,
			IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "false");

		helper("ifEndif.txt", "bla\nbla\n", properties);
	}

	@Test
	public void test_ifEndif_If_withProperty() throws URISyntaxException,
			IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifEndif.txt", "bla\nfoo\nbla\n", properties);
	}

	@Test
	public void test_ifEndif_singleLineComments_Else()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("ifEndif_singleLineComments.txt", "bla\nbla\n", properties);
	}

	@Test
	public void test_ifEndif_singleLineComments_Else_withProperty()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "false");

		helper("ifEndif_singleLineComments.txt", "bla\nbla\n", properties);
	}

	@Test
	public void test_ifEndif_singleLineComments_If_withProperty()
			throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifEndif_singleLineComments.txt", "bla\nfoo\nbla\n", properties);
	}

	@Test
	public void test_ifElseEndif_noParentheses_Else() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("ifElseEndif-noParentheses.txt", "bla\nbar\nbla\n", properties);
	}

	@Test
	public void test_ifElseEndif_noParentheses_Else_withProperty() throws URISyntaxException,
			IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "false");

		helper("ifElseEndif-noParentheses.txt", "bla\nbar\nbla\n", properties);
	}

	@Test
	public void test_ifElseEndif_noParentheses_If() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifElseEndif-noParentheses.txt", "bla\nfoo\nbla\n", properties);
	}
	
	@Test
	public void test_nestedIfNegateEndif() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("nestedIfNegateEndif.txt", "bla\nnot foo\nnot bar\nbla\n", properties);
		
		properties.put("BAR", "true");
		helper("nestedIfNegateEndif.txt", "bla\nnot foo\nbla\n", properties);
		
		properties.put("FOO", "true");
		helper("nestedIfNegateEndif.txt", "bla\nbla\n", properties);

		properties.put("BAR", "false");
		helper("nestedIfNegateEndif.txt", "bla\nbla\n", properties);
	}
	
	@Test
	public void test_nestedIfElseEndif() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();

		helper("nestedIfElseEndif.txt", "bla\n!foo\n!foo !bar\n!foo end\nbla\n", properties);
		
		properties.put("BAR", "true");
		helper("nestedIfElseEndif.txt", "bla\n!foo\n!foo bar\n!foo end\nbla\n", properties);
		
		properties.put("FOO", "true");
		helper("nestedIfElseEndif.txt", "bla\nfoo\nfoo bar\nfoo end\nbla\n", properties);

		properties.put("BAR", "false");
		helper("nestedIfElseEndif.txt", "bla\nfoo\nfoo !bar\nfoo end\nbla\n", properties);
	}
	
	
	@Test
	public void test_ifElseEndif_WithComment_If() throws URISyntaxException, IOException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("FOO", "true");

		helper("ifElseEndif-withComment.txt", "bla\nfoo\nbla\n", properties);
	}


	private void helper(String srcFileName, String expectedResult,
			Hashtable<String, String> properties) throws URISyntaxException,
			IOException, FileNotFoundException {
		URL testdataURL = FilePreProcessorTest.class.getResource("testdata");
		File testdataDir = new File(new URI(testdataURL.toExternalForm()));
		File srcFile = new File(testdataDir, srcFileName);

		File destFile = File.createTempFile("file", "txt");
		FilePreProcessor pp = new FilePreProcessor(properties);
		pp.preProcessFile(srcFile, destFile);

		Assert.assertEquals(expectedResult, getFileContent(destFile));
	}
}
