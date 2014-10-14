package pcf.pcl

import models.pcf.pcl._

class PCLParseResult (val constraint: Option[PCLConstraint], val message: Option[String]) {
  override def toString = constraint match {
    case Some(constraint) =>
      constraint.toString
    case None =>
      message.get
  }
}