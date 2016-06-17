/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : OptionParser.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 08.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.common.runner;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Slf4j
public class OptionsParser {
    public static final String DEBUG_OPTION = "debug";
    private static final String CAD_LOGIN_INFO = "CAD_LOGIN_INFO";
    private static final String DEVPROPS_FILENAME = "../dev.res";
    private static final String CAD_RES_PATH_ENV = "CAD_RES_PATH";
    private static final String VERBOSE_SQL_LONG = "vs";
    private static final String EXTRA_VERBOSE_SQL_LONG = "vvs";
    private static final Logger LOG = LoggerFactory.getLogger(OptionsParser.class);
    private static final String DBINI_SHORT_OPTION = "dbi";
    private static final String RES_FILE_SHORT_OPTION = "res";
    private static final String VERBOSE_SHORT = "v";
    protected Options options = new Options();
    protected CommandLine commandLine;

    public OptionsParser() {
        if((new File("../dev.res")).exists()) {
            LOG.info("Found development res file ../dev.res reading it.");
            Configuration.parseResFile("../dev.res");
        }

        Options var10000 = this.options;
        OptionBuilder.isRequired(false);
        OptionBuilder.withDescription("Verbose mode");
        OptionBuilder.withLongOpt("verbose");
        var10000.addOption(OptionBuilder.create("v"));
        var10000 = this.options;
        OptionBuilder.isRequired(false);
        OptionBuilder.withDescription("Verbose SQL mode");
        OptionBuilder.withLongOpt("vs");
        var10000.addOption(OptionBuilder.create());
        var10000 = this.options;
        OptionBuilder.isRequired(false);
        OptionBuilder.withDescription("Extra Verbose SQL mode (bind parameters)");
        OptionBuilder.withLongOpt("vvs");
        var10000.addOption(OptionBuilder.create());
        var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Resource file");
        OptionBuilder.withLongOpt("resFile");
        var10000.addOption(OptionBuilder.create("res"));
    }

    protected String getConfigSection() {
        return "default";
    }

    public void addResOption() {
        Options var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("Resource file");
        OptionBuilder.withLongOpt("resFile");
        var10000.addOption(OptionBuilder.create("res"));
    }

    public void addDbOptions(String prefix) {
        Options var10000;
        if(!this.options.hasOption("dbi")) {
            var10000 = this.options;
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("DatabaseFile <name> ");
            OptionBuilder.withLongOpt("dbIni");
            var10000.addOption(OptionBuilder.create("dbi"));
        }

        var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("DatabaseURL <jdbc:sybase:Tds:server:port/DB> ");
        OptionBuilder.withLongOpt(prefix + "dbURL");
        var10000.addOption(OptionBuilder.create(prefix + "db"));
        if(prefix.length() > 0) {
            var10000 = this.options;
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("DatabaseKey <name> ");
            OptionBuilder.withLongOpt(prefix + "dbKey");
            var10000.addOption(OptionBuilder.create(prefix));
        }

        var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("DatabaseUser <user>");
        OptionBuilder.withLongOpt(prefix + "dbUser");
        var10000.addOption(OptionBuilder.create(prefix + "u"));
        var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("DatabasePassword <password>");
        OptionBuilder.withLongOpt(prefix + "dbPassword");
        var10000.addOption(OptionBuilder.create(prefix + "p"));
        var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(false);
        OptionBuilder.withDescription("DatabaseDriver <default com.sybase.jdbc4.jdbc.SybDriver> optional");
        OptionBuilder.withLongOpt(prefix + "dbDriver");
        var10000.addOption(OptionBuilder.create(prefix + "d"));
    }

    public void createOptionGroup(String[] args, boolean required) {
        OptionGroup group = new OptionGroup();
        String[] arr$ = args;
        int len$ = args.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String name = arr$[i$];
            Option option = this.options.getOption(name);
            group.addOption(option);
        }

        group.setRequired(required);
        this.options.addOptionGroup(group);
    }

    public DatabaseInfo getDatabaseInfo(String prefix) {
        String driver = "com.sybase.jdbc4.jdbc.SybDriver";
        String url = this.commandLine.getOptionValue(prefix + "db");
        if(this.commandLine.hasOption('d')) {
            driver = this.commandLine.getOptionValue(prefix + 'd');
        }

        String dbServer = null;
        String dbName = null;
        String user;
        String password;
        if(url == null) {
            String dbIniFile = this.commandLine.getOptionValue("dbi");
            String dbKey = this.commandLine.getOptionValue(prefix);
            if(dbIniFile == null || dbKey == null) {
                throw new RuntimeException("Must specify either url or dbIni and dbKey for prefix " + prefix);
            }

            String dbLine = Configuration.getString("LOGIN_INFO", dbKey);
            if(dbLine == null) {
                throw new RuntimeException("For prefix " + prefix + ": Database Key " + dbKey + " not found in res files");
            }

            String[] parts = dbLine.split("\\s*,\\s*");
            dbServer = parts[0];
            if(dbServer == null) {
                throw new RuntimeException("Server not found not for DB " + dbKey + " in res files");
            }

            dbName = parts[1];
            user = parts[2];
            password = ResFileParser.decrypt(parts[3]);
            url = "jdbc:sybase:jndi:file://" + dbIniFile + "?" + dbServer + "&DATABASE=" + dbName + "&HOMOGENEOUS_BATCH=false";
            if(user == null) {
                throw new RuntimeException("User not found not for DB " + dbKey + " in res files");
            }

            if(password == null) {
                throw new RuntimeException("Password not found for DB " + dbKey + " in res files");
            }
        } else {
            user = this.commandLine.getOptionValue(prefix + "u");
            password = this.commandLine.getOptionValue(prefix + "p");
            if(user == null) {
                throw new RuntimeException("User not specified on command line -" + prefix + "u");
            }

            if(password == null) {
                throw new RuntimeException("Password  not specified on command line -" + prefix + "p");
            }
        }

        return new DatabaseInfo(url, user, password, driver, dbServer, dbName);
    }

    public void parse(String[] args) throws ParseException {
        String additionalArgsString = Configuration.getString(this.getConfigSection(), "cli_args");
        String[] additionalsArgs = new String[0];
        if(additionalArgsString != null) {
            additionalsArgs = additionalArgsString.split("\\s");
        }

        String[] processedArgs = new String[args.length + additionalsArgs.length];

        int len$;
        String filename;
        for(int resFiles = 0; resFiles < processedArgs.length; ++resFiles) {
            String arr$ = resFiles < args.length?args[resFiles]:additionalsArgs[resFiles - args.length];
            if(arr$.startsWith("$")) {
                len$ = arr$.indexOf(":");
                String i$ = null;
                if(len$ > 0) {
                    i$ = arr$.substring(len$ + 1);
                    filename = arr$.substring(1, len$);
                } else {
                    filename = arr$.substring(1);
                }

                String value = Configuration.getString(this.getConfigSection(), "cli_var_" + filename, i$);
                arr$ = value;
            }

            processedArgs[resFiles] = arr$;
        }

        this.commandLine = this.parseCmdLine(processedArgs);
        String[] var11;
        if(this.commandLine.hasOption("res")) {
            var11 = this.commandLine.getOptionValues("res");
        } else {
            ArrayList var12 = new ArrayList();
            String var14;
            if(System.getenv("CAD_LOGIN_INFO") != null) {
                var14 = System.getenv("CAD_LOGIN_INFO");
                if((new File(var14)).exists()) {
                    var12.add(var14);
                } else {
                    LOG.info("Res File {}  from environment variable {} does not exist", var14, "CAD_LOGIN_INFO");
                }
            } else {
                LOG.info("Environment variable {} not set.", "CAD_LOGIN_INFO");
            }

            if(System.getenv("CAD_RES_PATH") != null) {
                var14 = System.getenv("CAD_RES_PATH") + "/poet.res";
                if((new File(var14)).exists() && !var12.contains(var14)) {
                    var12.add(var14);
                } else {
                    LOG.info("Res File {}  from environment variable {} does not exist", var14, "CAD_RES_PATH");
                }
            } else {
                LOG.info("Environment variable {} not set.", "CAD_RES_PATH");
            }

            if(var12.isEmpty()) {
                LOG.error("Using development defaults", "CAD_LOGIN_INFO");
                var11 = new String[]{"../poet.res"};
            } else {
                var11 = (String[])((String[])var12.toArray(new String[var12.size()]));
            }
        }

        String[] var13 = var11;
        len$ = var11.length;

        for(int var15 = 0; var15 < len$; ++var15) {
            filename = var13[var15];
            LOG.info("Loading res file {}", filename);
            if(filename != null) {
                Configuration.parseResFile(filename);
            }
        }

        if(this.commandLine.hasOption("debug")) {
            Configuration.setIsDebug(true);
            LOG.error("RUNNING IN DEBUG MODE");
            LOG.error("RUNNING IN DEBUG MODE");
            LOG.error("RUNNING IN DEBUG MODE");
        } else {
            Configuration.setIsDebug(false);
        }

    }

    protected CommandLine parseCmdLine(String[] processedArgs) throws ParseException {
        return (new GnuParser()).parse(this.options, processedArgs);
    }

    public String getFilename() {
        return this.commandLine.getOptionValue('f');
    }

    public String getDescriptorFilename() {
        return this.commandLine.getOptionValue("df");
    }

    public String[] getRealArgs() {
        return this.commandLine.getArgs();
    }

    public void addDirectoryOption(boolean required) {
        this.addOptionWithArg("o", "output directory", "Directory where the CSV are stored", "directory", true);
    }

    public String getDirectory() {
        return this.getOptionArg("o");
    }

    public String getDirectoryWithTrailingSlash() {
        String directory = this.getOptionArg("o");
        if(!directory.endsWith("/")) {
            directory = directory + "/";
        }

        return directory;
    }

    public boolean hasOption(String option) {
        return this.commandLine.hasOption(option);
    }

    public boolean isVerbose() {
        return this.commandLine.hasOption("v");
    }

    public boolean isVerboseSQL() {
        return this.commandLine.hasOption("vs");
    }

    public boolean isExtraVerboseSQL() {
        return this.commandLine.hasOption("vvs");
    }

    public void addFileOption() {
        Options var10000 = this.options;
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withDescription("File <file1> required");
        OptionBuilder.withLongOpt("file");
        var10000.addOption(OptionBuilder.create("f"));
    }

    public void addOptionWithArg(String shortName, String longName, String description, String argName, boolean required) {
        Option option;
        if(shortName != null) {
            option = new Option(shortName, longName, true, description);
        } else {
            option = new Option(longName, true, description);
        }

        option.setArgName(argName);
        option.setRequired(required);
        this.options.addOption(option);
    }

    public void addOption(String shortName, String longName, String description, boolean required) {
        Option option = new Option(shortName, longName, false, description);
        option.setRequired(required);
        this.options.addOption(option);
    }

    public String getOptionArg(String name) {
        return this.commandLine.getOptionValue(name);
    }

    public String[] getOptionArgArray(String name) {
        return this.commandLine.getOptionValues(name);
    }

    public void printUsage(String app, String header) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp(app, header, this.options, "");
    }

    public String getDbInterfaceFile() {
        String dbi = null;
        if(this.commandLine.hasOption("dbi")) {
            dbi = this.commandLine.getOptionValue("dbi");
        }

        return dbi;
    }

    public Options getOptions() {
        return this.options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}

