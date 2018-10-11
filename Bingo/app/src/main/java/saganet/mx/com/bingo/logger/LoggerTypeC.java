package saganet.mx.com.bingo.logger;

/**
 * Created by LuisFernando on 16/02/2017.
 */

public abstract class LoggerTypeC {
    public enum eLoggerType{
        None,
        Error,
        Info,
        Verboso,
        Debug,
        Warning,
        Afirmar
    }
    public enum eLoggerTypeActive{
        None,
        Active,
        Inactive
    }
    public static eLoggerType LoggerType= eLoggerType.None;
    public static eLoggerTypeActive LoggerActive= eLoggerTypeActive.Inactive;
    public final static  String LoggerTokenSeparator="::";
    public final static  String LoggerToken="Bingo"+LoggerTokenSeparator;
}
