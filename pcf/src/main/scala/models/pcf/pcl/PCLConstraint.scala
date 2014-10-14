package models.pcf.pcl

class PCLConstraint(
  val name:String,
  val context:PCLContext,
  val conditions:List[PCLCondition],
  val exceptionHandler:Option[PCLExceptionHandler]) extends PCLModel {

  override def toString = PCLConstraint.Keyword_constraint + ":\t[name = " + name + "]" + "\n" + 
                          PCLConstraint.Keyword_context + ":\t" + context + "\n" + 
                          "condition: \t[" + conditions + "]\n" +
                          "exceptionHandler:\t[" + exceptionHandler + "]"
}

object PCLConstraint {
  val Keyword_constraint = "constraint"
  val Keyword_context = "context"
  val Keyword_condition_Pre = "pre"
  val Keyword_condition_Post = "post"
  val Keyword_condition_Inv = "inv"
  val Keyword_raise = "raise"
  val Keyword_exception = "exception"
  val Keyword_when = "when"
  val Keyword_then = "then"
}