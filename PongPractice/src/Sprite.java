import java.awt.Color;
import java.awt.Rectangle;

public class Sprite {
	
	private int xPosition, yPosition;
	private int xVelocity, yVelocity;
	
	private int width, height;
	private Color colour;
	
	private int initialXPosition, initialYPosition;
	
	public int getXPosition() {
		return xPosition;
	}
	
	public void setXPosition(int newX) {
		this.xPosition = newX;
	}
	
	public void setXPosition(int newX, int panelWidth) {
		xPosition = newX;
		if (xPosition < 0) {
			xPosition = 0;
		}
		else if (xPosition + width > panelWidth) {
			xPosition = panelWidth - width;
		}
	}
	
	public int getYPosition() {
		return yPosition;
	}
	
	public void setYPosition(int newY) {
		this.yPosition = newY;
	}
	
	public void setYPosition(int newY, int panelHeight) {
		yPosition = newY;
		if (yPosition < 0) {
			yPosition = 0;
		}
		else if (yPosition + height > panelHeight) {
			yPosition = panelHeight - height;
		}
	}
	
	public int getXVelocity() {
		return xVelocity;
	}
	
	public void setXVelocity(int newXVelocity) {
		this.xVelocity = newXVelocity;
	}
	
	public int getYVelocity() {
		return yVelocity;
	}
	
	public void setYVelocity(int newYVelocity) {
		this.yVelocity =  newYVelocity;
	}
	
	
	public void setInitialPosition(int initialX, int initialY) {
		this.initialXPosition = initialX;
		this.initialYPosition = initialY;
	}
	
	public void resetToInitialPosition() {
		setXPosition(initialXPosition);
		setYPosition(initialYPosition);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int newWidth) {
		this.width = newWidth;
	}
	
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public void setColour(Color newColour) {
		this.colour = newColour;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(getXPosition(), getYPosition(), getWidth(), getHeight());
	}
	
	
	
}
