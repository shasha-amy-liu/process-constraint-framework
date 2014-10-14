package models.pcf.pcl

class PCLContext ( val elements:List[PCLProcessElement] ) extends PCLModel {

//  override def toString = "context [" + super.toString + ", " + elements + "]"
  override def toString = "[" + elements + "]"
}