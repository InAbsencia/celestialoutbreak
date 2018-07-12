package io.iyyel.celestialoutbreak.entity;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.handler.FileHandler;
import io.iyyel.celestialoutbreak.handler.SoundHandler;
import io.iyyel.celestialoutbreak.handler.TextHandler;
import io.iyyel.celestialoutbreak.utils.Utils;

import java.awt.*;
import java.util.Random;

/**
 * The Ball is a MobileEntity that the @Paddle is used
 * to collide with. The Ball inherits from MobileEntity
 * because the Ball is going to move around the screen.
 * <p>
 * Ball is the entity that handles collisions. Since
 * we are talking about rather simple collision logic here,
 * this is doable. A better design could probably consist of a
 * collision controller or something like that.
 * <p>
 * Velocity is used to update the speed of the Ball, attempting
 * to give it a natural physical feel.
 */
public final class Ball extends MobileEntity {

    private final Utils utils = Utils.getInstance();
    private final TextHandler textHandler = TextHandler.getInstance();
    private final SoundHandler soundHandler = SoundHandler.getInstance();
    private final FileHandler fileHandler = FileHandler.getInstance();
    private final GameController gameController;

    private final Random random = new Random();
    private Point velocity;

    private final int PADDLE_COLLISION_TIMER_INITIAL = 20;
    private final int BALL_STALE_TIMER_INITIAL = 40;

    private int paddleCollisionTimer = 0;
    private int ballStaleTimer = BALL_STALE_TIMER_INITIAL;
    private int ballPosXOffset;
    private int ballPosYOffset;

    /**
     * Default constructor.
     *
     * @param pos            Current position of the Ball. (x, y)
     * @param width          Width of the Ball. (pixels)
     * @param height         Height of the Ball. (pixels)
     * @param color          Color of the Ball.
     * @param speed          Speed of the Ball.
     * @param ballPosXOffset Ball x-axis offset.
     * @param ballPosYOffset Ball y-axis offset.
     * @param gameController Current game instance.
     */
    public Ball(Point pos, int width, int height, Color color, int speed,
                int ballPosXOffset, int ballPosYOffset, GameController gameController) {
        super(pos, width, height, color, speed, gameController);
        this.ballPosXOffset = ballPosXOffset;
        this.ballPosYOffset = ballPosYOffset;
        this.gameController = gameController;

        velocity = new Point(speed, speed);
    }

    /**
     * This is the update method of the Ball.
     * It should be run roughly every 30, 60, 120 times a second.
     * <p>
     * The method checks for collision between the Blocks inside the BlockList
     * and the Ball, as well as checking whether the Ball has gone out of the screen
     * or not.
     *
     * @param paddle    The current Paddle of the game.
     * @param blockList The current BlockList of the game.
     */
    public void update(Paddle paddle, BlockList blockList) {
        if (ballStaleTimer == 0) {
            pos.x += velocity.x;
            pos.y += velocity.y;
        } else {
            ballStaleTimer--;
        }

        /* Check for collision. */
        checkCollision(paddle);
        checkCollision(blockList);

        /* Ball hit left x-axis. */
        if (pos.x < 0) {
            if (utils.isVerboseLogEnabled()) {
                fileHandler.writeLog(textHandler.vBallTouchedXAxisLeftMsg);
            }
            velocity.x = speed;
            soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BALL_HIT).play(false);
        }

        /* Ball hit right x-axis. */
        if (pos.x > (gameController.getWidth() - width)) {
            if (utils.isVerboseLogEnabled()) {
                fileHandler.writeLog(textHandler.vBallTouchedXAxisRightMsg);
            }
            velocity.x = -speed;
            soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BALL_HIT).play(false);
        }

        /* Ball hit top y-axis. */
        if (pos.y < 0) {
            if (utils.isVerboseLogEnabled()) {
                fileHandler.writeLog(textHandler.vBallTouchedYAxisTopMsg);
            }
            velocity.y = speed;
            soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BALL_HIT).play(false);
        }

        /* Ball hit bottom y-axis. */
        if (pos.y > (gameController.getHeight() - height)) {
            if (utils.isVerboseLogEnabled()) {
                fileHandler.writeLog(textHandler.vBallTouchedYAxisBottomMsg);
            }

            pos = new Point((gameController.getWidth() / 2) - ballPosXOffset, (gameController.getHeight() / 2) - ballPosYOffset);

            boolean isPositiveValue = random.nextBoolean();
            int ballSpeedDecrement = random.nextInt(speed);

            velocity.x = (isPositiveValue ? 1 : -1) * speed + (isPositiveValue ? -ballSpeedDecrement : ballSpeedDecrement);
            velocity.y = speed;

            soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BALL_RESET).play(false);
            ballStaleTimer = BALL_STALE_TIMER_INITIAL;
        }
    }

    /**
     * This is a generic method used to check if the ball collides with t.
     * t can either be a Paddle or a BlockList.
     * <p>
     * TODO: This can be cleaned up. Instead of using the actual BlockList, make it use
     * TODO: each individual block. That would make more logical sense, perhaps.
     * TODO: Perhaps find a better way to do this.
     *
     * @param t   Object to check collision with.
     * @param <T> Generic type.
     */
    private <T> void checkCollision(T t) {
        if (t instanceof Paddle && ((Paddle) t).getBounds().intersects(getBounds())) {
            if (paddleCollisionTimer == 0) {
                velocity.y *= -1;

                if (velocity.x < 0) {
                    velocity.x = -speed;
                } else {
                    velocity.x = speed;
                }

                paddleCollisionTimer = PADDLE_COLLISION_TIMER_INITIAL;

                soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BALL_HIT).play(false);

                if (utils.isVerboseLogEnabled()) {
                    fileHandler.writeLog(textHandler.vBallPaddleCollisionMsg(paddleCollisionTimer));
                }
            }
        } else if (t instanceof BlockList) {

            /* Cast t to a BlockList. */
            BlockList blockList = ((BlockList) t);

            for (int i = 0; i < blockList.getLength(); i++) {
                if (blockList.getBlock(i) != null && blockList.getBlock(i).getBounds().intersects(getBounds())) {
                    velocity.y *= -1;
                    blockList.destroyBlock(i);

                    soundHandler.getSoundClip(textHandler.SOUND_FILE_NAME_BALL_HIT).play(false);

                    if (utils.isVerboseLogEnabled()) {
                        fileHandler.writeLog(textHandler.vBallBlockListCollisionMsg(i));
                    }
                }
            }
        }
    }

    /**
     * Draws the Ball onto the screen.
     *
     * @param g Graphics object used to render this Entity.
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillOval(pos.x, pos.y, width, height);

        if (paddleCollisionTimer > 0) {
            paddleCollisionTimer--;
        }
    }

    /**
     * This is used to check if the Ball collides
     * with the Block.
     *
     * @return Rectangle using the Ball's bounds.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle(pos.x, pos.y, width, height);
    }

}