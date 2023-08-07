public class TileTypeIFactory implements TileFactory {
    @Override
    public TetrisTile createTile() {
        return new TileTypeI();
    }
}