//package lev.filippov.junk;
//
//import lev.filippov.GameScreen;
//
//public class Player {
//    private int hp;
//    private int money;
//    private int score;
//    private GameScreen gameScreen;
//    private int roundProgress;
//
//    public Player(GameScreen gameScreen) {
//        this.hp = 100;
//        this.gameScreen = gameScreen;
//        this.money = 300;
//        this.roundProgress = 1;
//    }
//
//    public int getHp() {
//        return hp;
//    }
//
//    public int getMoney() {
//        return money;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//
//    public void addScore(int scorePoints){
//        score+=scorePoints;
//    }
//
//    public void addMoney(int amount) {
//        money+=amount;
//    }
//
//    public void receiveDamage(int damage) {
//        hp-=damage;
//        if (hp<=0) {
//            hp = 0;
//        }
//    }
//
//    public boolean isMoneyEnough(int amount) {
//        if(money>=amount)
//            return true;
//        else{
//          gameScreen.getInfoEmitter().setup(gameScreen.getSelectedCellX(),gameScreen.getSelectedCellY(), "Not enough money!");
//        }
//        return false;
//    }
//
//    public void spendMoney (int amount) {
//        money-=amount;
//    }
//
//    public int getRoundProgress() {
//        return roundProgress;
//    }
//
//    public void roundCompleted() {
//        roundProgress++;//это HP, SCORE, MONEY хорошо бы хранить в файле, но нет времени заниматься этим сейчас
//        gameScreen.showWinScreen();
//    }
//}
