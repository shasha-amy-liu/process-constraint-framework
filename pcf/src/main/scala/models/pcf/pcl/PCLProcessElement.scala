package models.pcf.pcl

class PCLProcessElement(val alias:String, val name:String) extends PCLModel {

  def this(name:String) = this("", name)

  //  override def toString = "[" + elementType + " " + super.toString + ", alias = " + alias + ", name = " + name + "]"
  override def toString = if ( alias != "" ) "[alias = " + alias + ", name = " + name + "]" else "[name = " + name + "]" 
}
