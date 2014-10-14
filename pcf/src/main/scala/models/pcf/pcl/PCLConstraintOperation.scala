package models.pcf.pcl

class PCLConstraintOperation( val operation:String, val params:List[PCLExpression] ) extends PCLModel {
  override def toString = operation + "(" + params + ")"
}