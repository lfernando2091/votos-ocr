package saganet.mx.com.bingo.sheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import saganet.mx.com.bingo.logger.LoggerC;
import saganet.mx.com.bingo.variables.BingoC;

/**
 * Created by LuisFernando on 06/03/2017.
 */

public class SheetAnswers {
    private List<SheetConfig> datos;
    private List<SheetConfig> order;
    LoggerC log=new LoggerC(SheetAnswers.class);
    public final int numReference=33;
    public final int numColums=23;
    private double promArea=0.0,promRadio=0.0;

    @Override
    public String toString() {
        return "SheetAnswers{" +
                "datos=" + datos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SheetAnswers that = (SheetAnswers) o;

        return datos != null ? datos.equals(that.datos) : that.datos == null;

    }

    @Override
    public int hashCode() {
        return datos != null ? datos.hashCode() : 0;
    }

    public SheetAnswers(){
        datos=new ArrayList<SheetConfig>();
    }
    public void fillSheet(SheetConfig sheetConfig){
        this.datos.add(sheetConfig);
    }

    public List<SheetConfig> getSheetY(){
        //Collections.sort(datos,new SheetConfig.Comp());
        Collections.sort(datos,new SheetConfig.CompY());
        return datos;
    }
    public List<SheetConfig> getSheetX(){
        //Collections.sort(datos,new SheetConfig.Comp());
        Collections.sort(datos,new SheetConfig.CompX());
        return datos;
    }

    public double promArea(){
        promArea=0.0;
        for(SheetConfig sheetConfig:datos){
            promArea+=sheetConfig.getArea();
        }
        promArea/=datos.size();
        return promArea;
    }
    public double promRadio(){
        promRadio=0.0;
        for(SheetConfig sheetConfig:datos){
            promRadio+=sheetConfig.getRadio();
        }
        promRadio/=datos.size();
        return promRadio;
    }
    //TODO Encontrar la marca de referencia primaria en Y
    public List<SheetConfig> getFisrtMarkReference(){
        return getFisrt(getSheetY());
    }
    private List<SheetConfig> getFisrt(List<SheetConfig> datos){
        List<SheetConfig> sheetConfigList = new ArrayList<>();
        for(int n=0;n<numReference;n++){
            sheetConfigList.add(datos.get(n));
            datos.get(n).setMark(true);
            datos.get(n).setReference(true);
        }
        return sheetConfigList;
    }
    //TODO Encontrar la marca ULTIMA de referencia en Y
    public List<SheetConfig> getUltimateMarkReference(){
        return getUltimate(getSheetY());
    }
    private List<SheetConfig> getUltimate(List<SheetConfig> datos){
        List<SheetConfig> sheetConfigList = new ArrayList<>();
        for(int n=(datos.size()-numReference);n<datos.size();n++){
            sheetConfigList.add(datos.get(n));
            datos.get(n).setMark(true);
            datos.get(n).setReference(true);
            datos.get(n).setColumn(24);
        }
        return sheetConfigList;
    }

    private void parseColumns(){
        List<SheetConfig> omr = null;
        double ysum=0.0;
        double mark=0.0;
        int zentinell=3, colon=1;
        for (SheetConfig s: BingoC.respuesta.getSheetY()) {
            omr.add(s);
        }
        //Obtener la posición de los circulos de referencia marca los primeros tres
        for(int c=0; c<zentinell;c++){
            ysum+=omr.get(c).getPosY();
        }ysum/=3;
        for (int c=zentinell; c<omr.size();c++){
            if(omr.get(c).getPosY()<=ysum){
                omr.get(c).setColumn(colon);
                colon++;
            }else {
                mark=0;
            }
        }


    }
}
