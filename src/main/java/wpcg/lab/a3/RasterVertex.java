package wpcg.lab.a3;

/**
 * This vertex class can be used in the drawing/rasterization phase, where the
 * pixel coordinates (x,y) are given as int values, whereas the depth value z
 * is kept as a floating point number.
 */
public class RasterVertex {
    public int x;
    public int y;
    public float z;

    public RasterVertex(float x, float y, float z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = z;
    }

    public RasterVertex(int x, int y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
