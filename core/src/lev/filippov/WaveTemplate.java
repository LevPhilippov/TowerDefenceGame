package lev.filippov;
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
}
