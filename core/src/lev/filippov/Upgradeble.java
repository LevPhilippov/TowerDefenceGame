package lev.filippov;

public interface Upgradeble {

    TurretType upgrade();

    default boolean isPossibleToUpgrade() {
        return upgrade() != null;
    }
}
