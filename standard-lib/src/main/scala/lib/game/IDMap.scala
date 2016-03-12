package com.github.fellowship_of_the_bus
package lib
package game

import lib.util.{rand,openFileAsStream}

import rapture.json._
import rapture.json.jsonBackends.jackson._

trait IDFactory[IDKind] {
  def fromString(name: String): IDKind = nameMap(name)

  def ids: Vector[IDKind]
  def random(): IDKind = ids(rand(ids.length))

  private lazy val nameMap: Map[String, IDKind] = ids.map(x => (x.toString, x)).toMap
}

class IDMap[IDKind, ValueKind](fileName: String) (implicit extractor: Extractor[ValueKind, Json], factory: IDFactory[IDKind]) {
  def ids: Vector[IDKind] = factory.ids
  val idmap: Map[IDKind, ValueKind] = readMap()

  def random(): IDKind = factory.random()
  def randomValue(): ValueKind = idmap(random())
  def apply(id: IDKind): ValueKind = idmap(id)

  private def readMap(): Map[IDKind, ValueKind] = {
    val json = Json.parse(scala.io.Source.fromInputStream(openFileAsStream(fileName)).mkString)
    json.as[Map[String,ValueKind]].map({ case (k, v) => (factory.fromString(k), v) })
  }

  override def toString(): String = (ids, idmap).toString
}
