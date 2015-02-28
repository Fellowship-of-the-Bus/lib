package com.github.fellowship_of_the_bus.lib.game

import java.io.{File, PrintStream}
import java.util.Scanner
import net.{ServerSocket}

// currently expects one line messages
// of the form "Name: XXX Score: YYY"
object Scoreboard extends App {
  val tableHeader =
"""
<head>
  <script src="/~rschlunt/js/sorttable.js"></script>
  <style>
  table.sortable th:not(.sorttable_sorted):not(.sorttable_sorted_reverse):not(.sorttable_nosort):after { 
    content: " \25B4\25BE" 
  }

  table.sortable thead {
    background-color:#eee;
    color:#666666;
    font-weight: bold;
    cursor: default;
  }
  table.sortable tbody {
    background-color:#000;
    color:#ffffff;
  }
</style>
</head>

<table class="sortable">
  <thead>
    <tr><th>Rank</th><th>Name</th><th>Score</th></tr>
  </thead>
  <tbody>
""".trim
  val tableFooter =
"""
  </tbody>
</table>
""".substring(1)

  val baseSocket = ServerSocket(12345)
  val dataFile = new File("scoreboard.txt")

  var data = List[(String,Int)]()
  val maxLength = 100

  def read() = {
    dataFile.createNewFile      // only creates if it doesn't exist
    val sc = new Scanner(dataFile)
    while (sc.hasNextLine) {
      val line = sc.nextLine.split(' ')
      data = (line(0), Integer.parseInt(line(1)))::data
    }
  }

  def write() = {
    val dataWriter = new PrintStream(dataFile)
    val tableWriter = new PrintStream(new File("scoreboard.html"))

    tableWriter.println(tableHeader)

    var i = 1
    for {
      x <- data
      (name, score) = x
    } {
      dataWriter.println(s"$name $score")
      tableWriter.println(s"    <tr><td>$i</td><td>$name</td><td>$score</td></tr>")
      i = i + 1
    }

    tableWriter.println(tableFooter)

    println("Finished writing files")
    dataWriter.close
    tableWriter.close
  }

  // 'main'
  read()
  while(true) {
    val socket = baseSocket.accept()
    println(s"connected to ${socket.connection}")
    val str = socket.receiveNow()
    println(s"received: $str")
    val msg = str.split(' ')    // blocking...
    msg.foreach(println(_))
    if (msg.length == 4) {
      println("Correct num args")
      if (msg(0) == "Name:" && msg(2) == "Score:") {
        try {
          val name = msg(1)
          val score = Integer.parseInt(msg(3))
          println(s"Parsed... $name $score")
          data = ((name,score)::data).sortWith(_._2 > _._2).take(maxLength)
          write()
        } catch {
          case nfe: NumberFormatException =>
            // send back message?
        }
      }
    }
  }
}
