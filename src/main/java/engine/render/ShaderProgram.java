package engine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    public static final ShaderProgram SHADER_PROGRAM = new ShaderProgram("shader");
    private final int program;
    private int vs;
    private int fs;
    private final Map<String, Integer> uniforms;

    public ShaderProgram(String filename) {
        uniforms = new HashMap<>();

        program = glCreateProgram();
        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename + ".vs"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(filename + ".fs"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

      //  glBindAttribLocation(program, 0, "vertices");
      //  glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    public void createUniform(String uniformName){
        if(uniforms.containsKey(uniformName)) return;
        int uniformLocation = glGetUniformLocation(program, uniformName);
//        if (uniformLocation < 0) {
//            throw new Exception("Could not find uniform:" + uniformName);
//        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, int value) {
        if(!uniforms.containsKey(uniformName)) createUniform(uniformName);
            glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector4f value) {
        if(!uniforms.containsKey(uniformName)) createUniform(uniformName);
            glUniform4f(uniforms.get(uniformName), value.x, value.y, value.y, value.w);
    }
    public void setUniform(String uniformName, Vector3f value) {
        if(!uniforms.containsKey(uniformName)) createUniform(uniformName);
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.y);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        if(!uniforms.containsKey(uniformName)) createUniform(uniformName);
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File("src/main/shaders/" + filename)));
            String line;
            while ((line = br.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string.toString();
    }

    public void cleanUp() {
        unbind();
        glDetachShader(program, vs);
        glDetachShader(program, fs);
        glDeleteShader(vs);
        glDeleteShader(fs);
        glDeleteProgram(program);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
