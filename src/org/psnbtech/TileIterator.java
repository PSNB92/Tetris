package org.psnbtech;

@FunctionalInterface
interface TileIterator {
    void iterate(int row, int col);
}