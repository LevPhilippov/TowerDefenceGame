package lev.filippov;

public class Player {

    private int hp;
    private int money;
    private int score;
    private GameScreen gameScreen;
    private final String message1 = new String("Not enough money!");

    public Player(GameScreen gameScreen) {
        this.hp = 100;
        this.gameScreen = gameScreen;
        this.money = 300;
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

    public void addGoldandScoreForMonster(Monster m){
        money +=m.getCostForDestroying();
        score+=m.getScoreForDestroying();
    }

    public void addMoney(int amount) {
        money+=amount;
    }

    public void receiveDamage(Monster monster) {
        hp-=monster.getDamage();
        if (hp<=0) {
            hp = 0;
        }
    }

    public boolean isMoneyEnough(int amount) {
        if(money>=amount)
            return true;
        else{
          gameScreen.setTransparence(message1);
        }
        return false;
    }

    public void spendMoney (int amount) {
        money-=amount;
    }
}
