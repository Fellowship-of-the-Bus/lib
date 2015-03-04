package com.github.fellowship_of_the_bus.lib.util

import java.io.{File,FileNotFoundException,InputStream,OutputStream,FileOutputStream,FileInputStream,IOException}
import java.util.jar.JarFile

/**
 * Adaptation of cz.adamh.utils.NativeUtils by Adam Heinrich
 * @see http://adamheinrich.com/blog/2012/how-to-load-native-jni-library-from-jar/
 */
object Native {
  val (os, separator) = System.getProperty("os.name").split(" ")(0).toLowerCase match {
    case "linux" => "linux" -> ":"
    case "mac" => "macosx" -> ":"
    case "windows" => "windows" -> ";"
    case "sunos" => "solaris" -> ":"
    case x => x -> ":"
  }

  /** Returns the system dependent install location. */
  def findInstallDir() = {
    val location = os match {
      case "linux" | "macosx" => 
        System.getProperty("user.home") + "/bin"
      case "windows" =>
        System.getProperty("/Program Files")
      case _ =>
        println("Unsupported platform: ${os.name}")
    }

    new File(location + "/fellowship-of-the-bus")
  }

  /** Installs all files from jar/natives into natdir */
  def installNatives(jar: JarFile, natdir: File) = {
    val entries = jar.entries
    while (entries.hasMoreElements) {
      val entry = entries.nextElement

      //entry is a native library
      if (entry.getName.startsWith("natives/" + os)) {
        // entry is not a directory
        if (! entry.getName.endsWith("/")) {
          // get the file name, copy it into the natives install directory
          val parts = entry.getName.split("/")
          val dst = new File(natdir + File.separator + parts(parts.length-1))

          val in = jar.getInputStream(entry)
          val out = new FileOutputStream(dst)

          copy(in, out)
        }
      }
    }
  }

  /** Loads native libraries from the enclosing jar file, if there is one.
    * Needs to copy the native libraries to a location outside of the Jar,
    * which is then added to the java library path. */
  def loadLibraryFromJar() = {
    val dir = findInstallDir
    val natdir = new File(dir, "natives")

    // only install files if natives directory does not exist
    // if things get more complicated, may need to check if specific files exist
    // include version.txt to indentify how old the sources are?
    if (! natdir.exists) {
      natdir.mkdirs()
      // load files from jar into dir/natives
      val jar = new JarFile(getClass.getProtectionDomain.getCodeSource.getLocation.getFile)

      installNatives(jar, natdir)
    }

    // java.library.path is cached by the JVM, 
    // need to reset it to null to get it to reload
    val path = System.getProperty("java.library.path")
    System.setProperty("java.library.path", path + separator + natdir.getAbsolutePath)
    val fieldSysPath = classOf[ClassLoader].getDeclaredField("sys_paths")
    fieldSysPath.setAccessible(true);
    fieldSysPath.set(null, null);
  }

  /** Copies all of the data from src to dst */
  def copy(src: InputStream, dst: OutputStream) = {
    // Prepare buffer for data copying
    val buffer = new Array[Byte](1024)

    try {
      def loop(readBytes: => Int): Unit = {
        val read = readBytes
        if (read != -1) {
          dst.write(buffer, 0, read)
          loop(readBytes)
        } else ()
      }

      loop(src.read(buffer))
    } finally {
      // If read/write fails, close streams safely before throwing an exception
      src.close();
      dst.close();
    }
  }
}
