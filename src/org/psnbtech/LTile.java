public class TileTypeI implements Tile {
    public TileTypeI() {
        this.baseColor = new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX);
        this.lightColor = baseColor.brighter();
        this.darkColor = baseColor.darker();
        this.dimension = 4;
        this.tiles = new boolean[][] {
            {
                false, false, false, false,
                true,  true,  true,  true,
                false, false, false, false,
                false, false, false, false,
            },
            {
                false, false, true, false,
                false, false, true, false,
                false, false, true, false,
                false, false, true, false,
            },
            {
                false, false, false, false,
                false, false, false, false,
                true,  true,  true,  true,
                false, false, false, false,
            },
            {
                false, true, false, false,
                false, true, false, false,
                false, true, false, false,
                false, true, false, false,
            }
        };
        this.cols = 4;
        this.rows = 4;
        this.spawnCol = 5 - (dimension >> 1);
        this.spawnRow = getTopInset(0);
    }

    
}
