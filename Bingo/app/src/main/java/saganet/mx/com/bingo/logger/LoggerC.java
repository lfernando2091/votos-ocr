package saganet.mx.com.bingo.logger;
import android.util.Log;

/**
 * Created by LuisFernando on 16/02/2017.
 */

public final class LoggerC extends LoggerTypeC implements LoggerI{
    private Class<?> context=null;
    public LoggerC(){
    }
    public LoggerC(Class<?> c){
        context=c;
    }
    public void printf(String key, String value, eLoggerType t){
        if(LoggerActive.equals(eLoggerTypeActive.Active)){
            switch (t){
                case Afirmar:
                    Log.i(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
                    break;
                case Error:
                    Log.e(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
                    break;
                case Info:
                    Log.i(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
                    break;
                case Verboso:
                    Log.v(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
                    break;
                case Debug:
                    Log.d(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
                    break;
                case Warning:
                    Log.w(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
                    break;
            }
        }
    }
    public void printf(String key, String value){
        if(LoggerActive.equals(eLoggerTypeActive.Active)){
            Log.e(LoggerToken + context.getName() + LoggerTokenSeparator + key,value);
        }
    }
    public void printf(String value){
        if(LoggerActive.equals(eLoggerTypeActive.Active)){
            Log.e(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
        }
    }
    public void printf(String value, eLoggerType t){
        if(LoggerActive.equals(eLoggerTypeActive.Active)){
            switch (t){
                case Afirmar:
                    Log.i(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
                    break;
                case Error:
                    Log.e(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
                    break;
                case Info:
                    Log.i(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
                    break;
                case Verboso:
                    Log.v(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
                    break;
                case Debug:
                    Log.d(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
                    break;
                case Warning:
                    Log.w(LoggerToken + context.getName() + LoggerTokenSeparator + "Value",value);
                    break;
            }
        }
    }
}