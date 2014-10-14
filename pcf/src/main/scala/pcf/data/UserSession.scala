package pcf.data

case class UserSession(
  id: String,
  process: PcfProcess,
  proconto: PcfProcontO,
  pcl: PcfPcl)

object UserSession {

}