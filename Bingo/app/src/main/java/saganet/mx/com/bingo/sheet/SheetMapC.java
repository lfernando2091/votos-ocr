package saganet.mx.com.bingo.sheet;

import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.variables.BingoC;
import saganet.mx.com.bingo.xdata.omr;

/**
 * Created by LuisFernando on 14/03/2017.
 */

public class SheetMapC {
    LoggerC log=new LoggerC(SheetMapC.class);
    private int key[][];
    private int columns=23, row=33, id=0;
    private int block[]={22,45,68,91,114,137,160,183,206,229,252,275,298,321,344,367,388,411,434,457,480,503,526,549,572,595,618,641,664,687,710,733,755};

    public SheetMapC(){
        id=0;
        key= new int[row][columns];
        for (int i=0;i<row;i++){
            for(int c=0;c<columns;c++){
                //Agregar elementos en blancos a suprimir como campos de respuesta
                if(     (i==0 && c==11)  ||
                        (i==16 && c==11) ||
                        (i==16 && c==12) ||
                        (i==32 && c==11) ||
                        (i==32 && c==18) ||
                        (i==32 && c==19) ||
                        (i==32 && c==20) ||
                        (i==32 && c==21) ||
                        (i==32 && c==22) ){
                    key[i][c]=0;
                }
                else {
                    key[i][c]=++id;
                }
            }
        }
    }
    public int getIdAnswer(SheetConfig sheetConfig){
        if(BingoC.SpecialDetection){
            int type=getIdElement(sheetConfig.getColumn(), sheetConfig.getRow());
            if(type>=1 && type<=22){
                return (22-type)+1;
            }else {
                for(int u=0;u<33;u++){
                    if(type>=(block[u]+1) && type<=block[u+1]){
                        return ((block[u]+1)+(block[u+1]-type));
                    }
                }
            }
        } else{
            return getIdElement(sheetConfig.getColumn(), sheetConfig.getRow());
        }
        return 0;
    }

    public int getIdAnswer(int column, int row){
        return getIdElement(column, row);
    }

    private int getIdElement(int column, int row){
        if(column==0 || row==0){
            return 0;
        }
        return key[row-1][column-1];
    }
}
