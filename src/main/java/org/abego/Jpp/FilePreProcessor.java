package org.abego.Jpp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;

public class FilePreProcessor {

	@SuppressWarnings("unchecked")
	public FilePreProcessor(@SuppressWarnings("rawtypes") Map properties) {
		this.providedProperties = new Hashtable((Map)properties);
	}

	public void preProcessFile(File srcFile, File destFile) throws IOException {
		ensureDirExists(destFile.getParentFile());

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(srcFile));
			writer = new BufferedWriter(new FileWriter(destFile));

			Parser parser = new Parser(reader, writer);
			parser.parse(true, false);

		} catch (Exception ex) {
			throw new RuntimeException(String.format(
					"Error when processing file '%s': %s",
					srcFile.getAbsolutePath(), ex.getMessage()), ex);

		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	// Parser ============================================================
		
	private static final String directivePrefixRE = "\\s*(?:(?:/\\*)|(?://))?#";
	private static final String directiveSuffixRE = "\\s*(?:\\*/)?\\s*";
	private static final String idRE = "[a-zA-Z_][a-zA-Z0-9_]*";
	private static final String conditionRE = "(\\!)?(" + idRE + ")";

	private static Pattern createDirectivePattern(String directiveRE) {
		return Pattern.compile(directivePrefixRE + directiveRE
				+ directiveSuffixRE);
	}

	private static final Pattern re_if = createDirectivePattern("if\\s*(?:\\()?"
			+ conditionRE + "(?:\\))?");
	private static final Pattern re_else = createDirectivePattern("else");
	private static final Pattern re_endif = createDirectivePattern("endif");
	
	private static final String newline = System.getProperty("line.separator");
	
	private class Parser {

		private BufferedReader reader;
		private BufferedWriter writer;
		private String line;
		private int lineNo = 0;

		private Parser(BufferedReader reader, BufferedWriter writer) {
			this.reader = reader;
			this.writer = writer;
		}

		private String readLine() throws IOException {
			line = reader.readLine();
			if (line != null) {
				lineNo++;
			}
			return line;
		}

		private Condition getIfCondition() {
			if (line == null) {
				return null;
			}
			Matcher m = re_if.matcher(line);
			return m.matches() ? new Condition(m.group(1) != null, m.group(2))
					: null;
		}

		private boolean isElseLine() {
			return line != null && re_else.matcher(line).matches();
		}

		private boolean isEndifLine() {
			return line != null && re_endif.matcher(line).matches();
		}

		public void parse(boolean copyLines, boolean insideIf)
				throws IOException {
			readLine();
			while (line != null) {
				Condition ifCondition = getIfCondition();
				if (ifCondition != null) {
					boolean isTrue = ifCondition.getValue();

					// process ifPart
					int ifLineNo = lineNo;
					parse(copyLines && isTrue, true);
					boolean hasElsePart = isElseLine();
					if (hasElsePart) {
						int elseLineNo = lineNo;
						parse(copyLines && !isTrue, true);

						if (!isEndifLine()) {
							throw new RuntimeException(String.format(
									"Missing '#endif' for '#else' in line %d",
									elseLineNo));
						}
					} else {
						if (!isEndifLine()) {
							throw new RuntimeException(String.format(
									"Missing '#endif' for '#if' in line %d",
									ifLineNo));
						}
					}
				} else if (isElseLine() || isEndifLine()) {
					if (!insideIf) {
						throw new RuntimeException(String.format(
								"Missing '#if' for statement in line %d",
								lineNo));
					}
					return;
				} else if (copyLines) {
					writer.write(line);
					writer.write(newline);
				}
				readLine();
			}
		}
	}

	private class Condition {
		private final boolean negate;
		private final String id;

		public Condition(boolean negate, String id) {
			this.id = id.trim();
			this.negate = negate;
		}

		public boolean getValue() {
			Object value = getPropertyValue(id);
			boolean isTrue = getBooleanValue(value);
			return negate ? !isTrue : isTrue;
		}
	}

	// Properties ============================================================
	
	@SuppressWarnings("rawtypes")
	private Hashtable providedProperties;

	private Object getPropertyValue(String name) {
		return providedProperties.get(name);
	}
	
	// Util ============================================================
		
	private static void ensureDirExists(File dir) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new BuildException("Cannot create directory: "
						+ dir.getAbsolutePath());
			}
		}
	}

	private static boolean getBooleanValue(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		} else {
			return value.toString().equalsIgnoreCase("true");
		}
	}
}
