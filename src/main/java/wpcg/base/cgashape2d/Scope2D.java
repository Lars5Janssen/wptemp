/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d;

import com.google.common.base.Preconditions;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import wpcg.base.math.Matrices;

/**
 * Represents the local coordinate system of a shape.
 */
public class Scope2D {

  /**
   * Homogenious matrix for the rotation + translation in 2D.
   */
  private Matrix3f coordSys;

  public Scope2D() {
    coordSys = Matrix3f.IDENTITY;
  }

  public Scope2D(Matrix3f coordSys) {
    Preconditions.checkNotNull(coordSys);
    this.coordSys = coordSys;
  }

  public Scope2D(Scope2D scope) {
    coordSys = new Matrix3f(scope.coordSys);
  }

  public Matrix3f getCoordSys() {
    return coordSys;
  }

  /**
   * Generate a copy of the scope including a translation.
   */
  public Scope2D createTranslatedCopy(Vector2f t) {
    Scope2D newScope = new Scope2D(this);
    newScope.coordSys = Matrices.createTranslation(t).mult(coordSys);
    return newScope;
  }
}
