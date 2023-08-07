public interface ITetrisTile {
    Color getBaseColor();
    Color getLightColor();
    Color getDarkColor();
    int getDimension();
    int getSpawnColumn();
    int getSpawnRow();
    int getLeftInset(int rotation);
    int getRightInset(int rotation);
    int getTopInset(int rotation);
    int getBottomInset(int rotation);
    boolean isTile(int x, int y, int rotation);
}
