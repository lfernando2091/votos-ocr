package saganet.mx.com.bingo.sheet;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.variables.BingoC;

/**
 * Created by LuisFernando on 17/03/2017.
 */

public class SheetOMR {
    LoggerC log=new LoggerC(SheetOMR.class);
    List<Integer> value;
    List<String> key;


    public SheetOMR() {
    }

    public void RunOMR(){
        List<SheetConfig> c=getMark();
        double promedioRadio=(BingoC.respuesta.promRadio());
        int asig=0, def=0;
        for(SheetConfig sheetConfig: c){
            if(sheetConfig.getColumn()==0){
                sheetConfig.setRow(++asig);
            } else if(sheetConfig.getColumn()==24){
                sheetConfig.setRow(++def);
            }
        }
        //-----BingoC.respuesta.numReference
        for(int i=0;i<BingoC.respuesta.numReference;i++){
            //TODO ALGORITMO PRIMERA PRUEBA DETECCION POR FILAS
            double dif=c.get(i+BingoC.respuesta.numReference).getPosY()-c.get(i).getPosY();
            double nextRefPosition=0.0;
            if(i==32){nextRefPosition=c.get(i).getPosX()+ (promedioRadio+6.0);
            } else{nextRefPosition= c.get(i+1).getPosX()- promedioRadio;
            }
            double relativeColum=dif/(BingoC.respuesta.numColums+1);
            double posPerColumn=c.get(i).getPosY();
            for (int h=0;h< BingoC.respuesta.numColums;h++){
                //TODO ALGORITMO PRIMERA PRUEBA DETECCION POR COLUMNAS
                posPerColumn+=relativeColum;
                for(SheetConfig sheetConfig: BingoC.respuesta.getSheetX()){
                    if(((sheetConfig.getPosX()<nextRefPosition)) && !sheetConfig.isMark()){
                        //Asignar fila
                        sheetConfig.setRow(i+1);
                        sheetConfig.setMark(true);
                    }
                    if((sheetConfig.getPosY()>(posPerColumn-promedioRadio)) &&
                            (sheetConfig.getPosY()<(posPerColumn+promedioRadio)) && sheetConfig.isMark()){
                        //Asignar columna
                        sheetConfig.setColumn(h+1);
                        sheetConfig.setAnswer(true);
                    }
                }
            }
        }

        SheetMapC sheetMapC= new SheetMapC();
        value= new ArrayList<Integer>();
        key= new ArrayList<String>();
        for(SheetConfig sheetConfig: BingoC.respuesta.getSheetX()){
            if(!sheetConfig.isReference()){
                sheetConfig.setRealId(sheetMapC.getIdAnswer(sheetConfig));
                log.printf("contestados: "+sheetMapC.getIdAnswer(sheetConfig));
                //log.printf("datos analizados: " +sheetConfig.toString());
                value.add(sheetMapC.getIdAnswer(sheetConfig));
                key.add(""+ sheetConfig.getRow() +"-"+sheetConfig.getColumn());
            }
        }
    }

    public int Size(){
        return value.size();
    }

    public String getResultValue() {
        String h="";
        Collections.sort(value, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
        for (Integer s: value) h+=s+",";
        return h;
    }

    public String getResultKey() {
        String h="";
        for (String s: key) h+=s+",";
        return h;
    }


    private List<SheetConfig> getMark(){
        List<SheetConfig> a= new ArrayList<>();
        List<SheetConfig> b= new ArrayList<>();
        for (SheetConfig s: BingoC.respuesta.getFisrtMarkReference()) {
            a.add(s);
            Collections.sort(a,new SheetConfig.CompXY());
        }
        for (SheetConfig s: BingoC.respuesta.getUltimateMarkReference()) {
            b.add(s);
            Collections.sort(b,new SheetConfig.CompXY());
        }
        a.addAll(b);
        return a;
    }
}
