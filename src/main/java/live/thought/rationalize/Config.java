package live.thought.rationalize;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Config
{
  /** Options for the command line parser. */
  protected static final Options           options             = new Options();
  /** The Commons CLI command line parser. */
  protected static final CommandLineParser gnuParser           = new GnuParser();
  /** Default values for connection. */
  private static final String              DEFAULT_HOST        = "localhost";
  private static final String              DEFAULT_PORT        = "11617";
  private static final String              DEFAULT_USER        = "user";
  private static final String              DEFAULT_PASS        = "password";
  private static final String              DEFAULT_PREFIX      = "Rationalize";
  private static final String              DEFAULT_INPUT_FILE  = "rationalize_input.csv";
  private static final String              DEFAULT_OUTPUT_FILE = "rationalize_results.csv";

  private static final String              HOST_PROPERTY       = "host";
  private static final String              PORT_PROPERTY       = "port";
  private static final String              USER_PROPERTY       = "user";
  private static final String              PASS_PROPERTY       = "password";
  private static final String              PREFIX_PROPERTY     = "prefix";
  private static final String              ACCOUNT_PROPERTY    = "account";
  private static final String              HELP_OPTION         = "help";
  private static final String              CONFIG_OPTION       = "config";
  private static final String              ACCOUNT_FILE_OPTION = "accountFile";
  private static final String              OUTPUT_FILE_OPTION  = "output";

  /** Set up command line options. */
  static
  {
    options.addOption("H", HOST_PROPERTY, true, "Thought RPC server host (default: localhost)");
    options.addOption("P", PORT_PROPERTY, true, "Thought RPC server port (default: 11617)");
    options.addOption("u", USER_PROPERTY, true, "Thought server RPC user");
    options.addOption("p", PASS_PROPERTY, true, "Thought server RPC password");
    options.addOption("P", PREFIX_PROPERTY, true, "Prefix for created account names. (default: Rationalize)");
    options.addOption("a", ACCOUNT_PROPERTY, true, "Thought wallet account name to obtain funds from");
    options.addOption("A", ACCOUNT_FILE_OPTION, true, "File containing funding lines (default: rationalize_input.csv");
    options.addOption("o", OUTPUT_FILE_OPTION, true, "File to write account keys to (default: rationalize_results.csv");
    options.addOption("h", HELP_OPTION, true, "Displays usage information");
    options.addOption("f", CONFIG_OPTION, true, "Configuration file to load options from.  Command line options override config file.");
  }

  protected String host;
  protected int    port;
  protected String user;
  protected String password;

  protected String prefix;
  protected String sourceAccount;
  protected String accountFileName;
  protected String outputFileName;
  
  protected boolean testnet = false;

  public Config(String[] args)
  {
    CommandLine commandLine = null;

    try
    {
      Properties props = new Properties();
      // Read the command line
      commandLine = gnuParser.parse(options, args);
      // Check for the help option
      if (commandLine.hasOption(HELP_OPTION))
      {
        usage();
        System.exit(0);
      }
      // Check for a config file specified on the command line
      if (commandLine.hasOption(CONFIG_OPTION))
      {
        try
        {
          props.load(new FileInputStream(new File(commandLine.getOptionValue(CONFIG_OPTION))));
        }
        catch (Exception e)
        {
          Console.output(String.format("@|red Specified configuration file %s unreadable or not found.|@",
              commandLine.getOptionValue(CONFIG_OPTION)));
          System.exit(1);
        }
      }
      // Command line options override config file values
      if (commandLine.hasOption(HOST_PROPERTY))
      {
        props.setProperty(HOST_PROPERTY, commandLine.getOptionValue(HOST_PROPERTY));
      }
      if (commandLine.hasOption(PORT_PROPERTY))
      {
        props.setProperty(PORT_PROPERTY, commandLine.getOptionValue(PORT_PROPERTY));
      }
      if (commandLine.hasOption(USER_PROPERTY))
      {
        props.setProperty(USER_PROPERTY, commandLine.getOptionValue(USER_PROPERTY));
      }
      if (commandLine.hasOption(PASS_PROPERTY))
      {
        props.setProperty(PASS_PROPERTY, commandLine.getOptionValue(PASS_PROPERTY));
      }
      if (commandLine.hasOption(ACCOUNT_PROPERTY))
      {
        props.setProperty(ACCOUNT_PROPERTY, commandLine.getOptionValue(ACCOUNT_PROPERTY));
      }
      if (commandLine.hasOption(ACCOUNT_FILE_OPTION))
      {
        props.setProperty(ACCOUNT_FILE_OPTION, commandLine.getOptionValue(ACCOUNT_FILE_OPTION));
      }
      if (commandLine.hasOption(OUTPUT_FILE_OPTION))
      {
        props.setProperty(OUTPUT_FILE_OPTION, commandLine.getOptionValue(OUTPUT_FILE_OPTION));
      }
      if (commandLine.hasOption(PREFIX_PROPERTY))
      {
        props.setProperty(PREFIX_PROPERTY, commandLine.getOptionValue(PREFIX_PROPERTY));
      }
      
      host = props.getProperty(HOST_PROPERTY, DEFAULT_HOST);
      port = Integer.parseInt(props.getProperty(PORT_PROPERTY, DEFAULT_PORT));
      user = props.getProperty(USER_PROPERTY, DEFAULT_USER);
      password = props.getProperty(PASS_PROPERTY, DEFAULT_PASS);
      sourceAccount = props.getProperty(ACCOUNT_PROPERTY, "");
      accountFileName = props.getProperty(ACCOUNT_FILE_OPTION, DEFAULT_INPUT_FILE);
      outputFileName = props.getProperty(OUTPUT_FILE_OPTION, DEFAULT_OUTPUT_FILE);
      prefix = props.getProperty(PREFIX_PROPERTY, DEFAULT_PREFIX);
      
      if (props.getProperty(PORT_PROPERTY, DEFAULT_PORT).startsWith("11"))
      {
        testnet = true;
      }
    }
    catch (ParseException pe)
    {
      System.err.println(pe.getLocalizedMessage());
      usage();
    }
    catch (Exception e)
    {
      System.err.println(e.getLocalizedMessage());
      e.printStackTrace(System.err);
    }
  }

  public String getSourceAccount()
  {
    return sourceAccount;
  }

  public void setSourceAccount(String sourceAccount)
  {
    this.sourceAccount = sourceAccount;
  }

  public String getHost()
  {
    return host;
  }

  public void setHost(String host)
  {
    this.host = host;
  }

  public int getPort()
  {
    return port;
  }

  public void setPort(int port)
  {
    this.port = port;
  }

  public String getUser()
  {
    return user;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }
    
  public String getAccountFileName()
  {
    return accountFileName;
  }

  public void setAccountFileName(String accountFileName)
  {
    this.accountFileName = accountFileName;
  }

  public String getOutputFileName()
  {
    return outputFileName;
  }

  public void setOutputFileName(String outputFileName)
  {
    this.outputFileName = outputFileName;
  }
  
  public boolean isTestnet()
  {
    return testnet;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  public static void usage()
  {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("Rationalize", options);
  }
}
