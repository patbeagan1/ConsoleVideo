package com.pbeagan.demo

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.commons.cli.PosixParser
import java.io.IOException
import kotlin.system.exitProcess

@Throws(InterruptedException::class, IOException::class)
fun main(args: Array<String>) {
    val options = Options().apply {
        addOption(
            "v",
            "version",
            false,
            "version of this library"
        )
        addOption(
            "r",
            "reduced",
            true,
            "reduces the color space by the given amount, 256/x"
        )
        addOption(
            "c",
            "compat",
            false,
            "requests to use the reduced ansi color set"
        )
        addOption(
            "n",
            "normalize",
            false,
            "normalizes the color value so it will use the maximum amount of the color palette."
        )
        addOption(
            "f",
            "file",
            true,
            "the file to be read"
        )
        addOption(
            "p",
            "palette",
            true,
            "indicates an optional palette file which will be used to color the output."
        )
        addOption(
            "P",
            "reducePalette",
            true,
            "reduces the color space of the palette file by the given amount, 256/x"
        )
        addOption(
            "w",
            "width",
            true,
            "the maximum width of the generated video"
        )
        addOption(
            "h",
            "height",
            true,
            "the maximum height of the generated video"
        )
        addOption(
            "H",
            "help",
            false,
            "prints this help text and exits."
        )
    }
    val parser: CommandLineParser = PosixParser()

    try {
        val cmd: CommandLine = parser.parse(options, args)
        when {
            cmd.hasOption("H") -> {
                HelpFormatter().printHelp("CMD", options)
                exitProcess(0)
            }
            cmd.hasOption("v") -> {
                val version: String = io.humble.video_native.Version.getVersionInfo()
                println("Humble Version: $version")
            }
            args.isEmpty() -> {
                val formatter = HelpFormatter()
                formatter.printHelp(Decoder::class.java.canonicalName + " <filename>", options)
            }
            else -> {
                Decoder(
                    filename = cmd.getOptionValue("f"),
                    palette = cmd.getOptionValue("p"),
                    reductionRate = cmd.getOptionValue("r")?.toInt() ?: 0,
                    paletteReductionRate = cmd.getOptionValue("P")?.toInt() ?: 0,
                    isCompatPalette = cmd.hasOption("c"),
                    shouldNormalize = cmd.hasOption("n"),
                    width = cmd.getOptionValue("w")?.toInt(),
                    height = cmd.getOptionValue("h")?.toInt()
                ).playVideo()
            }
        }
    } catch (e: ParseException) {
        System.err.println("Exception parsing command line: " + e.localizedMessage)
    }
}
