package com.moc.spm

import java.io.{ByteArrayInputStream, File}
import java.nio.file.{Files, Paths}
import org.scalatest._

import SPM.{dbnl, nl}

class SPMSpec extends FlatSpec with Matchers {
	"SPM" should "correctly get the console input" in {

		val expected = "doggy"

		val input = new ByteArrayInputStream(expected.getBytes)

		Console.withIn(input) {
			val actual = SPM.getConsoleInput("unit-test")

			actual should equal (expected)
		}

	}

	it should "generate the project directory structure correctly" in {
		val tmpDirectory = Files.createTempDirectory("tmp-spm-test-1")

		val root = tmpDirectory.toString

		//Delete the tmp directory when we're finished, for cleanliness
		(new File(root)).deleteOnExit();

		SPM.buildDirectories("test", "1.0.0", "2.11.0", root)

		//Check for build.sbt
		Files.exists(Paths.get(root, "build.sbt")) should be (true)

		//Check for README.md
		Files.exists(Paths.get(root, "README.md")) should be (true)

		//Check for .gitignore
		Files.exists(Paths.get(root, ".gitignore")) should be (true)

		//Check for main folder/subfolders
		Files.exists(Paths.get(root, "main")) should be (true)

		Files.exists(Paths.get(root, "main", "scala")) should be (true)

		Files.exists(Paths.get(root, "main", "resources")) should be (true)

		Files.exists(Paths.get(root, "main", "java")) should be (true)

		//Check for test folder/subfolders
		Files.exists(Paths.get(root, "test")) should be (true)

		Files.exists(Paths.get(root, "test", "scala")) should be (true)

		Files.exists(Paths.get(root, "test", "resources")) should be (true)

		Files.exists(Paths.get(root, "test", "java")) should be (true)
	}

	it should "generate the build.sbt correctly" in {
		val expected = """name := "test"""" + dbnl + """version := "1.0.0"""" + dbnl + """scalaVersion := "2.11.0""""

		val actual = SPM.genBuildSBT("test", "1.0.0", "2.11.0")

		actual should be (expected)
	}

}