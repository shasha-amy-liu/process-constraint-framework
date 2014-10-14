package models.pcf.pcl

class PCLException ( val exceptionType:String ) extends PCLModel {

  override def toString = "[" + exceptionType + "]"
}