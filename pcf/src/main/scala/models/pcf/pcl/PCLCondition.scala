package models.pcf.pcl

sealed abstract class PCLCondition extends PCLModel

case class PCLInvCondition(
    val conditionType: String,
    val expression: PCLExpression,
    val exceptions: Option[List[PCLException]]) extends PCLCondition

case class PCLPostCondition(
    val conditionType: String,
    val expression: PCLExpression,
    val exceptions: Option[List[PCLException]]) extends PCLCondition

case class PCLPreCondition(
    val conditionType: String,
    val expression: PCLExpression,
    val exceptions: Option[List[PCLException]]) extends PCLCondition

object PCLCondition {
  def apply(
      conditionType: String,
      expression: PCLExpression,
      exceptions: Option[List[PCLException]]): PCLCondition = {
    //FIXME: handle null pointer and empty string
    conditionType match {
      case PCLConstraint.Keyword_condition_Pre =>
        new PCLPreCondition(PCLConstraint.Keyword_condition_Pre, expression, exceptions)
      case PCLConstraint.Keyword_condition_Post =>
        new PCLPostCondition(PCLConstraint.Keyword_condition_Post, expression, exceptions)
      case PCLConstraint.Keyword_condition_Inv =>
        new PCLInvCondition(PCLConstraint.Keyword_condition_Inv, expression, exceptions)
    }
  }
}
