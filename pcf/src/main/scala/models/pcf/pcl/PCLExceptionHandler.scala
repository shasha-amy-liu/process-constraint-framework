package models.pcf.pcl

class PCLExceptionHandler (
    val exception:PCLException,
    val handler:String) extends PCLModel {

  override def toString = "[" + exception + "->" + handler + "]"
}