package pcf.pcl

import java.util.StringTokenizer
import scala.util.parsing.combinator.JavaTokenParsers
import models.pcf.pcl._
import scala.collection.mutable.ListBuffer
import org.slf4j.LoggerFactory

class PCLParser extends JavaTokenParsers {
  val logger = LoggerFactory.getLogger(getClass());

  def pclParse(input: String): PCLParseResult = {
    val parseResult = parseAll(pcl, input)

    parseResult match {
      case Success(x, _) => new PCLParseResult(Some(parseResult.get), None)
      case NoSuccess(err, next) => {
        val error = "failed to parse PCL input " + "(line " + next.pos.line + ", column " + next.pos.column + "):\n" +
                    err + "\n" +
                    next.pos.longString + "\n"
        logger.debug(error)
        new PCLParseResult(None, Some(error)) 
      }
    }
  }

  def pcl: Parser[PCLConstraint] = PCLConstraint.Keyword_constraint ~> ident ~ context ~ conditions ~ opt(exception_handler_clause) ^^ {
    case constraintName ~ context ~ conditions ~ exceptionHandler => {
      val result = new PCLConstraint(constraintName, context, conditions, exceptionHandler)
      result
    }
  }

  def exception_handler_clause: Parser[PCLExceptionHandler] = PCLConstraint.Keyword_exception ~ PCLConstraint.Keyword_when ~ ident ~ PCLConstraint.Keyword_then ~ ident ^^ {
    case keywordException ~ keywordWhen ~ exception ~ keywordThen ~ handler => {
      val pclException = new PCLException(exception)
      val result = new PCLExceptionHandler(pclException, handler)
      result
    }
  }

  def context: Parser[PCLContext] = PCLConstraint.Keyword_context ~> repsep(process_element, ",") ^^ {
    case list => {
      var buffer = new ListBuffer[PCLProcessElement]()
      list.foreach(e => buffer += e)
      new PCLContext(buffer.toList)
    }
  }

  // Assume all processElements defined in context are activity elements
  def process_element: Parser[PCLProcessElement] = opt(ident <~ ":") ~ ident ^^ {
    case alias ~ name => alias match {
      case Some(alias) => new PCLProcessElement(alias, name)
      case None => new PCLProcessElement(name)
    }
  }

  def conditionType: Parser[String] = PCLConstraint.Keyword_condition_Pre |
                                      PCLConstraint.Keyword_condition_Post |
                                      PCLConstraint.Keyword_condition_Inv |
                                      failure("unsupported condition type")

  def conditions: Parser[List[PCLCondition]] = rep(condition)

  def condition: Parser[PCLCondition] = conditionType ~ (expression | if_expression) ~ opt(exception_clause) ^^ {
    case conditionType ~ expr ~ exceptions => {
      logger.debug("-----> condition:" + conditionType + "\n exceptions " + exceptions)
      val result = PCLCondition(conditionType, expr, exceptions)
      result
    }
  }

  def exception_clause: Parser[List[PCLException]] = PCLConstraint.Keyword_raise ~> exceptions ^^ {
    case list => {
      val result = new ListBuffer[PCLException]()
      list.foreach(exceptionType => {
        result += new PCLException(exceptionType)
      })
      result.toList
  }}

  /**
   * rep1sep: at lease match one exception type
   */
  def exceptions: Parser[List[String]] = rep1sep(ident, ",") ^^ {
    case list => {
      list
    }
  }

  def expression: Parser[PCLExpression] = logical_expression

  def logical_operator: Parser[String] = "and"|"or"|"xor" |
                                         failure("unsupported logical operator")

  def logical_expression: Parser[PCLExpression] = relational_expression ~ rep(logical_operator ~ relational_expression) ^^ {
    case left ~ list => {
      if (list.size == 0) {
        // Only one relational_expression
        left
      } else {
        var exp: BinaryExpression = null
        list.foreach(opExpr => {
          val op = opExpr._1
          val right = opExpr._2
          if (exp == null) {
            exp = new BinaryExpression(op, left, right)
          } else {
            val nextExp = new BinaryExpression(op, exp, right)
            exp = nextExp
          }
        })
        exp
      }
    }
  }

  def relational_expression: Parser[PCLExpression] = additive_expression ~ rep(relational_operator ~ additive_expression) ^^ {
    case left ~ list => {
      if (list.size == 0) {
        left
      } else {
        var exp: BinaryExpression = null
        list.foreach(opExp => {
          val op = opExp._1
          logger.debug("relational_operator = " + op)
          val right = opExp._2
          logger.debug("right = " + right)
          if (exp == null) {
            exp = new BinaryExpression(op, left, right)
          } else {
            val nextExp = new BinaryExpression(op, exp, right)
            exp = nextExp
          }
        })
        exp
      }
    }
  }

  def additive_expression: Parser[PCLExpression] = multiplicative_expression ~ rep(add_operator ~ multiplicative_expression) ^^ {
    case left ~ list => {
      if (list.size == 0) {
        left
      } else {
        var exp: BinaryExpression = null
        list.foreach(opExpr => {
          val op = opExpr._1
          logger.debug("add_operator = " + op)
          val right = opExpr._2
          logger.debug("right = " + right)
          if (exp == null) {
            exp = new BinaryExpression(op, left, right)
          } else {
            val nextExp = new BinaryExpression(op, exp, right)
            exp = nextExp
          }
        })
        exp
      }
    }
  }

  def multiplicative_expression: Parser[PCLExpression] = unary_expression ~ rep(multiply_operator ~ unary_expression) ^^ {
    case unary ~ list => {
      if (list.size == 0) {
        unary
      } else {
        var exp: BinaryExpression = null
        list.foreach(opExpr => {
          val op = opExpr._1
          logger.debug("multiply_operator = " + op)
          val right = opExpr._2
          logger.debug("right = " + right)
          if (exp == null) {
            exp = new BinaryExpression(op, unary, right)
          } else {
            val nextExp = new BinaryExpression(op, exp, right)
            exp = nextExp
          }
        })
        exp
      }
    }
  }

  def unary_operator: Parser[String] = "not"|
                                       failure("unsupported unary operator")

  def multiply_operator: Parser[String] = "*"|"/"|
                                          failure("unsupported multiply operator")

  def add_operator: Parser[String] = "+"|"-"|
                                     failure("unsupported add operator")  
  
  def relational_operator: Parser[String] = "<>"|"=="|"<="|">="|">"|"<"|
                                            failure("unsupported relational operator")

  def unary_expression: Parser[PCLExpression] = opt(unary_operator) ~ primary_expression ^^ {
    case op ~ primary_expression => {
      op match {
        case Some(unaryOp) => {
          logger.debug("unary op = " + unaryOp)
          val result = new UnaryExpression(unaryOp, primary_expression)
          result
        }
        case None => {
          primary_expression
        }
      }
    }
  }

  def if_expression: Parser[PCLExpression] = ("if" ~> expression) ~ ("then" ~> expression) ~ opt("else" ~> expression) <~ "endif" ^^ {
    case cond ~ default ~ alt => new IfExpression(cond, default, alt)
  }

  def primary_expression: Parser[PCLExpression] = if_expression | floatingPointNumberParser | attributeParser | operationParser | 
                                                  literalParser | "(" ~> expression <~ ")"|
                                                  failure("unsupported primary expression")

  def operationParser: Parser[PCLExpression] = ident ~ ("(" ~> repsep(primary_expression, ",") <~ ")") ^^ {
    case operation ~ list  => {
      logger.debug("operation = " + operation)
      val constraintOperation = new PCLConstraintOperation(operation, list)
      new Func(constraintOperation)
    }
  }

  /**
   * constraint attribute is in the form of "a->b.c->d.location" with navigation operators (".", "->")
   * Refer match multiple occurrence of dot http://stackoverflow.com/questions/4739759/how-to-match-repeated-patterns
   */
  def attributeParser: Parser[PCLExpression] = """\w+((\.|->)\w+)+""".r ^^ {
    case literal => {
      literal match {
        case s: String => {
          logger.debug("attribut literal = " + s)
          /*
           * Refer split a string, but also keep all the delimiters
           * (http://stackoverflow.com/questions/2206378/how-to-split-a-string-but-also-keep-the-delimiters)
           * a->b.c->d.location produces ("a" "->" "b" "." "c" "->" "d" "." "location")
           */
          val WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))"
          val words = s.split(String.format(WITH_DELIMITER, "\\.|(->)"))
          val path = words.toList
          // path.foreach(println)
          new Var(new PCLConstraintComplexAttribute(path))
        }
      }
    }
  }

  def literalParser: Parser[PCLExpression] = (ident | stringLiteral) ^^ {
    case literal => {
      literal.toLowerCase() match {
        case "true" => new Constant(PCLConstraintConstantAttribute[Boolean](true))
        case "false" => new Constant(PCLConstraintConstantAttribute[Boolean](false))
        case _ => new Constant(PCLConstraintConstantAttribute[String](literal))
      }
    }
  }

  def floatingPointNumberParser: Parser[PCLExpression] = floatingPointNumber ^^ {
    case number => {
      new Constant(PCLConstraintConstantAttribute[Float](number))
    }
  }
}