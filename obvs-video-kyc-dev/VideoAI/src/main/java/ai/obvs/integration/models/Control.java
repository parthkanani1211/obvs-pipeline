package ai.obvs.integration.models;

public class Control {
  String name;
  double confidence;
  Rectangle box;
  String Value;

  public Control() {
    box = new Rectangle(0,0,0,0);
    confidence = 0.0;
  }

  public Control(String name, double confidence, Rectangle box) {
    this.name = name;
    this.confidence = confidence;
    this.box = box;
  }

  public String getName() {
    return name;
  }

  public double getConfidence() {
    return confidence;
  }

  public Rectangle getBox() {
    return box;
  }

  public String getValue() {
    return Value;
  }

  public void setValue(String value) {
    Value = value;
  }

  @Override
  public String toString() {
    return "Control{" +
        "name='" + name + '\'' +
        ", confidence=" + confidence +
        ", box=" + box +
        ", Value='" + Value + '\'' +
        '}';
  }
}
