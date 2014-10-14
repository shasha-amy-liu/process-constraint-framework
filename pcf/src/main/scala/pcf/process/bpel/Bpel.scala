package pcf.process.bpel

import org.apache.ode.bpel.o.OProcess
import com.google.common.collect.Maps
import scala.collection.JavaConversions._
import com.google.common.base.Joiner

object Bpel {

  def toString(process: Option[OProcess]) : String = {
    process match {
      case None => "invalid bpel file"
      case Some(proc) => {
        val map = Maps.newTreeMap[String, Object]()
        map.put("processName", proc.processName)
        map.put("messageTypes", proc.messageTypes)

        val partnerLinks = proc.getAllPartnerLinks()
        val plMap = Maps.newHashMap[String, Object]()
        partnerLinks.foreach(pl => {
          val partnerLink = Maps.newTreeMap[String, String]()
          partnerLink.put("name", pl.getName())
          partnerLink.put("type", pl.partnerLinkType.toString())
          partnerLink.put("rolename", pl.myRoleName)

          plMap.put("partnerLink", partnerLink)
        })
        map.put("partnerLinks", plMap)

        val result = Joiner.on("\n").withKeyValueSeparator(": ").join(map)
        result
      }
    }
  }
}