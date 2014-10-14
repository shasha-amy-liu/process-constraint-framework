package pcf.utils

import java.io.File

/**
 * 	All the file operations are based on the {project.dir}/data folder
 */
object FileUtil {
  /*
  val logger = Logger.logger;

  def baseDir: File = {
    val path = play.Play.application().path().getAbsolutePath() + File.separator + "data"
    logger.info("path = " + path)
    val base = new File(path)
    if (!base.exists()) {
      base.mkdirs();
    }
    base
  }

  def getDir(relativePath: String): File = {
    getDir(relativePath, null)
  }

  def getDir(relativePath: String, parent: File): File = {
    var dir: File = null;
    if (parent == null) {
      val path = baseDir.getAbsolutePath() + File.separator + relativePath
      dir = new File(path)
    } else {
      val path = parent.getAbsolutePath() + File.separator + relativePath
      dir = new File(path)
    }

    if (!dir.exists()) {
      dir.mkdirs();
    }
    dir
  }

  def getFile(fileName: String, dir: File): File = {
    if (!dir.exists()) {
      dir.mkdirs();
    }
    new File(dir.getAbsolutePath() + File.separator + fileName)
  }
 */
}