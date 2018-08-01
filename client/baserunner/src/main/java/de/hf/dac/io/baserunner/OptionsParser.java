/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : OptionsParser.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.io.config.resfile.Configuration;
import de.hf.dac.io.config.resfile.ResFileParser;
import de.hf.dac.io.config.resfile.ResfileConfigurationImpl;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the arguments that were given on the command line.
 */
// TODO New version of commons-cli has maybe better way to build options
public class OptionsParser {
    public static final String DEBUG_OPTION = "debug";
    public static final String HELP_OPTION = "help";
    private static final String DAC_LOGIN_INFO = ResfileConfigurationImpl.DAC_LOGIN_INFO;

    private static final String DEVPROPS_FILENAME = ResfileConfigurationImpl.DEVPROPS_FILENAME;
    private static final String DAC_RES_PATH_ENV = ResfileConfigurationImpl.DAC_RES_PATH_ENV;

    private static final Logger LOG = LoggerFactory.getLogger(OptionsParser.class);
    private static final String RES_FILE_SHORT_OPTION = "res";
    private static final String VERBOSE_SHORT = "v";

    private Boolean hasHelpOption = false;

    /** The options expected */
    protected Options options = new Options();

    /** The result of the parsing */
    protected CommandLine commandLine;

    /** Default constructor. */
    public OptionsParser() {
        String devResFileName = System.getenv("DEV_RES_FILE") != null ? System.getenv("DEV_RES_FILE") : DEVPROPS_FILENAME;

        if (new File(devResFileName).exists()) {
            LOG.info("Found development res file " + devResFileName + " reading it.");
            Configuration.parseResFile(devResFileName);
        }

        options.addOption(OptionBuilder.isRequired(false).withDescription("Verbose mode").withLongOpt("verbose").create("v"));
        options.addOption(OptionBuilder.hasArg().withDescription("Resource file").withLongOpt("resFile").create(RES_FILE_SHORT_OPTION));
        options.addOption(OptionBuilder.isRequired(false).withDescription("print usage").withLongOpt("help").create(HELP_OPTION));
    }

    protected String getConfigSection() {
        return "default";
    }

    /** Adds res file option. */
    public void addResOption() {
        options.addOption(OptionBuilder.hasArg().withDescription("Resource file").withLongOpt("resFile").create(RES_FILE_SHORT_OPTION));
    }

    /**
     * Creates an option group from the given option name, only one option from a group may exist on the command line.
     *
     * @param args
     *        The options to include in the group.
     * @param required
     *        If true, one of the options must be specified.
     */
    public void createOptionGroup(String[] args, boolean required) {
        OptionGroup group = new OptionGroup();
        for (String name : args) {
            Option option = options.getOption(name);
            group.addOption(option);
        }
        group.setRequired(required);
        options.addOptionGroup(group);

    }


    /**
     * Parses the given command line arguments, and processes the res files.
     *
     * @param args
     *        The command line arguments
     * @throws ParseException
     *         Thrown if the arguments could not be parsed
     */
    public void parse(String[] args) throws ParseException {
        parseCommandLineOptions(args);
        if(!hasHelpOption) parseResFilesOptions();
    }

    private void parseResFilesOptions() {
        String[] resFiles;
        if (commandLine.hasOption(RES_FILE_SHORT_OPTION)) {
            // command line takes precedence
            resFiles = commandLine.getOptionValues(RES_FILE_SHORT_OPTION);
        } else {
            // collect files from environment variables
            List<String> resFileList = new ArrayList<String>();
            if (System.getenv(DAC_LOGIN_INFO) != null) {
                String loginResFileName = System.getenv(DAC_LOGIN_INFO);
                if (new File(loginResFileName).exists()) {
                    resFileList.add(loginResFileName);
                } else {
                    LOG.info("Res File {}  from environment variable {} does not exist", loginResFileName, DAC_LOGIN_INFO);
                }
            } else {
                LOG.info("Environment variable {} not set.", DAC_LOGIN_INFO);
            }
            if (System.getenv(DAC_RES_PATH_ENV) != null) {
                // take it from environment
                String poetResFileName = System.getenv(DAC_RES_PATH_ENV) + "/" + ResfileConfigurationImpl.DAC_RES_FILENAME;

                if (new File(poetResFileName).exists() && !resFileList.contains(poetResFileName)) {
                    resFileList.add(poetResFileName);
                } else {
                    LOG.info("Res File {}  from environment variable {} does not exist", poetResFileName, DAC_RES_PATH_ENV);
                }
            } else {
                LOG.info("Environment variable {} not set.", DAC_RES_PATH_ENV);
            }
            if (resFileList.isEmpty()) {
                LOG.error("Using development defaults", DAC_LOGIN_INFO);
                resFiles = new String[] { "../" + ResfileConfigurationImpl.DAC_RES_FILENAME };
            } else {
                resFiles = (String[]) resFileList.toArray(new String[resFileList.size()]);
            }
        }
        for (String filename : resFiles) {
            LOG.info("Loading res file {}", filename);
            if (filename != null) {
                Configuration.parseResFile(filename);
            }
        }

        // set DEBUG if provided as commandLine Parameter
        if (commandLine.hasOption(DEBUG_OPTION)) {
            Configuration.setIsDebug(true);
            LOG.error("RUNNING IN DEBUG MODE");
        } else {
            Configuration.setIsDebug(false);
        }
    }

    private void parseCommandLineOptions(String[] args) throws ParseException {
        String additionalArgsString = Configuration.getString(getConfigSection(), "cli_args");
        String[] additionalsArgs = new String[0];
        if (additionalArgsString != null) {
            additionalsArgs = additionalArgsString.split("\\s");
        }
        String[] processedArgs = new String[args.length + additionalsArgs.length];
        for (int i = 0; i < processedArgs.length; i++) {
            String arg = i < args.length ? args[i] : additionalsArgs[i - args.length];
            while (arg.contains("$[")) {
                // make replacements
                int startOfVar = arg.indexOf("$[");
                // start of default value (optional)
                int endOfVar = arg.indexOf("]");
                String def = null;
                String variable;
                variable = arg.substring(startOfVar + 2, endOfVar);
                String value = Configuration.getString(getConfigSection(), "cli_var_" + variable, def);

                arg = arg.substring(0, startOfVar) + value + arg.substring(endOfVar + 1);
            }
            if (arg.startsWith("$")) {
                // start of default value (optional)
                int indexOfDefault = arg.indexOf(":");
                String def = null;
                String variable;
                if (indexOfDefault > 0) {
                    def = arg.substring(indexOfDefault + 1);
                    variable = arg.substring(1, indexOfDefault);
                } else {
                    variable = arg.substring(1);
                }
                String value = Configuration.getString(getConfigSection(), "cli_var_" + variable, def);
                if (value == null) {
                    throw new RuntimeException("Variable "+variable + " not defined please set  cli_var_" + variable + "=<value>  in dev.res/poet.res in section [CCR]");
                }
                arg = value;
            }
            if(arg.equals("-h")|| arg.equals("-help")) {
                hasHelpOption = true;
                return;
            }
            processedArgs[i] = arg;
        }
        commandLine = parseCmdLine(processedArgs);
    }

    protected CommandLine parseCmdLine(String[] processedArgs) throws ParseException {
        return new GnuParser().parse(options, processedArgs);
    }

    /**
     * These are arguments that remain after the options have been parsed.
     *
     * @return Array of real arguments
     */
    public String[] getRealArgs() {
        return commandLine.getArgs();
    }


    /**
     * Returns whether the option is present in the command line.
     *
     * @param option
     *        THe name of the option.
     * @return True if the option is present.
     */
    public boolean hasOption(String option) {
        return commandLine.hasOption(option);
    }

    public boolean isVerbose() {
        return commandLine.hasOption(VERBOSE_SHORT);
    }

    public boolean hasHelpOption() {
        return hasHelpOption;
    }


    /**
     * Adds a custom option, which has one argument.
     *
     * @param shortName
     *        The shortName of the option (can be null)
     * @param longName
     *        The long name of the option
     * @param description
     *        The description of the option (shown in usage)
     * @param argName
     *        The name of argument
     * @param required
     *        If true it is required.
     */
    public void addOptionWithArg(String shortName, String longName, String description, String argName, boolean required) {
        Option option;
        if (shortName != null) {
            option = new Option(shortName, longName, true, description);
        } else {
            option = new Option(longName, true, description);
        }
        option.setArgName(argName);
        option.setRequired(required);
        options.addOption(option);
    }

    /**
     * Add option without argument.
     *
     * @param shortName
     *        The shortName of the option (can be null)
     * @param longName
     *        The long name of the option
     * @param description
     *        The description of the option (shown in usage)
     * @param required
     *        If true it is required.
     */
    public void addOption(String shortName, String longName, String description, boolean required) {
        Option option = new Option(shortName, longName, false, description);
        option.setRequired(required);
        options.addOption(option);
    }

    /**
     * Gets the argument of the custom option.
     *
     * @param name
     *        The name of the custom option
     * @return The value of the option
     */
    public String getOptionArg(String name) {
        return commandLine.getOptionValue(name);
    }

    /**
     * Gets the argument of the custom option as an array.
     *
     * @param name
     *        The name of the custom option
     * @return The values of the option
     */
    public String[] getOptionArgArray(String name) {
        return commandLine.getOptionValues(name);
    }

    /**
     * Prints the usage.
     *
     * @param app
     *        The name of the application
     * @param header
     *        the header to print.
     */
    public void printUsage(String app, String header) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(app, header, options, "");

    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

}

