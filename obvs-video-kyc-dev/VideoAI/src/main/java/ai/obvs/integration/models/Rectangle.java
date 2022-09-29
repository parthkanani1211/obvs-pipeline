package ai.obvs.integration.models;

public class Rectangle {
  int top;
  int left;
  int width;
  int height;

  public Rectangle(){

  }
  public Rectangle(int left, int top, int width, int height) {
    this.top = top;
    this.left = left;
    this.width= width;
    this.height = height;
  }

  public int getTop() {
    return top;
  }

  public int getLeft() {
    return left;
  }

  public int getWidth() { return width; }

  public int getHeight() { return height; }

  @Override
  public String toString() {
    return "Rectangle{" +
        "top=" + top +
        ", left=" + left +
        ", width=" + width +
        ", height=" + height +
        '}';
  }
}
