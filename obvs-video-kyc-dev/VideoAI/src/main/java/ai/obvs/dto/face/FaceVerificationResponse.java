package ai.obvs.dto.face;

public class FaceVerificationResponse {
    private boolean isLive;
    private boolean isMatch;

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }
}
