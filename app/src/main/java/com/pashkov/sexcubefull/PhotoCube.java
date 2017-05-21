package com.pashkov.sexcubefull;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import com.pashkov.sexcubefull.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class PhotoCube {
	   private FloatBuffer vertexBuffer;  // Vertex Buffer
	   private FloatBuffer texBuffer;     // Texture Coords Buffer
	   
	   private int numFaces = 43;
	   private int[] imageFileIDs = {  // Image file IDs
	      R.drawable.k0,
	      R.drawable.k1,
	      R.drawable.k2,
	      R.drawable.k3,
	      R.drawable.k4,
	      R.drawable.k5,
	      R.drawable.k6,
	      R.drawable.k7,
	      R.drawable.k8,
	      R.drawable.k9,
	      R.drawable.k10,
	      R.drawable.k11,
	      R.drawable.k12,
	      R.drawable.k13,
	      R.drawable.k14,
	      R.drawable.k15,
	      R.drawable.k16,
	      R.drawable.k17,
	      R.drawable.k18,
	      R.drawable.k19,
	      R.drawable.k20,
	      R.drawable.k21,
	      R.drawable.k22,
	      R.drawable.k23,
	      R.drawable.k24,
	      R.drawable.k25,
	      R.drawable.k26,
	      R.drawable.k27,
	      R.drawable.k28,
	      R.drawable.k29,
	      R.drawable.k30,
	      R.drawable.k31,
	      R.drawable.k32,
	      R.drawable.k33,
	      R.drawable.k34,
	      R.drawable.k35,
	      R.drawable.k36,
	      R.drawable.k37,
	      R.drawable.k38,
	      R.drawable.k39,
	      R.drawable.k40,
	      R.drawable.k41,
	      R.drawable.k42
	   };
	      private int[] textureIDs = new int[numFaces];
	      private Bitmap[] bitmap = new Bitmap[numFaces];
	   private float cubeHalfSize = 1.0f;
	        
	   // Constructor - Set up the vertex buffer
	   public PhotoCube(Context context) {
	      // Allocate vertex buffer. An float has 4 bytes
	      ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
	      vbb.order(ByteOrder.nativeOrder());
	      vertexBuffer = vbb.asFloatBuffer();
	  
	      // Read images. Find the aspect ratio and adjust the vertices accordingly.
	      for (int face = 0; face < numFaces; face++) {
	         bitmap[face] = BitmapFactory.decodeStream(
	               context.getResources().openRawResource(imageFileIDs[face]));
	         int imgWidth = bitmap[face].getWidth();
	         int imgHeight = bitmap[face].getHeight();
	         float faceWidth = 2.0f;
	         float faceHeight = 2.0f;
	         // Adjust for aspect ratio
	         if (imgWidth > imgHeight) {
	            faceHeight = faceHeight * imgHeight / imgWidth; 
	         } else {
	            faceWidth = faceWidth * imgWidth / imgHeight;
	         }
	         float faceLeft = -faceWidth / 2;
	         float faceRight = -faceLeft;
	         float faceTop = faceHeight / 2;
	         float faceBottom = -faceTop;
	         
	         // Define the vertices for this face
	         float[] vertices = {
	            faceLeft,  faceBottom, 0.0f,  // 0. left-bottom-front
	            faceRight, faceBottom, 0.0f,  // 1. right-bottom-front
	            faceLeft,  faceTop,    0.0f,  // 2. left-top-front
	            faceRight, faceTop,    0.0f,  // 3. right-top-front
	         };
	         vertexBuffer.put(vertices);  // Populate
	      }
	      vertexBuffer.position(0);    // Rewind
	  
	      // Allocate texture buffer. An float has 4 bytes. Repeat for 6 faces.
	      float[] texCoords = {
	         0.0f, 1.0f,  // A. left-bottom
	         1.0f, 1.0f,  // B. right-bottom
	         0.0f, 0.0f,  // C. left-top
	         1.0f, 0.0f   // D. right-top
	      };
	      ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4 * numFaces);
	      tbb.order(ByteOrder.nativeOrder());
	      texBuffer = tbb.asFloatBuffer();
	      for (int face = 0; face < numFaces; face++) {
	         texBuffer.put(texCoords);
	      }
	      texBuffer.position(0);   // Rewind
	   }
   
   // Render the shape
   public void draw(GL10 gl, Integer texture0, Integer texture1, Integer texture2, Integer texture3, Integer texture4, Integer texture5) {
	   gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
	      gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
	      gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display) 
	   
	      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array (NEW)
	      gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer (NEW)
	      
	      // front
	      gl.glPushMatrix();
	      gl.glTranslatef(0f, 0f, cubeHalfSize);
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texture0]);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	      gl.glPopMatrix();
	  
	      // left
	      gl.glPushMatrix();
	      gl.glRotatef(270.0f, 0f, 1f, 0f);
	      gl.glTranslatef(0f, 0f, cubeHalfSize);
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texture1]);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	      gl.glPopMatrix();
	  
	      // back
	      gl.glPushMatrix();
	      gl.glRotatef(180.0f, 0f, 1f, 0f);
	      gl.glTranslatef(0f, 0f, cubeHalfSize);
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texture2]);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	      gl.glPopMatrix();
	  
	      // right
	      gl.glPushMatrix();
	      gl.glRotatef(90.0f, 0f, 1f, 0f);
	      gl.glTranslatef(0f, 0f, cubeHalfSize);
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texture3]);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	      gl.glPopMatrix();
	  
	      // top
	      gl.glPushMatrix();
	      gl.glRotatef(270.0f, 1f, 0f, 0f);
	      gl.glTranslatef(0f, 0f, cubeHalfSize);
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texture4]);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	      gl.glPopMatrix();
	  
	      // bottom
	      gl.glPushMatrix();
	      gl.glRotatef(90.0f, 1f, 0f, 0f);
	      gl.glTranslatef(0f, 0f, cubeHalfSize);
	      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[texture5]);
	      gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	      gl.glPopMatrix();
	  
	      gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
	      gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	      gl.glDisable(GL10.GL_CULL_FACE);
   }
  
// Load images into 6 GL textures
   public void loadTexture(GL10 gl,Integer a) {
      gl.glGenTextures(43, textureIDs, 0); // Generate texture-ID array for 6 IDs
  
      // Generate OpenGL texture images
      for (int face = 0; face < numFaces; face++) {
         gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[face]);
         // Build Texture from loaded bitmap for the currently-bind texture ID
         GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap[face], 0);
         bitmap[face].recycle();
         gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
         gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);     
      }
   }
}