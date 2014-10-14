package models.pcf.pcl

import scala.language.existentials

sealed trait PCLExpression extends PCLModel

case class Constant(val constant:PCLConstraintConstantAttribute[_]) extends PCLExpression
// Treat constraint attribute as variable
case class Var(val attribute:PCLConstraintComplexAttribute) extends PCLExpression
// Treat constraint operation as user-defined function
case class Func(val operation:PCLConstraintOperation) extends PCLExpression
case class UnaryExpression(operator:String, arg:PCLExpression) extends PCLExpression
case class BinaryExpression(operator:String, left:PCLExpression, right:PCLExpression) extends PCLExpression
case class IfExpression(condition:PCLExpression, default:PCLExpression, alternative: Option[PCLExpression]) extends PCLExpression