package wpcg.base.mesh;

import com.google.common.base.Preconditions;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

/**
 * Tools for triangle meshes
 */
public class JMonkeyTools extends TriangleMeshTools {

    public enum Shading {
        PER_VERTEX, PER_FACET
    }

    private static PhongMaterialProps defaultPhongMatProps = new PhongMaterialProps(
            ColorRGBA.DarkGray, ColorRGBA.White, ColorRGBA.DarkGray, 20.0f);

    public static Material makePhongMaterial(AssetManager assetManager, PhongMaterialProps phongMaterialProps) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", phongMaterialProps.diffuseColor);
        mat.setColor("Ambient", phongMaterialProps.ambientColor);
        mat.setFloat("Shininess", phongMaterialProps.shininess);
        mat.setColor("Specular", phongMaterialProps.specularColor);
        mat.setBoolean("UseMaterialColors", true);
        mat.setBoolean("UseVertexColor", true);
        return mat;
    }

    public static Material makeMaterialUnshaded(AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.DarkGray);
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return mat;
    }

    /**
     * Generate a material for the given texture.
     */
    public static Material makeTextureMaterial(AssetManager assetManager, Texture texture) {
        Preconditions.checkNotNull(texture);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        return mat;
    }

    public static Geometry createGeometry(AssetManager assetManager, TriangleMesh triangleMesh) {
        return createGeometry(assetManager, triangleMesh, new PhongMaterialProps(), null, Shading.PER_FACET);
    }

    /**
     * Create a geometry object for a triangle mesh.
     */
    public static Geometry createGeometry(AssetManager assetManager, TriangleMesh triangleMesh,
                                          PhongMaterialProps phongMaterialProps,
                                          String normalMapFilename,
                                          Shading shading) {
        Mesh mesh = createMesh(triangleMesh, shading);
        Geometry geom = new Geometry("triangle mesh", mesh);
        if (phongMaterialProps == null) {
            phongMaterialProps = defaultPhongMatProps;
        }

        Material mat = makePhongMaterial(assetManager, phongMaterialProps);

        // Normal map
        if (normalMapFilename != null) {
            TangentBinormalGenerator.generate(geom);
            Texture normalMap = assetManager.loadTexture(normalMapFilename);
            normalMap.setMagFilter(Texture.MagFilter.Bilinear);
            normalMap.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
            mat.setTexture("NormalMap", normalMap);
        }

        geom.setMaterial(mat);

        return geom;
    }

    public static Mesh createMesh(TriangleMesh triangleMesh, Shading shading) {
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Triangles);

        int size = triangleMesh.getNumberOfTriangles() * 3;

        float[] positionBuffer = new float[size * 3];
        float[] colorBuffer = new float[size * 4];
        float[] normalBuffer = new float[size * 3];
        float[] texCoordsBuffer = new float[size * 2];
        int[] indexBuffer = new int[size];

        // Fill vertex and color buffer
        for (int triangleIndex = 0; triangleIndex < triangleMesh.getNumberOfTriangles(); triangleIndex++) {

            Triangle t = triangleMesh.getTriangle(triangleIndex);
            ColorRGBA color = ColorRGBA.Orange;
            Vector3f normal = t.getNormal();

            for (int vertexInTriangleIndex = 0; vertexInTriangleIndex < 3; vertexInTriangleIndex++) {
                int vertexIndex = t.getVertexIndex(vertexInTriangleIndex);
                int texCoordIndex = t.getTextureCoordinate(vertexInTriangleIndex);
                Vertex vertex = triangleMesh.getVertex(vertexIndex);
                Vector3f pos = vertex.getPosition();
                if (shading == Shading.PER_VERTEX) {
                    normal = vertex.getNormal();
                }

                Vector2f texCoord = texCoordIndex >= 0 ? triangleMesh.getTextureCoordinate(texCoordIndex) :
                        new Vector2f(0, 0);

                // Position
                positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = pos.get(0);
                positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = pos.get(1);
                positionBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = pos.get(2);

                // Normal
                normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3] = normal.get(0);
                normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 1] = normal.get(1);
                normalBuffer[triangleIndex * 9 + vertexInTriangleIndex * 3 + 2] = normal.get(2);

                // Color
                colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4] = color.r;
                colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 1] = color.g;
                colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 2] = color.b;
                colorBuffer[triangleIndex * 12 + vertexInTriangleIndex * 4 + 3] = color.a;

                // Texture coords
                texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2] = texCoord.x;
                texCoordsBuffer[triangleIndex * 6 + vertexInTriangleIndex * 2 + 1] = texCoord.y;
            }
        }
        for (int i = 0; i < size; i++) {
            indexBuffer[i] = i;
        }

        mesh.setBuffer(Type.Position, 3, positionBuffer);
        mesh.setBuffer(Type.Index, 1, indexBuffer);
        mesh.setBuffer(Type.Color, 4, colorBuffer);
        mesh.setBuffer(Type.Normal, 3, normalBuffer);
        mesh.setBuffer(Type.TexCoord, 2, texCoordsBuffer);
        mesh.updateBound();
        return mesh;
    }


}
