package pcf.pcl

import scala.collection.mutable.ListBuffer
import pcf.pcl._
import org.slf4j.LoggerFactory

import org.scalatest.FunSuite

class PCLParserTestSpec extends FunSuite {
  val logger = LoggerFactory.getLogger(getClass())

  test("test1") {
    val input = "constraint test1\n" +
      "context t1:Task1, t2:Task2\n" +
      "pre a->b.c->d.location < 60.0\n" +
      "    and distance(a.location, b.location) < 100" +
      "    or b < c"
    logger.info("********** test 1 **********\n" + input + "\n")
    logger.info("********** test 1 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "test1")
  }

  test("test2") {
    val input = "constraint if_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post if aa.size < bb.size\n" +
      "     then bb.size - aa.size" +
      "     else aa.size - bb.size endif"
    logger.info("********** test 2 **********\n" + input + "\n")
    logger.info("********** test 2 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "if_expressionTest")
  }

  test("test2 1") {
    val input = "constraint if_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post if aa.size < bb.size\n" +
      "     then bb.size - aa.size" +
      "     endif"
    logger.info("********** test 2.1 **********\n" + input + "\n")
    logger.info("********** test 2.1 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "if_expressionTest")
  }

  test("test3 unsupported keyword") {
    val input = "Constraint test\n" +
      "context t:Task, task2, task3\n" +
      "pre aa.size < bb.size\n"
    logger.info("********** test 3 **********\n" + input + "\n")
    logger.info("********** test 3 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test4 unsupported keyword") {
    val input = "constraint test\n" +
      "Context t:Task, task2, task3\n" +
      "pre aa.size < bb.size\n"
    logger.info("********** test 4 **********\n" + input + "\n")
    logger.info("********** test 4 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test5 unsupported keyword") {
    val input = "constraint test\n" +
      "context t:Task, task2, task3\n" +
      "Pre aa.size < bb.size\n"
    logger.info("********** test 5 **********\n" + input + "\n")
    logger.info("********** test 5 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test6 unsupported expression") {
    val input = "constraint test\n" +
      "context t:Task, task2, task3\n" +
      "pre aa.size <<<< bb.size\n"
    logger.info("********** test 6 **********\n" + input + "\n")
    logger.info("********** test 6 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test7") {
    val input = "constraint if_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post if aa.size < bb.size\n" +
      "     then bb.size -- aa.size" +
      "     else aa.size * bb.size endif"
    logger.info("********** test 7 **********\n" + input + "\n")
    logger.info("********** test 7 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test8") {
    val input = "constraint if_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post aa.size < bb.size and bb.size >= aa.size \n"
    logger.info("********** test 8 **********\n" + input + "\n")
    logger.info("********** test 8 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "if_expressionTest")
  }

  test("test9 exception parser") {
    val input = "constraint exception_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post aa.size < bb.size and bb.size >= aa.size \n" +
      "     raise HospitalNotFoundException"
    logger.info("********** test 9 **********\n" + input + "\n")
    logger.info("********** test 9 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "exception_expressionTest")
  }

  test("test9_1 raise clause with exception type") {
    val input = "constraint exception_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post aa.size < bb.size and bb.size >= aa.size \n" +
      "     raise"
    logger.info("********** test 9 **********\n" + input + "\n")
    logger.info("********** test 9 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test9_2 raise clause with multiple exception types") {
    val input = "constraint exception_expressionTest\n" +
      "context t:Task, task2, task3\n" +
      "post aa.size < bb.size and bb.size >= aa.size \n" +
      "     raise HospitalNotFoundException, DetailNotAvailableException"
    logger.info("********** test 9 **********\n" + input + "\n")
    logger.info("********** test 9 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "exception_expressionTest")
  }

  test("test10 exception handler clause") {
    val input = "constraint exception_handler_clauseTest\n" +
      "context t:Task, task2, task3\n" +
      "post aa.size < bb.size and bb.size >= aa.size \n" +
      "     raise HospitalNotFoundException, DetailNotAvailableException\n" +
      "exception "
    logger.info("********** test 10 **********\n" + input + "\n")
    logger.info("********** test 10 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint == None)
  }

  test("test10_1") {
    val input = "constraint exception_handler_clauseTest\n" +
      "context t:Task, task2, task3\n" +
      "post aa.size < bb.size and bb.size >= aa.size \n" +
      "     raise HospitalNotFoundException, DetailNotAvailableException\n" +
      "exception when HospitalNotFoundException\n" +
      "          then extendDistance"
    logger.info("********** test 10 **********\n" + input + "\n")
    logger.info("********** test 10 result **********")
    val parser = new PCLParser
    val constraint = parser.pclParse(input)
    logger.info(constraint.toString)
    assert(constraint.constraint.get.name == "exception_handler_clauseTest")
  }
}