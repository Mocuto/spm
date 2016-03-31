package com.moc.spm

import java.io.{File, PrintWriter};
import java.nio.file.{Files, Paths}

import scala.io.{Source, StdIn}

object SPM {

	val workingDirectory = System.getProperty("user.dir");

	val nl = sys.props("line.separator");

	val dbnl = nl + nl

    def main(args : Array[String]) {
        println("Scala Package Manager - version")

        walkthrough();

        buildDirectories("test", "1.0.0", "2.11.0", "/Users/Mocuto/testdir")
    }


    /**
    */
    def getConsoleInput(name : String, default : String = "") : String = {
    	print(s"$name: ($default) ");

    	var userInput = StdIn.readLine

    	return if (userInput.trim.length <= 0) default else userInput
    }

    /** Walks them through the init process 

    	TODO: Make the defaults configurable */
    def walkthrough() {
    	val name = getConsoleInput("name", workingDirectory.split("/").last)

    	val version = getConsoleInput("version", "1.0.0")

    	val scalaVersion = getConsoleInput("scalaVersion", "2.11.0")
    }

    def genREADME(projectName : String, version : String) : String = {
    	val lines = Source.fromURL(getClass.getResource("/README.md")).getLines.mkString(nl)

    	val readMe = s"#$projectName" + nl + s"*$version*" + nl + lines;

    	return readMe
    }

    def genGitIgnore : String = {
    	return Source.fromURL(getClass.getResource("/sample-gitignore.txt")).getLines.mkString(nl)
    }

    def genBuildSBT(projectName : String, version : String, scalaVersion : String) : String = {
    	val output = s"""name := "$projectName"""" + dbnl + s"""version := "$version"""" + dbnl + s"""scalaVersion := "$scalaVersion""""

    	return output
    }

    /** Writes contents to a file. Returns true if there were no errors */
    def writeFile(filename : String, contents : String) : Boolean = {
    	val pw = new PrintWriter(new File(filename))

    	pw.write(contents)

    	pw.close();
    	return !pw.checkError
    }

    /** TODO: accept as an argument a class describing the directory structure
		The default is based off of: http://www.scala-sbt.org/0.13/docs/Directories.html

		@rootStr - Absolute path to the project folder
	*/
    def buildDirectories(projectName : String, version : String, scalaVersion : String, rootStr : String = "") : Boolean = {
    	val root = if (rootStr.trim.length <= 0) Paths.get(workingDirectory) else Paths.get(rootStr)

    	//Create build.sbt
    	writeFile(Paths.get(rootStr, "build.sbt").toString, genBuildSBT(projectName, version, scalaVersion))

    	//Create README.md
    	writeFile(Paths.get(rootStr, "README.md").toString, genREADME(projectName, version))

    	//Create .gitignore
    	writeFile(Paths.get(rootStr, ".gitignore").toString, genGitIgnore)

    	//Create main and subfolders
    	Files.createDirectories(Paths.get(rootStr, "main", "scala"))

    	Files.createDirectory(Paths.get(rootStr, "main", "resources"))

    	Files.createDirectory(Paths.get(rootStr, "main", "java"))

    	//Create test and subfolders
    	Files.createDirectories(Paths.get(rootStr, "test", "scala"))

    	Files.createDirectory(Paths.get(rootStr, "test", "resources"))

    	Files.createDirectory(Paths.get(rootStr, "test", "java"))

    	return true;
    }
}