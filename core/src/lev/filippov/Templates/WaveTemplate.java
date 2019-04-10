package lev.filippov.Templates;
//# map-down
//# numberOfWawes
//5
//# waveNumber, monstersHP, rate(per sec), duration(sec)
//    1,  50,     3,  20
//    2,  75,     5,  20
//    3,  100,    7,  20
//    4,  120,    10, 20
//    5,  150,    10, 40

public class WaveTemplate {
    private int waveNumber;
    private int monsterHP;
    private float rate;
    private int duration;
    //диапазон для респауна монстров
    private int cellXBegin;
    private int cellXEnd;
    private int cellYBegin;
    private int cellYEnd;


    public WaveTemplate(String strTemplate1) {
        String[] tokens = strTemplate1.split(",");
        this.waveNumber = Integer.parseInt(tokens[0].trim());
        this.monsterHP = Integer.parseInt(tokens[1].trim());
        this.rate = 1/Float.parseFloat(tokens[2].trim());
        this.duration = Integer.parseInt(tokens[3].trim());
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getMonsterHP() {
        return monsterHP;
    }

    public float getRate() {
        return rate;
    }

    public int getDuration() {
        return duration;
    }

    public void addMonsterRespawnCells(String str){
        String[] tokens = str.split(",");
        this.cellXBegin = Integer.parseInt(tokens[0].trim());
        this.cellXEnd = Integer.parseInt(tokens[1].trim());
        this.cellYBegin = Integer.parseInt(tokens[2].trim());
        this.cellYEnd = Integer.parseInt(tokens[3].trim());
    }

    public int getCellXBegin() {
        return cellXBegin;
    }

    public int getCellXEnd() {
        return cellXEnd;
    }

    public int getCellYBegin() {
        return cellYBegin;
    }

    public int getCellYEnd() {
        return cellYEnd;
    }
}
