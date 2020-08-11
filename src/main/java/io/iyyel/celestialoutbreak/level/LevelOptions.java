package io.iyyel.celestialoutbreak.level;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.handler.FileHandler;
import io.iyyel.celestialoutbreak.handler.LogHandler;
import io.iyyel.celestialoutbreak.handler.TextHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.awt.*;
import java.util.Map;

public final class LevelOptions {

    private final TextHandler textHandler = TextHandler.getInstance();
    private final FileHandler fileHandler = FileHandler.getInstance();
    private final LogHandler logHandler = LogHandler.getInstance();

    /*
     * Level options.
     */
    private String levelName;
    private String levelDesc;
    private int levelPlayerLife;
    private String levelSoundFileName;
    private Color levelColor;

    /*
     * Power up options.
     */
    private Dimension powerUpDim;
    private int powerUpSpeed;
    private int powerUpChance;
    private float powerUpLum;
    private float powerUpSat;

    /*
     * Paddle options.
     */
    private Point paddlePos;
    private Dimension paddleDim;
    private int paddleSpeed;
    private Color paddleColor;

    /*
     * Ball options.
     */
    private Point ballPos;
    private Dimension ballDim;
    private int ballSpeed;
    private Color ballColor;

    /*
     * BlockList options.
     */
    private Point blockPosStart;
    private Point blockPosSpacing;
    private int blockAmount;
    private int blockHitPoints;
    private Dimension blockDim;
    private float blockLum;
    private float blockSat;

    /*
     * GamePanel options.
     */
    private Color gamePanelTitleColor;
    private Color gamePanelValueColor;

    /*
     * Required objects.
     */
    private final GameController gameController;

    /*
     * Constructor.
     */
    public LevelOptions(String fileName, GameController gameController) {
        this.gameController = gameController;

        try {
            parseLevelOptions(fileName);
        } catch (Exception e) {
            /*
             * If the level options aren't able to be parsed correctly, any exception that will be
             * thrown will be printed to the console as well as the current log file, and then the
             * gameController will be stopped, since invalid level options has been found which might cause
             * the application to be in an inconsistent state.
             */
            logHandler.log(textHandler.errorParsingPropertiesMsg(fileName, ExceptionUtils.getStackTrace(e)), LogHandler.LogLevel.ERROR, false);
            gameController.stop();
        }
    }

    private void parseLevelOptions(String fileName) {
        Map<String, String> map = fileHandler.readPropertiesFromFile(fileName);

        /* Level options */
        levelName = map.get(textHandler.PROP_KEY_LEVEL_NAME);
        levelDesc = map.get(textHandler.PROP_KEY_LEVEL_DESC);
        levelPlayerLife = Integer.parseInt(map.get(textHandler.PROP_KEY_LEVEL_PLAYER_LIFE));
        levelSoundFileName = map.get(textHandler.PROP_KEY_LEVEL_SOUND_FILE_NAME);
        int levelColorValue = Integer.decode(map.get(textHandler.PROP_KEY_LEVEL_COLOR));
        levelColor = new Color(levelColorValue);

        /* Power up options */
        int powerUpWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_WIDTH));
        int powerUpHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_HEIGHT));
        powerUpDim = new Dimension(powerUpWidth, powerUpHeight);
        powerUpSpeed = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_SPEED));
        powerUpChance = Integer.parseInt(map.get(textHandler.PROP_KEY_POWERUP_CHANCE));
        powerUpLum = Float.parseFloat(map.get(textHandler.PROP_KEY_POWERUP_LUMINANCE));
        powerUpSat = Float.parseFloat(map.get(textHandler.PROP_KEY_POWERUP_SATURATION));

        /* Paddle options */
        int paddlePosXOffset = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_POS_X_OFFSET));
        int paddlePosYOffset = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_POS_Y_OFFSET));
        paddlePos = new Point((gameController.getWidth() / 2) - paddlePosXOffset, gameController.getHeight() - paddlePosYOffset);
        int paddleWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_WIDTH));
        int paddleHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_HEIGHT));
        paddleDim = new Dimension(paddleWidth, paddleHeight);
        paddleSpeed = Integer.parseInt(map.get(textHandler.PROP_KEY_PADDLE_SPEED));
        int paddleCol = Integer.decode(map.get(textHandler.PROP_KEY_PADDLE_COLOR));
        paddleColor = new Color(paddleCol);

        /* Ball options */
        int ballWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_BALL_WIDTH));
        int ballHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_BALL_HEIGHT));
        ballDim = new Dimension(ballWidth, ballHeight);
        ballPos = new Point(paddlePos.x + (paddleWidth / 2) - (ballWidth / 2), paddlePos.y - (ballHeight));
        ballSpeed = Integer.parseInt(map.get(textHandler.PROP_KEY_BALL_SPEED));
        int ballColorHex = Integer.decode(map.get(textHandler.PROP_KEY_BALL_COLOR));
        ballColor = new Color(ballColorHex);

        /* Block options */
        int blockPosXStart = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_X_START));
        int blockPosYStart = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_Y_START));
        blockPosStart = new Point(blockPosXStart, blockPosYStart);
        int blockPosXSpacing = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_X_SPACING));
        int blockPosYSpacing = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_POS_Y_SPACING));
        blockPosSpacing = new Point(blockPosXSpacing, blockPosYSpacing);
        blockAmount = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_AMOUNT));
        blockHitPoints = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_HEALTH));
        int blockWidth = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_WIDTH));
        int blockHeight = Integer.parseInt(map.get(textHandler.PROP_KEY_BLOCK_HEIGHT));
        blockDim = new Dimension(blockWidth, blockHeight);
        blockLum = Float.parseFloat(map.get(textHandler.PROP_KEY_BLOCK_LUMINANCE));
        blockSat = Float.parseFloat(map.get(textHandler.PROP_KEY_BLOCK_SATURATION));

        /* GamePanel options */
        int gamePanelTitleColorInt = Integer.decode(map.get(textHandler.PROP_KEY_GAME_PANEL_TITLE_COLOR));
        int gamePanelValueColorInt = Integer.decode(map.get(textHandler.PROP_KEY_GAME_PANEL_VALUE_COLOR));
        gamePanelTitleColor = new Color(gamePanelTitleColorInt);
        gamePanelValueColor = new Color(gamePanelValueColorInt);
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public int getLevelPlayerLife() {
        return levelPlayerLife;
    }

    public String getLevelSoundFileName() {
        return levelSoundFileName;
    }

    public Color getLevelColor() {
        return levelColor;
    }

    public Dimension getPowerUpDim() {
        return powerUpDim;
    }

    public int getPowerUpSpeed() {
        return powerUpSpeed;
    }

    public int getPowerUpChance() {
        return powerUpChance;
    }

    public float getPowerUpLum() {
        return powerUpLum;
    }

    public float getPowerUpSat() {
        return powerUpSat;
    }

    public Point getPaddlePos() {
        return paddlePos;
    }

    public Dimension getPaddleDim() {
        return paddleDim;
    }

    public int getPaddleSpeed() {
        return paddleSpeed;
    }

    public Color getPaddleColor() {
        return paddleColor;
    }

    public Point getBallPos() {
        return ballPos;
    }

    public Dimension getBallDim() {
        return ballDim;
    }

    public int getBallSpeed() {
        return ballSpeed;
    }

    public Color getBallColor() {
        return ballColor;
    }

    public Point getBlockPosStart() {
        return blockPosStart;
    }

    public Point getBlockPosSpacing() {
        return blockPosSpacing;
    }

    public int getBlockAmount() {
        return blockAmount;
    }

    public int getBlockHitPoints() {
        return blockHitPoints;
    }

    public Dimension getBlockDim() {
        return blockDim;
    }

    public float getBlockLum() {
        return blockLum;
    }

    public float getBlockSat() {
        return blockSat;
    }

    public Color getGamePanelTitleColor() {
        return gamePanelTitleColor;
    }

    public Color getGamePanelValueColor() {
        return gamePanelValueColor;
    }

}