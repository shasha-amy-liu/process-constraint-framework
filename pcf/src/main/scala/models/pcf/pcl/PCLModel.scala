package models.pcf.pcl

import java.util.UUID

trait PCLModel {
  
  val id:String = UUID.randomUUID().toString();
//  override def toString = "id = " + id
}