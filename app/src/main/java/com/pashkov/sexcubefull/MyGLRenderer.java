package com.pashkov.sexcubefull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
   
public class MyGLRenderer implements GLSurfaceView.Renderer {
   
   cube cubik;
   private Context context;
   private PhotoCube cube;
   
   static Integer texture_num0 = 15;
   static Integer texture_num1 = 18;
   static Integer texture_num2 = 22;
   static Integer texture_num3 = 19;
   static Integer texture_num4 = 23;
   static Integer texture_num5 = 21;
   
   public static float coficient = 1.8f;
   // For controlling cube's z-position, x and y angles and speeds (NEW)
   float angleX = 0;   // (NEW)
   float angleY = 0;   // (NEW)
   float speedX = 0;   // (NEW)
   float speedY = 0;   // (NEW)
   float z = -6.0f;    // (NEW)
   
   // Constructor
   public MyGLRenderer(Context context) {
      this.context = context;
      cube = new PhotoCube(context);
   }
  
   // Call back when the surface is first created or re-created.
   public void onSurfaceCreated(GL10 gl, EGLConfig config) {
      gl.glClearColor(0.0078f, 0.227f, 0.313f, 1.0f);  // Set color's clear-value to black
      gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
      gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
      gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
      gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
      gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
      gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
     
      // Setup Texture, each time the surface is created (NEW)
      cube.loadTexture(gl,0);             // Load images into textures (NEW)
      gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)
   }
   
   // Call back after onSurfaceCreated() or whenever the window's size changes.
   public void onSurfaceChanged(GL10 gl, int width, int height) {
      if (height == 0) height = 1;   // To prevent divide by zero
      float aspect = (float)width / height;
   
      // Set the viewport (display area) to cover the entire window
      gl.glViewport(0, 0, width, height);
  
      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
      gl.glLoadIdentity();                 // Reset projection matrix
      // Use perspective projection
      GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);
  
      gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
      gl.glLoadIdentity();                 // Reset
  
      // You OpenGL|ES display re-sizing code here
      // ......
   }
   
   // Call back to draw the current frame.
   public void onDrawFrame(GL10 gl) {
      // Clear color and depth buffers
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

      // ----- Render the Cube -----
      gl.glLoadIdentity();              // Reset the model-view matrix
      gl.glTranslatef(0.0f, -0.0f, z);   // Translate into the screen (NEW)
      gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // Rotate (NEW)
      gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // Rotate (NEW)
      cube.draw(gl,texture_num0,texture_num1,texture_num2,texture_num3,texture_num4,texture_num5);
      
      // Update the rotational angle after each refresh (NEW)
      angleX += speedX + (com.pashkov.sexcubefull.cube.x1/coficient) + 2;  // (NEW)
      angleY += speedY + (com.pashkov.sexcubefull.cube.y1/coficient) + 2;  // (NEW)
   }
}
