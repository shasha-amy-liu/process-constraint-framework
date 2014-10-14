package pcf.process.bpel

import pcf.pcl._
import org.slf4j.LoggerFactory
import java.io.File
import org.apache.ode.bpel.compiler.DefaultResourceFinder
import org.apache.ode.utils.SystemUtils
import org.apache.ode.bpel.compiler.BpelCompiler20
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import org.apache.ode.utils.StreamUtils
import java.io.FileInputStream
import org.apache.ode.bpel.compiler.bom.BpelObjectFactory
import org.apache.ode.bpel.compiler.BpelCompiler
import org.apache.ode.bpel.o.OProcess

/**
 * Bpel parser only support bpel 2.0 specification.
 */
class BpelParser(val path: String) {

  val logger = LoggerFactory.getLogger(getClass())

  def parse: Option[OProcess] = {
    var result: Option[OProcess] = None

    val bpelFile = new File(path)
    logger.debug("file exists? " + bpelFile.exists() +
                 "\tbpelFile path = " + bpelFile.getAbsolutePath())

    val parentDir = bpelFile.getParentFile();
    val wf = new DefaultResourceFinder(bpelFile.getAbsoluteFile().getParentFile(), parentDir.getAbsoluteFile());
    var processName: Option[String] = None

    try {
      val outputDir = new File(SystemUtils.userDirectory())
      val compiler = new BpelCompiler20()
      compiler.setResourceFinder(wf);
      val isrc = new InputSource(new ByteArrayInputStream(StreamUtils.read(new FileInputStream(bpelFile))))
      isrc.setSystemId(bpelFile.getAbsolutePath());

      val process = BpelObjectFactory.getInstance().parse(isrc, bpelFile.toURI())

      val oprocess = compiler.compile(process, wf, BpelCompiler.getVersion(outputDir.getAbsolutePath()));
      if (oprocess != null) {
        result = Some(oprocess)
      }
    } catch {
      case ex: Exception => logger.error("Failed to parse bpel process file in " + bpelFile.getAbsolutePath(), ex)
    }

    result
  }
}