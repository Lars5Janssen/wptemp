/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a3;

import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;

/**
 * Represents a virtual camera with extrinsic and intrinsic camera parameters
 */
public class Camera {

    /**
     * Camera eye point
     */
    private Vector3f eye;

    /**
     * Reference point to look to
     */
    private Vector3f ref;

    /**
     * Camera up vector
     */
    private Vector3f up;

    /**
     * Field of view along the horizontal axis.
     */
    private float fovx;

    /**
     * Width of the screen (horizontal resolution)
     */
    private int width;

    /**
     * Height of the screen (vertical resolution)
     */
    private int height;

    /**
     * Distance to image plane.
     */
    private float z0;

    public Camera(Vector3f eye, Vector3f ref, Vector3f up, float fovx, float z0, int width, int height) {
        this.eye = eye;
        this.ref = ref;
        this.up = up;
        this.fovx = fovx;
        this.z0 = z0;
        this.width = width;
        this.height = height;
    }

    /**
     * Rotate the eye horizontally around the up-vector centered at the reference point.
     */
    public void rotateHorizontal(float dx) {
        eye = eye.subtract(ref);
        float angle = dx * 0.01f;
        Matrix3f R = new Matrix3f();
        R.fromAngleAxis(angle, up);
        eye = R.mult(eye);
        eye = eye.add(ref);
    }

    /**
     * Rotate the eye vertically around the left vector centered at the reference point.
     */
    public void rotateVertical(float dy) {
        Vector3f left = ref.subtract(eye).cross(up).normalize();
        eye = eye.subtract(ref);
        float angle = dy * 0.01f;
        Matrix3f R = new Matrix3f();
        R.fromAngleAxis(angle, left);
        eye = R.mult(eye);
        eye = eye.add(ref);
        up = left.cross(ref.subtract(eye.normalize()));
    }


    public Vector3f getEye() {
        return eye;
    }

    public Vector3f getRef() {
        return ref;
    }

    public Vector3f getUp() {
        return up;
    }

    public float getFovX() {
        return fovx;
    }

    public float getFovY() {
        return fovx / getAspectRatio();
    }

    private float getAspectRatio() {
        return (float) getWidth() / (float) getHeight();
    }

    public float getZ0() {
        return z0;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
