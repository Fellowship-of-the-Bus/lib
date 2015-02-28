package com.github.fellowship_of_the_bus.lib.net

import java.net.{Socket => JClientSocket, DatagramSocket => JUDPSocket, ServerSocket => JServerSocket, InetAddress,
  DatagramPacket }
import java.io.PrintStream
import java.util.Scanner

trait Socket extends java.io.Closeable {
  protected def conn: java.io.Closeable
  def send(s: String): Unit
  def receive(): Option[String]
  def receiveNow(): String
  def close(): Unit = conn.close()
}

case class ServerSocket(port: Int) {
  private val conn = new JServerSocket(port)
  def accept(): ClientSocket = ClientSocket(conn.accept())
  def close(): Unit = conn.close()
}

case class ClientSocket(conn: JClientSocket) extends Socket {
  def this(addr: String, port: Int) = this(new JClientSocket(addr, port))

  private lazy val out = new PrintStream(conn.getOutputStream)
  private lazy val in = new Scanner(conn.getInputStream)

  def send(s: String): Unit = out.println(s)
  def receive() = if (in.hasNextLine) Some(in.nextLine) else None
  def receiveNow() = in.nextLine

  def connection() = conn.getRemoteSocketAddress //(conn.getAddress, conn.getPort)
}

class UDPSocket(port: Int, buffLen: Int)  {
  def this(port: Int) = this(port, 256)

  private val conn = new JUDPSocket(port)

  private lazy val buffer = new Array[Byte](buffLen)
  private lazy val packet = new DatagramPacket(buffer, buffer.length)

  def send(s: String)(implicit addr: (InetAddress, Int)) = {
    val bytes = s.getBytes
    val (ipAddr, port) = addr
    val sendPacket = new DatagramPacket(bytes, bytes.length, ipAddr, port)
    conn.send(sendPacket)
  }

  // needs to be non-blocking, fix this
  def receive(): Option[(String, InetAddress, Int)] = {
    conn.receive(packet)
    val msg = new String(buffer, packet.getOffset, packet.getLength)
    val ipAddr = packet.getAddress()
    val port = packet.getPort()
    Some((msg, ipAddr, port))
  }

  def receiveNow() = {
    conn.receive(packet)
    val msg = new String(buffer, packet.getOffset, packet.getLength)
    val ipAddr = packet.getAddress()
    val port = packet.getPort()
    Some((msg, ipAddr, port))    
  }
}

// // UDP + connection
// case class ConnectedSocket extends Socket {
//   def send(s: String) = {
    
//   }

//   def receive() = {
//     val packet = new DatagramPacket()
//   }
// }

