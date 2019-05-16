package io.iyyel.celestialoutbreak.ui.screen.component;

import io.iyyel.celestialoutbreak.controller.GameController;

import java.awt.*;

public final class Button extends AbstractComponent {

    private final String text;
    private Color fontColor;
    private final Rectangle rect;
    private final Font font;

    private final Point textPos;

    public Button(Point pos, Dimension dim, String text, Font font, Color fontColor, Color color, Point posOffset, Point textOffset, GameController gameController) {
        super(pos, dim, color, posOffset, gameController);

        this.text = text;
        this.fontColor = fontColor;
        this.font = font;

        this.textPos = new Point(super.pos.x + (super.dim.width / 2) - textOffset.x, super.pos.y + (super.dim.height / 2) - textOffset.y);
        rect = new Rectangle(super.pos.x, super.pos.y, super.dim.width, super.dim.height);
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(fontColor);
        g.setFont(font);
        drawScreenCenterString(text, textPos.y, font, g);
        g.setColor(color);
        g.draw(rect);
    }


    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

}