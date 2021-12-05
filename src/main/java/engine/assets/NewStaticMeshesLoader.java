package engine.assets;

import engine.io.Utils;
import gameData.Stages.GameStage.GameStage;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.lwjgl.assimp.Assimp.*;

public class NewStaticMeshesLoader {

    public static NewMesh[] load(String resourcePath, String texturesDir) throws Exception {
        return load(resourcePath, texturesDir,
                aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate
                        | aiProcess_FixInfacingNormals | aiProcess_PreTransformVertices);
    }

    public static NewMesh[] load(String resourcePath, String texturesDir, int flags) throws Exception {
//        System.out.println(resourcePath);
        resourcePath = getPath(resourcePath);
        texturesDir = getPath(texturesDir);

//        System.out.println(resourcePath);
        if(resourcePath.contains("/C:")) resourcePath = resourcePath.replace("/C:","C:");
        if(texturesDir.contains("/C:")) texturesDir = texturesDir.replace("/C:","C:");
//        System.out.println(resourcePath);

        AIScene aiScene = aiImportFile(resourcePath, flags);
        if (aiScene == null) {
            throw new Exception("Error loading model");
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<NewMaterial> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials, texturesDir);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        NewMesh[] meshes = new NewMesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            NewMesh mesh = processMesh(aiMesh, materials);
            meshes[i] = mesh;
        }

        return meshes;
    }

    private static String getPath(String name) {

        String bresourcePath = NewStaticMeshesLoader.class.getClassLoader().getResource(name).getPath();
        if(bresourcePath.contains(".jar!")) {
            bresourcePath = bresourcePath.substring(bresourcePath.indexOf('/'));
            String path = bresourcePath;
            String path1 = path.substring(0, path.lastIndexOf(".jar!"));
            String path2 = path.substring(path.lastIndexOf(".jar!"));
            path1 = path1.substring(0, path1.lastIndexOf('/'));
            path2 = path2.substring(path2.indexOf('/'));
            bresourcePath = path1 + path2;
        }
        return bresourcePath;
    }

    protected static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }

    protected static void processMaterial(AIMaterial aiMaterial, List<NewMaterial> materials,
            String texturesDir) throws Exception {

        AIColor4D colour = AIColor4D.create();

        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null,
                null, null, null, null, null);
        String textPath = path.dataString();
        Texture texture = null;
        if (textPath != null && textPath.length() > 0) {
            TextureCache textCache = TextureCache.getInstance();
            String textureFile = "";
			if ( texturesDir != null && texturesDir.length() > 0 ) {
				textureFile += texturesDir + "/";
			}
			textureFile += textPath;
            textureFile = textureFile.replace("//", "/");
            texture = textCache.getTexture(textureFile);
        }

        Vector4f ambient = NewMaterial.DEFAULT_COLOUR;
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0,
                colour);
        if (result == 0) {
            ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f diffuse = NewMaterial.DEFAULT_COLOUR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0,
                colour);
        if (result == 0) {
            diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4f specular = NewMaterial.DEFAULT_COLOUR;
        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0,
                colour);
        if (result == 0) {
            specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
        }

        NewMaterial material = new NewMaterial(ambient, diffuse, texture, 1.0f);
        material.setSpecularColour(specular);
//        material.setTexture(texture);
        materials.add(material);
    }

    private static NewMesh processMesh(AIMesh aiMesh, List<NewMaterial> materials) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        // Texture coordinates may not have been populated. We need at least the empty slots
        if ( textures.size() == 0) {
            int numElements = (vertices.size() / 3) * 2;
            for (int i=0; i<numElements; i++) {
                textures.add(0.0f);
            }
        }

        NewMesh mesh = new NewMesh(Utils.listToArray(vertices), Utils.listToArray(textures),
                Utils.listToArray(normals), Utils.listIntToArray(indices));
        NewMaterial material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new NewMaterial();
        }
        mesh.setMaterial(material);

        return mesh;
    }

    protected static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        while (aiNormals != null && aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }

    protected static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
        AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
        int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
        for (int i = 0; i < numTextCoords; i++) {
            AIVector3D textCoord = textCoords.get();
            textures.add(textCoord.x());
            textures.add(1 - textCoord.y());
        }
    }

    protected static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }
}
