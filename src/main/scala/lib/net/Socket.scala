package com.github.fellowship_of_the_bus.lib.net

import java.net.{Socket => JClientSocket, ServerSocket => JServerSocket, InetAddress,
  SocketAddress, InetSocketAddress, NetworkInterface, SocketException, URL }
import java.nio.ByteBuffer
import java.nio.channels.{DatagramChannel => JUDPSocket}
import java.io.{PrintStream, BufferedReader, InputStreamReader }
import java.util.Scanner

object IP {
  private val lookupAddr = "http://checkip.amazonaws.com/"

  /** Produces a list of local IP addresses. Speciallically, produces all
    * of the InetAddresses that are not link local, which are associated with
    * NetworkInterfaces that are up, and not loopback */
  def allLocalIPs(): List[InetAddress] = {
    import scala.collection.Iterator
    import scala.collection.JavaConversions.enumerationAsScalaIterator

    val nics = NetworkInterface.getNetworkInterfaces.toList
    for {
      nic <- nics
      if (nic.isUp && ! nic.isLoopback)
      addr <- nic.getInetAddresses
      if (! addr.isLinkLocalAddress)
    } yield addr
  }

  /** produce a LAN address of this machine */
  def localIP(): InetAddress = {
    val addrs = allLocalIPs

    // I'm not sure how to choose if there is more than
    // one option at this point, so just produce the first item
    addrs.head
  }

  /** produce the public IP address of this machine */
  def publicIP(): InetAddress = {
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
  def receive(): Option[String] = if (in.hasNextLine) Some(in.nextLine) else None
  def receiveNow(): String = in.nextLine

  def connection(): SocketAddress = conn.getRemoteSocketAddress
}

object UDPSocket {
  private val defaultBuffLen = 256

  def apply(port: Int) = new UDPSocket(port, defaultBuffLen)
  def apply(port: Int, buffLen: Int) = new UDPSocket(port, buffLen)
}

class UDPSocket(port: Int, buffLen: Int) extends java.io.Closeable {
  private val conn = {
    val channel = JUDPSocket.open()
    channel.socket().bind(new InetSocketAddress(port))
    channel.configureBlocking(false)
    channel
  }

  def send(s: String)(implicit addr: SocketAddress): Int = {
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

  def connect(addr: SocketAddress): ConnectedSocket = new ConnectedSocket(addr, this)

  // def receiveNow() = {
  //   // conn.receive(packet)
  //   // val msg = new String(buffer, packet.getOffset, packet.getLength)
  //   // val ipAddr = packet.getAddress()
  //   // val port = packet.getPort()
  //   // Some((msg, ipAddr, port))
  // }

  // def getIP() = conn.getInetAddress()

  def close() = conn.close()
}

/** UDP + connection. Create using UDPSocket.connect */
class ConnectedSocket protected[net](addr: SocketAddress, protected val conn: UDPSocket)
extends Socket {
  def send(s: String): Unit = conn.send(s)(addr)
  def receiveNow(): String = {
    // TODO: should loop until a valid packet is received
    receive() match {
      case Some(msg) => msg
      case None => ""
    }
  }

  def receive(): Option[String] = for {
    (msg, sender) <- conn.receive()
    if (sender == addr)
  } yield msg
}

