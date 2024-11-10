package com.mz.nlpservice;


import com.mz.nlpservice.core.Context;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;


/**
 * Created by meizu on 2016/6/29.
 */
public class Main {
    private static Logger LOG = Logger.getLogger(Main.class);
    private static final String DEF_CONFIG = "server.ini";

    public static void main(String args[]) throws Exception {
        CommandLine line = parseParam(args);
        if (line == null)
            return;
        String confPath = DEF_CONFIG;
        if (!line.hasOption("conf")) {
            LOG.info("will use the default config file:" + DEF_CONFIG);
        }
        if (line.hasOption("conf")) {
            confPath = line.getOptionValue("conf");
        }
        LOG.info("init server from conf file:" + confPath);
        Context.getInstance().parseFile(confPath);
    }

    private static CommandLine parseParam(String args[]) throws ParseException {
        Options options = new Options();
        Option help = new Option( "h", "help", false, "print this message" );
        Option confFile   =  Option.builder( "conf" )
                .argName("Config File")
                .hasArg()
                .desc("use config file for server, default=server.ini")
                .build();
        options.addOption(help);
        options.addOption(confFile);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine line = parser.parse( options, args );
        if (line.hasOption("h")) {
            formatter.printHelp("server", options);
            return null;
        }
        return  line;
    }
}
