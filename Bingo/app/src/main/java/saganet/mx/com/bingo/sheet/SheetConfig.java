package saganet.mx.com.bingo.sheet;


import java.util.Comparator;

/**
 * Created by LuisFernando on 06/03/2017.
 */

public class SheetConfig implements Comparable<SheetConfig>{
    private int id;
    private double posX;
    private double posY;
    private double area;
    private boolean anable;
    private double radio;
    private int sumXY;
    private int column;
    private int row;
    private boolean mark;
    private boolean answer;
    private boolean reference;
    private int realId;

    @Override
    public String toString() {
        return "SheetConfig{" +
                "id=" + id +
                ", posX=" + posX +
                ", posY=" + posY +
                ", area=" + area +
                ", anable=" + anable +
                ", radio=" + radio +
                ", sumXY=" + sumXY +
                ", column=" + column +
                ", row=" + row +
                ", mark=" + mark +
                ", answer=" + answer +
                ", reference=" + isReference() +
                ", realId=" + realId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getSumXY(){
        return sumXY;
    }
    public void setSumXY(double posX,double posY) {
        this.sumXY=(int)Math.round(this.posX) + (int)Math.round(this.posY);
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getArea() {
        area=Math.sqrt((Math.PI*radio));
        return area;
    }

    public boolean isAnable() {
        return anable;
    }

    public void setAnable(boolean anable) {
        this.anable = anable;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        area=Math.sqrt((Math.PI*radio));
        this.radio = radio;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public static class CompX implements Comparator<SheetConfig> {
        @Override
        public int compare(SheetConfig arg0, SheetConfig arg1) {
            return (int)Math.round(arg0.posX) - (int)Math.round(arg1.posX);
        }
    }
    public static class CompY implements Comparator<SheetConfig> {
        @Override
        public int compare(SheetConfig arg0, SheetConfig arg1) {
            return (int)Math.round(arg0.posY) - (int)Math.round(arg1.posY);
        }
    }
    public static class CompXY implements Comparator<SheetConfig> {
        @Override
        public int compare(SheetConfig arg0, SheetConfig arg1) {
            return arg0.sumXY - arg1.sumXY;
        }
    }
    public static class CompArea implements Comparator<SheetConfig> {
        @Override
        public int compare(SheetConfig arg0, SheetConfig arg1) {
            return (int)Math.round(arg0.area) - (int)Math.round(arg1.area);
        }
    }

    @Override
    public int compareTo(SheetConfig o) {
        int compare =((SheetConfig)o).getSumXY();

        //ascending order
        //return this.sumXY - compare;
        //descending order
        return compare - this.sumXY;
    }
}
