package models.pcf.pcl

sealed abstract class PCLConstraintAttribute extends PCLModel

case class PCLConstraintComplexAttribute( val path:List[String] ) extends PCLConstraintAttribute
case class PCLConstraintConstantAttribute[T] ( val value:Any ) extends PCLConstraintAttribute