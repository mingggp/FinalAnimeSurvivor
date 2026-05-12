package entityInterface;

public interface Updatable {
    void update(double accumulateDeltaTime);
    boolean isExpired();
}
