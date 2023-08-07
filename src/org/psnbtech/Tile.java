public interface class Tile {
    protected Color baseColor;
    protected Color lightColor;
    protected Color darkColor;
    protected int spawnCol;
    protected int spawnRow;
    protected int dimension;
    protected int rows;
    protected int cols;
    protected boolean[][] tiles;

    // Aquí van los métodos getBaseColor(), getLightColor(), getDarkColor(), etc.
    /**
	 * Gets the base color of this type.
	 * @return The base color.
	 */
	public Color getBaseColor() {
		return baseColor;
	}
	
	/**
	 * Gets the light shading color of this type.
	 * @return The light color.
	 */
	public Color getLightColor() {
		return lightColor;
	}
	
	/**
	 * Gets the dark shading color of this type.
	 * @return The dark color.
	 */
	public Color getDarkColor() {
		return darkColor;
	}
	
	/**
	 * Gets the dimension of this type.
	 * @return The dimension.
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Gets the spawn column of this type.
	 * @return The spawn column.
	 */
	public int getSpawnColumn() {
		return spawnCol;
	}
	
	/**
	 * Gets the spawn row of this type.
	 * @return The spawn row.
	 */
	public int getSpawnRow() {
		return spawnRow;
	}
	
	/**
	 * Gets the number of rows in this piece. (Only valid when rotation is 0 or 2,
	 * but it's fine since this is only used for the preview which uses rotation 0).
	 * @return The number of rows.
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Gets the number of columns in this piece. (Only valid when rotation is 0 or 2,
	 * but it's fine since this is only used for the preview which uses rotation 0).
	 * @return The number of columns.
	 */
	public int getCols() {
		return cols;
	}
	
	/**
	 * Checks to see if the given coordinates and rotation contain a tile.
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @param rotation The rotation to check in.
	 * @return Whether or not a tile resides there.
	 */
	public boolean isTile(int x, int y, int rotation) {
		return tiles[rotation][y * dimension + x];
	}
	
	/**
	 * The left inset is represented by the number of empty columns on the left
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The left inset.
	 */
	public int getLeftInset(int rotation) {
		/*
		 * Loop through from left to right until we find a tile then return
		 * the column.
		 */
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The right inset is represented by the number of empty columns on the left
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The right inset.
	 */
	public int getRightInset(int rotation) {
		/*
		 * Loop through from right to left until we find a tile then return
		 * the column.
		 */
		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The left inset is represented by the number of empty rows on the top
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The top inset.
	 */
	public int getTopInset(int rotation) {
		/*
		 * Loop through from top to bottom until we find a tile then return
		 * the row.
		 */
		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}
	
	/**
	 * The botom inset is represented by the number of empty rows on the bottom
	 * side of the array for the given rotation.
	 * @param rotation The rotation.
	 * @return The bottom inset.
	 */
	public int getBottomInset(int rotation) {
		/*
		 * Loop through from bottom to top until we find a tile then return
		 * the row.
		 */
		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}
}
