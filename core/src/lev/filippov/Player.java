package lev.filippov;

public class Player {

    private int hp;
    private int money;
    private int score;

    private static Player ourInstance = new Player();


    public static Player getInstance() {
        return ourInstance;
    }

    private Player() {
        this.hp = 100;
    }

    public int getHp() {
        return hp;
    }

    public int getMoney() {
        return money;
    }

    public int getScore() {
        return score;
    }

    public void addGoldandScore(Monster m){
        money +=m.getCostForDestroying();
        score+=m.getScoreForDestroying();
    }

    public void receiveDamage(Monster monster) {
        hp-=monster.getDamage();
        if (hp<=0) {
            hp = 0;
        }
    }

    public boolean isMoneyEnough(int amount) {
        return money>=amount;
    }

    public void spendMoney (int amount) {
        money-=amount;
    }
}
