package com.github.fellowship_of_the_bus.lib.net

import java.net.{Socket => JClientSocket, ServerSocket => JServerSocket, InetAddress,
  SocketAddress, InetSocketAddress, NetworkInterface, SocketException, URL }
import java.nio.ByteBuffer
import java.nio.channels.{DatagramChannel => JUDPSocket}
import java.io.{PrintStream, BufferedReader, InputStreamReader }
import java.util.Scanner

object IP {
  private val lookupAddr = "http://checkip.amazonaws.com/"

  /** produce a LAN address of this machine */
  def localIP() = {
    import scala.collection.Iterator
    import scala.collection.JavaConversions.enumerationAsScalaIterator
    
    val nics: Iterator[NetworkInterface] = NetworkInterface.getNetworkInterfaces
    val addrs = for {
      nic <- nics.toList
      if (nic.isUp)
      if (! nic.isLoopback)
      addr <- nic.getInetAddresses
      if (addr.isSiteLocalAddress)
    } yield addr

    // I'm not sure how to choose if there is more than
    // one option at this point, so just produce the first item
    addrs.head
  }

  /** produce the public IP address of this machine */
  def publicIP() = {
    val url = new URL(lookupAddr)
    val in = new BufferedReader(new InputStreamReader(url.openStream))
    val ip = in.readLine
    InetAddress.getByName(ip)
  }
}

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

  def connection() = conn.getRemoteSocketAddress
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
    Option(sender).map(
      new String(buffer.array, buffer.arrayOffset, 
        buffer.position-buffer.arrayOffset) -> _)
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

