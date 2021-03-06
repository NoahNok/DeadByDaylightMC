package noahnok.DBDL.files.player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.game.playerStates.PlayerState;
import noahnok.DBDL.files.game.runnables.SpectatingRunnable;
import noahnok.DBDL.files.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DPlayer {

    private DeadByDaylight main;

    private UUID id;
    private String name;
    private Config dataFile;
    private PlayerStatus status = PlayerStatus.OUT_OF_GAME;

    private PlayerState playerState;

    private boolean spectating;

    private DPlayer spectate;

    private BukkitTask spectatingRunnable;

    private int gameScore;

    private DGame currentGame;

    //Config values to be loaded
    private int bloodPoints=0,score=0;
    private int escapes=0,deaths=0,timesSacrificed=0,generatorsFixed=0,generatorsMessedup=0,timesHooked=0,hookEscapes=0,hookPulloff=0,gatesOpened=0,hooksBroken=0,heals=0;

    private int timesSacrificing=0,successfulSacrifices=0,kills=0,losses=0,survivorPickups=0,survivorPickupEscapes=0,playerEscapes=0,wins=0;


    public DPlayer(UUID id, Config dataFile, DeadByDaylight main) {
        this.id = id;
        this.dataFile = dataFile;
        this.name = Bukkit.getServer().getPlayer(id).getName();
        this.main = main;
    }

    public Map<String, Integer> returnGenericData(){
        Map<String, Integer> values = new HashMap<String, Integer>();
        values.put("bloodPoints", bloodPoints);


        return values;
    }

    public DGame getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(DGame curretnGame) {
        this.currentGame = curretnGame;
    }

    public BukkitTask getSpectatingRunnable() {
        return spectatingRunnable;
    }


    public PlayerState getPlayerState() {
        return playerState;
    }

    public void resetPlayerState(){
        if (playerState != null) {
            main.getPlayerStateManager().survivorHealed(this, true);
            if (playerState.getBleedingRunnable() != null) {
                playerState.getBleedingRunnable().cancel();
                playerState.setBleedingRunnable(null);
            }
        }



        playerState = new PlayerState(this);
    }

    public void stopSpectating(){
        if (this.getSpectatingRunnable() != null){
            this.getSpectatingRunnable().cancel();
            this.spectatingRunnable = null;
        }
    }

    public boolean isSpectating() {
        return spectating;
    }

    public void startSpectating(DGame game){
        Player player = this.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        resetPlayerState();

        for (DPlayer dplayer : game.getHunted()) {
            if (dplayer != null && !dplayer.isDead() && !dplayer.isHunter()) {

                player.setSpectatorTarget(dplayer.getPlayer());
                this.setSpectating(true);
                spectate = dplayer;
                this.spectatingRunnable = new SpectatingRunnable(dplayer.getName(), this).runTaskTimer(main, 0, 80);
                return;
            }else{
                continue;
            }
        }

    }

    public void spectateNext(DPlayer player){
        if (this.getPlayer().getGameMode() != GameMode.SPECTATOR) this.getPlayer().setGameMode(GameMode.SPECTATOR);
        if (!this.isSpectating()) this.setSpectating(true);
        this.getPlayer().setSpectatorTarget(player.getPlayer());
        spectate = player;
        spectatingRunnable.cancel();
        this.spectatingRunnable = new SpectatingRunnable(player.getName(), this).runTaskTimer(main, 0, 80);


    }



    public DPlayer getSpectate() {
        return spectate;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public boolean isPlayerASurvivorAndAlive(){
        return status.isAlive();
    }

    public boolean isHunter(){
        switch (this.status){
            case HUNTER:
                return true;

            case CARRYING:
                return true;

            default:
                return false;
        }
    }

    public boolean isDead(){
        switch (this.status){
            case DEAD:
                return true;

            case ESCAPED:
                return true;

            case SACRIFICED:
                return true;

            default:
                return false;
        }
    }

    public Player getPlayer(){
        return Bukkit.getServer().getPlayer(id);
    }

    public void addToScore(int amount){
        gameScore += amount;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void addToStaticScore(int value){
        score += value;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void clearGameScore(){
        gameScore = 0;
    }

    public UUID getId() {
        return id;
    }

    public FileConfiguration getDataFile() {
        return dataFile.getConfig();
    }

    public Config grabConfig(){
        return dataFile;
    }

    public void kill(){
        id = null;
        name = null;
        dataFile = null;
    }

    public Map<String, Integer> returnHuntedData(){
        Map<String, Integer> values = new HashMap<String, Integer>();
        values.put("escapes", escapes);
        values.put("deaths", deaths);
        values.put("timesSacrificed", timesSacrificed);
        values.put("generatorsFixed", generatorsFixed);
        values.put("generatorsMessedup", generatorsMessedup);
        values.put("timesHooked", timesHooked);
        values.put("hookEscapes", hookEscapes);
        values.put("hookPulloff", hookPulloff);
        values.put("gatesOpened", gatesOpened);
        values.put("hooksBroken", hooksBroken);
        values.put("heals", heals);
        return values;
    }

    public Map<String, Integer> returnHunterData(){
        Map<String, Integer> values = new HashMap<String, Integer>();
        values.put("timesSacrificing", timesSacrificing);
        values.put("successfulSacrifices", successfulSacrifices);
        values.put("kills", kills);
        values.put("losses", losses);
        values.put("survivorPickups", survivorPickups);
        values.put("survivorPickupEscapes", survivorPickupEscapes);
        values.put("playerEscapes", playerEscapes);


        return values;
    }



    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public int getBloodPoints() {
        return bloodPoints;
    }

    public int getEscapes() {
        return escapes;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTimesSacrificed() {
        return timesSacrificed;
    }

    public int getGeneratorsFixed() {
        return generatorsFixed;
    }

    public int getGeneratorsMessedup() {
        return generatorsMessedup;
    }

    public int getTimesHooked() {
        return timesHooked;
    }

    public int getHookEscapes() {
        return hookEscapes;
    }

    public int getHookPulloff() {
        return hookPulloff;
    }

    public int getGatesOpened() {
        return gatesOpened;
    }

    public int getHooksBroken() {
        return hooksBroken;
    }

    public int getHeals() {
        return heals;
    }

    public int getTimesSacrificing() {
        return timesSacrificing;
    }

    public int getSuccessfulSacrifices() {
        return successfulSacrifices;
    }

    public int getKills() {
        return kills;
    }

    public int getLosses() {
        return losses;
    }

    public int getSurvivorPickups() {
        return survivorPickups;
    }

    public int getSurvivorPickupEscapes() {
        return survivorPickupEscapes;
    }

    public int getPlayerEscapes() {
        return playerEscapes;
    }

    public void setBloodPoints(int bloodPoints) {
        this.bloodPoints = bloodPoints;
    }

    public void setEscapes(int escapes) {
        this.escapes = escapes;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setTimesSacrificed(int timesSacrificed) {
        this.timesSacrificed = timesSacrificed;
    }

    public void setGeneratorsFixed(int generatorsFixed) {
        this.generatorsFixed = generatorsFixed;
    }

    public void setGeneratorsMessedup(int generatorsMessedup) {
        this.generatorsMessedup = generatorsMessedup;
    }

    public void setTimesHooked(int timesHooked) {
        this.timesHooked = timesHooked;
    }

    public void setHookEscapes(int hookEscapes) {
        this.hookEscapes = hookEscapes;
    }

    public void setHookPulloff(int hookPulloff) {
        this.hookPulloff = hookPulloff;
    }

    public void setGatesOpened(int gatesOpened) {
        this.gatesOpened = gatesOpened;
    }

    public void setHooksBroken(int hooksBroken) {
        this.hooksBroken = hooksBroken;
    }

    public void setHeals(int heals) {
        this.heals = heals;
    }

    public void setTimesSacrificing(int timesSacrificing) {
        this.timesSacrificing = timesSacrificing;
    }

    public void setSuccessfulSacrifices(int successfulSacrifices) {
        this.successfulSacrifices = successfulSacrifices;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setSurvivorPickups(int survivorPickups) {
        this.survivorPickups = survivorPickups;
    }

    public void setSurvivorPickupEscapes(int survivorPickupEscapes) {
        this.survivorPickupEscapes = survivorPickupEscapes;
    }

    public void setPlayerEscapes(int playerEscapes) {
        this.playerEscapes = playerEscapes;
    }


    public void setStatus(PlayerStatus status) {
        this.status = status;
    }


    //Messaging utils for player

    public void sendAB(String message){
        this.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
