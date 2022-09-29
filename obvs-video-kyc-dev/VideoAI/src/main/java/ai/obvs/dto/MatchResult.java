package ai.obvs.dto;

public class MatchResult {
    private String valueToMatchWith;
    private boolean result;

    public MatchResult(String valueToMatchWith, boolean result) {
        this.valueToMatchWith = valueToMatchWith;
        this.result = result;
    }

    public String getValueToMatchWith() {
        return valueToMatchWith;
    }

    public boolean isResult() {
        return result;
    }
}
