package org.abego.Jpp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @author ub
 */
public class JppTask extends Task {
	private static Logger logger = Logger.getLogger("org.abego.Jpp");

	// Task Attributes ------------------

	// - destDir
	private File destDir;

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}

	public File getDestDir() {
		return destDir;
	}

	// - force
	private boolean force;

	public boolean getForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	// - verbose
	private boolean verbose;

	public boolean getVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	// - readonly
	private boolean readonly;

	public boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	// - fileSet
	private List<FileSet> fileSets = new ArrayList<FileSet>();

	public void addFileSet(FileSet fileSet) {
		fileSets.add(fileSet);
	}

	// Task "execute" ------------------

	public void execute() throws BuildException {
		checkAttributes();

		FilePreProcessor preprocessor = new FilePreProcessor(getProject()
				.getProperties());
		for (FileSet fileSet : fileSets) {
			processFileSet(fileSet, preprocessor);
		}
	}

	private void processFileSet(FileSet fileSet, FilePreProcessor preprocessor) {
		DirectoryScanner ds = fileSet.getDirectoryScanner(getProject());
		for (String file : ds.getIncludedFiles()) {
			processFile(file, ds.getBasedir(), preprocessor);
		}
	}

	private void processFile(
			String file,
			File baseDir,
			FilePreProcessor preprocessor) {
		try {
			File srcFile = new File(baseDir, file);
			File destFile = new File(destDir, file);

			// No need to preprocess the srcFile when the destFile is already
			// newer than the destFile
			if (!getForce() && destFile.exists()
					&& destFile.lastModified() > srcFile.lastModified()) {
				return;
			}

			if (destFile.exists() && !destFile.canWrite()) {
				destFile.setWritable(true);
			}

			if (getVerbose()) {
				logger.info(String.format(
						"preprocessing %s (to %s)",
						srcFile.getAbsolutePath(),
						destFile.getAbsolutePath()));
			}

			preprocessor.preProcessFile(srcFile, destFile);

			if (getReadonly()) {
				destFile.setReadOnly();
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	private void checkAttributes() throws BuildException {
		if (destDir == null) {
			throw new BuildException("destDir required");
		}
		if (fileSets.isEmpty()) {
			throw new BuildException("FileSet(s) required");
		}
	}
}
