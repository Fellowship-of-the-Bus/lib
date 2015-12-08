package com.github.fellowship_of_the_bus.lib.net

import java.net.{Socket => JClientSocket, ServerSocket => JServerSocket, InetAddress,
  SocketAddress, InetSocketAddress }
import java.nio.ByteBuffer
import java.nio.channels.{DatagramChannel => JUDPSocket}
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

  private val conn = {
    var channel = JUDPSocket.open()
    channel.socket().bind(new InetSocketAddress(port))
    channel.configureBlocking(false)
    channel
  }

  def send(s: String)(implicit addr: SocketAddress) = {
    val bytes = s.getBytes
    val buffer = ByteBuffer.wrap(bytes)
    conn.send(buffer, addr) // this?
  }

  // needs to be non-blocking, fix this
  def receive(): Option[(String, SocketAddress)] = {
    val buffer = ByteBuffer.allocate(buffLen)
    val sender = conn.receive(buffer)
    if (sender == null) None
    else {    
      val msg = new String(buffer.array, buffer.arrayOffset, buffer.position-buffer.arrayOffset)
      Some((msg, sender))
    }
  }

  // def receiveNow() = {
  //   // conn.receive(packet)
  //   // val msg = new String(buffer, packet.getOffset, packet.getLength)
  //   // val ipAddr = packet.getAddress()
  //   // val port = packet.getPort()
  //   // Some((msg, ipAddr, port))    
  // }

  // def getIP() = conn.getInetAddress()
}

// // UDP + connection
// case class ConnectedSocket extends Socket {
//   def send(s: String) = {
    
//   }

//   def receive() = {
//     val packet = new DatagramPacket()
//   }
// }

