package noahnok.DBDL.files.player;

public enum PlayerStatus {
    OUT_OF_GAME(false),HUNTER(false),HUNTED(true),ESCAPED(true),DEAD(false),HOOKED(true),SACRIFICED(false),CARRYING(false);

    private boolean alive;

    PlayerStatus(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }
}
