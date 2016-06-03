package com.github.fellowship_of_the_bus
package lib
package game

import lib.util.{rand,openFileAsStream}

import rapture.json._
import rapture.json.jsonBackends.jackson._

import scala.io.Source

trait IDFactory[IDKind] {
  type Parser = PartialFunction[String, IDKind]
  lazy val fromString: Parser = ids.map(x => (x.toString, x)).toMap
  /** make new ID from string */
  protected var functions = Vector[Parser]()
  def addParser(pf: Parser): Unit = functions = functions :+ pf
  /** apply first defined partial function in supplied order to get an IDKind */
  def apply(name: String): IDKind =
    if (fromString.isDefinedAt(name)) fromString(name)
    else functions.find(pf => pf.isDefinedAt(name)).map(pf => pf(name)).get
  def ids: Vector[IDKind]
  def random(): IDKind = ids(rand(ids.length))
}

class IDMap[IDKind, ValueKind](file: Source, fileName: String)(implicit extractor: Extractor[ValueKind, Json], factory: IDFactory[IDKind]) {
  def this(in: java.io.InputStream, name: String = "")(implicit extractor: Extractor[ValueKind, Json], factory: IDFactory[IDKind]) =
    this(Source.fromInputStream(in),  name)
  def this(fileName: String)(implicit extractor: Extractor[ValueKind, Json], factory: IDFactory[IDKind]) =
    this(openFileAsStream(fileName), fileName)

  def ids: Vector[IDKind] = factory.ids
  val idmap: Map[IDKind, ValueKind] = readMap()

  def random(): IDKind = factory.random()
  def randomValue(): ValueKind = idmap(random())
  def apply(id: IDKind): ValueKind = idmap(id)

  private def readMap(): Map[IDKind, ValueKind] = {
    val json = Json.parse(file.mkString)
    json.as[Map[String,ValueKind]].map({
      case (k, v) =>
        try {
          // turn pair of (string, Value) into pair of (ID, Value)
          (factory(k), v)
        } catch {
          case e: Exception => throw new Exception(s"Key $k in ${fileName} not found in IDs: $ids", e)
        }
    })
  }

  override def toString(): String = (ids, idmap).toString
}
