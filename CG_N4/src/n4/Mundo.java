package n4;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import object.OBJModel;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.texture.TextureData;


public final class Mundo implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {
	private ArrayList<Ponto4D[]> faces2D;
	private Ponto4D[] pistaIntermediaria;
	private Ponto4D[] muroIntermediario;
	
	private boolean mudouDirecao;
	private float raio;
	
	private Poligno poligno3D;

	private double xEye, yEye, zEye;
	private double xAt, yAt, zAt;
	private double xUp, yUp, zUp;

	private double angle, near, far;
	private int width, height, img_width, img_height;

	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	
	private OBJModel porsche;
	private int xrotOBJ = 0;
	private int yrotOBJ = 0;
	
	private Animator animator;
	
	private float xPosition = 0.0f;
	private float yPosition = 0.0f;	
	private float xCamera = 0.0f;
	private float yCamera = 0.0f;
	
	private FloatBuffer posLuz;
    private float[] p = { 25.0f, 25.0f, 25.0f, 0.0f};
    
    private int idTexture[];
    private TextureData td;
    private ByteBuffer buffer;
    
	private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;

	private int prevMouseX, prevMouseY;
	
	private static final Mundo INSTANCE = new Mundo();

	private Mundo() {		
		this.posLuz = FloatBuffer.wrap(p);
		this.faces2D = new ArrayList<Ponto4D[]>();
		this.pistaIntermediaria = new Ponto4D[24];
		this.muroIntermediario = new Ponto4D[24];
	}

	public static Mundo getInstance() {
		return INSTANCE;
	}

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));		

		initLookAt();		
		initPerspective();

		this.poligno3D = new Poligno(this.gl, Color.WHITE); 
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Cor de fundo preta
	    gl.glClearDepth(100.0f);      // set clear depth value to farthest	    

	    gl.glDepthFunc(GL.GL_LEQUAL);  // Tipo de teste de profundidade a ser feito	    
	    gl.glEnable(GL.GL_DEPTH_TEST); // Ativa teste de profundidade	    
	    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
	    gl.glShadeModel(GL.GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	    gl.glEnable(GL.GL_NORMALIZE);	    

        loadImage("Roads0059_4_thumbhuge.jpg");        
//        loadImage("tijolo2.jpg");
        idTexture = new int[10];
        gl.glGenTextures(1, idTexture, 1);

        // Especifica qual é a textura corrente pelo identificador
        gl.glBindTexture(GL.GL_TEXTURE_2D, idTexture[0]);

        // Envio da textura para OpenGL
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, img_width, img_height, 0,
                		GL.GL_BGR, GL.GL_UNSIGNED_BYTE, buffer);

        // Define os filtros de magnificação e minificação
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	    
    	this.xrotOBJ = 90;
    	this.yrotOBJ = 180;
    	this.porsche = new OBJModel("data/porsche", 4.0f, gl, true);
    	 
    	animator.stop();
	}	

	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);		
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(angle, (double) width / height, near, far);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();		
		
		cameraPrimeiraPessoa();	
		
		glu.gluLookAt(xEye, yEye, zEye, xAt, yAt, zAt, xUp, yUp, zUp);
			
		gl.glViewport(0, 0, width, height);		
		
		gl.glPushMatrix();
			gl.glTranslated(xCamera, yCamera, 1.0);
			
			gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f); 	
			gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f); 			
			gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);			
		
			gl.glTranslated(-xPosition, -yPosition, -1.0);		
			
			gl.glPushMatrix();
				gl.glTranslated(xPosition, yPosition, 0.0f);
				gl.glRotatef(-view_rotz, 0.0f, 0.0f, 1.0f);
			    gl.glRotated(xrotOBJ, 1.0, 0.0, 0.0);
				gl.glRotated(yrotOBJ, 0.0, 1.0, 0.0);
				
				gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posLuz);
			    gl.glEnable(GL.GL_CULL_FACE);			
				gl.glEnable(GL.GL_LIGHTING);        
			    gl.glEnable(GL.GL_LIGHT0);	
			    
				this.porsche.draw(gl);			
				gl.glDisable(GL.GL_LIGHTING);		
			gl.glPopMatrix();		 

			this.desenhaPista();
			this.desenhaMuros();
			
			for (Ponto4D[] face2D : this.faces2D)
				if (this.poligno3D.colidiu(face2D, xPosition, yPosition)) {
					animator.stop();
					break;
				}
		
//			displaySRU();		
//			run();
		gl.glPopMatrix();
		this.mudouDirecao = false;
//		gl.glFlush();
	}
	
	private void desenhaMuros() {
		this.faces2D.clear();
		this.desenhaMuroInterno();
		this.desenhaMurosExterno();
	}
	
	private void desenhaMurosExterno() {
		this.poligno3D.desenha(new float[]{-5.5f, 15.0f, 0.0f},
							   new float[]{0.0f, 0.0f, 0.0f},
							   new float[]{0.5f, 35.0f, 0.5f});
		this.faces2D.add(this.poligno3D.obtemFace2D());
		this.poligno3D.geraPontosInferiores(this.muroIntermediario);
		
		this.geraMuro3D(new float[]{0.25f, 67.75f, 0.0f},
						new float[]{-30.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{17.25f, 84.75f, 0.0f},
						new float[]{-60.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});

		this.geraMuro3D(new float[]{55.0f, 90.5f, 0.0f},
						new float[]{-90.0f, 0.0f, 0.0f},
						new float[]{0.5f, 20.0f, 0.5f});
		
		this.geraMuro3D(new float[]{87.25f, 94.75f, 0.0f},
						new float[]{-60.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{100.25f, 107.75f, 0.0f},
						new float[]{-30.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{104.5f, 130.0f, 0.0f},
				        new float[]{0.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 10.0f, 0.5f});
		
		this.geraMuro3D(new float[]{110.25f, 157.75f, 0.0f},
				        new float[]{-30.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
	
		this.geraMuro3D(new float[]{127.25f, 174.75f, 0.0f},
				        new float[]{-60.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{165.0f, 180.5f, 0.0f},
				        new float[]{-90.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 20.0f, 0.5f});
	
		this.geraMuro3D(new float[]{202.75f, 174.75f, 0.0f},
				        new float[]{-120.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{219.75f, 157.75f, 0.0f},
				        new float[]{-150.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{225.5f, 60.0f, 0.0f},
				        new float[]{-180.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 80.0f, 0.5f});
		
		this.geraMuro3D(new float[]{219.75f, -37.75f, 0.0f},
				        new float[]{-210.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
	
		this.geraMuro3D(new float[]{202.75f, -54.75f, 0.0f},
				        new float[]{-240.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{110.0f, -60.5f, 0.0f},
				        new float[]{-270.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 75.0f, 0.5f});
				
		this.geraMuro3D(new float[]{17.25f, -54.75f, 0.0f},
				        new float[]{-300.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
				
		this.geraMuro3D(new float[]{0.25f, -37.75f, 0.0f},
				        new float[]{-330.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
				
		this.poligno3D.desenha(new float[]{-5.5f, 15.0f, 0.0f},
						   	   new float[]{0.0f, 0.0f, 0.0f},
						   	   new float[]{0.5f, 35.0f, 0.5f});
		this.poligno3D.geraPontosSuperiores(this.muroIntermediario);
		this.poligno3D.desenha(this.muroIntermediario);
		this.faces2D.add(this.poligno3D.obtemFace2D(this.muroIntermediario));
	}
	
	private void desenhaMuroInterno() {
		this.poligno3D.desenha(new float[]{5.5f, 15.0f, 0.0f},
							   new float[]{0.0f, 0.0f, 0.0f},
							   new float[]{0.5f, 35.0f, 0.5f});
		this.faces2D.add(this.poligno3D.obtemFace2D());
		this.poligno3D.geraPontosInferiores(this.muroIntermediario);
		
		this.geraMuro3D(new float[]{9.75f, 62.25f, 0.0f},
						new float[]{-30.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{22.75f, 75.25f, 0.0f},
						new float[]{-60.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{55.0f, 79.5f, 0.0f},
						new float[]{-90.0f, 0.0f, 0.0f},
						new float[]{0.5f, 20.0f, 0.5f});
		
		this.geraMuro3D(new float[]{92.75f, 85.25f, 0.0f},
						new float[]{-60.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{109.75f, 102.25f, 0.0f},
						new float[]{-30.0f, 0.0f, 0.0f},
						new float[]{0.5f, 5.0f, 0.5f});

		this.geraMuro3D(new float[]{115.5f, 130.0f, 0.0f},
				        new float[]{0.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 10.0f, 0.5f});
		
		this.geraMuro3D(new float[]{119.75f, 152.25f, 0.0f},
				        new float[]{-30.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{132.75f, 165.25f, 0.0f},
				        new float[]{-60.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{165.0f, 169.5f, 0.0f},
				        new float[]{-90.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 20.0f, 0.5f});
		
		this.geraMuro3D(new float[]{197.25f, 165.25f, 0.0f},
				        new float[]{-120.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{210.25f, 152.25f, 0.0f},
				        new float[]{-150.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{214.5f, 60.0f, 0.0f},
				        new float[]{-180.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 80.0f, 0.5f});
		
		this.geraMuro3D(new float[]{210.25f, -32.25f, 0.0f},
				        new float[]{-210.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{197.25f, -45.25f, 0.0f},
				        new float[]{-240.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{110.0f, -49.5f, 0.0f},
				        new float[]{-270.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 75.0f, 0.5f});
		
		this.geraMuro3D(new float[]{22.75f, -45.25f, 0.0f},
				        new float[]{-300.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.geraMuro3D(new float[]{9.75f, -32.25f, 0.0f},
				        new float[]{-330.0f, 0.0f, 0.0f},
				        new float[]{0.5f, 5.0f, 0.5f});
		
		this.poligno3D.desenha(new float[]{5.5f, 15.0f, 0.0f},
							   new float[]{0.0f, 0.0f, 0.0f},
							   new float[]{0.5f, 35.0f, 0.5f});
		this.poligno3D.geraPontosSuperiores(this.muroIntermediario);
		this.poligno3D.desenha(this.muroIntermediario);
		this.faces2D.add(this.poligno3D.obtemFace2D(this.muroIntermediario));
	}
	
	private void desenhaPista() {
		gl.glEnable(GL.GL_TEXTURE_2D);
			this.poligno3D.desenha(new float[]{0.0f, 15.0f, -1.0f}, 
								   new float[]{0.0f, 0.0f, 0.0f}, 
								   new float[]{5.0f, 35.0f, 0.5f});
			this.poligno3D.geraPontosInferiores(this.pistaIntermediaria);
			
			this.geraPista3D(new float[]{5.0f, 65.0f, -1.0f},
					         new float[]{-30.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{20.0f, 80.0f, -1.0f},
					         new float[]{-60.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{55.0f, 85.0f, -1.0f},
					         new float[]{-90.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 20.0f, 0.5f});
			
			this.geraPista3D(new float[]{90.0f, 90.0f, -1.0f},
					         new float[]{-60.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{105.0f, 105.0f, -1.0f},
					         new float[]{-30.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{110.0f, 130.0f, -1.0f},
					         new float[]{0.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 10.0f, 0.5f});
			
			this.geraPista3D(new float[]{115.0f, 155.0f, -1.0f},
					         new float[]{-30.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{130.0f, 170.0f, -1.0f},
					         new float[]{-60.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{165.0f, 175.0f, -1.0f},
					         new float[]{-90.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 20.0f, 0.5f});
			
			this.geraPista3D(new float[]{200.0f, 170.0f, -1.0f},
					         new float[]{-120.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{215.0f, 155.0f, -1.0f},
					         new float[]{-150.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.geraPista3D(new float[]{220.0f, 60.0f, -1.0f},
					         new float[]{-180.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 80.0f, 0.5f});
			
			this.geraPista3D(new float[]{215.0f, -35.0f, -1.0f},
					         new float[]{-210.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
	
			this.geraPista3D(new float[]{200.0f, -50.0f, -1.0f},
					         new float[]{-240.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});

			this.geraPista3D(new float[]{110.0f, -55.0f, -1.0f},
					         new float[]{-270.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 75.0f, 0.5f});
		
			this.geraPista3D(new float[]{20.0f, -50.0f, -1.0f},
					         new float[]{-300.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});

			this.geraPista3D(new float[]{5.0f, -35.0f, -1.0f},
					         new float[]{-330.0f, 0.0f, 0.0f},
					         new float[]{5.0f, 5.0f, 0.5f});
			
			this.poligno3D.desenha(new float[]{0.0f, 15.0f, -1.0f}, 
								   new float[]{0.0f, 0.0f, 0.0f}, 
								   new float[]{5.0f, 35.0f, 0.5f});
			this.poligno3D.geraPontosSuperiores(this.pistaIntermediaria);
			this.poligno3D.desenha(this.pistaIntermediaria);
		this.gl.glDisable(GL.GL_TEXTURE_2D);
	}
	
	private void geraMuro3D(float[] translacao, float[] rotacao, float[] escala) {
		this.poligno3D.desenha(translacao, rotacao, escala);
		this.faces2D.add(this.poligno3D.obtemFace2D());
		this.geraMuro3DIntermediario();
	}
	
	private void geraMuro3DIntermediario() {
		this.poligno3D.geraPontosSuperiores(this.muroIntermediario);
		this.poligno3D.desenha(this.muroIntermediario);
		this.faces2D.add(this.poligno3D.obtemFace2D(this.muroIntermediario));
		this.poligno3D.geraPontosInferiores(this.muroIntermediario);
	}
	
	private void geraPista3D(float[] translacao, float[] rotacao, float[] escala) {
		this.poligno3D.desenha(translacao, rotacao, escala);
		this.geraPista3DIntermediaria();
	}
	
	private void geraPista3DIntermediaria() {
		this.poligno3D.geraPontosSuperiores(this.pistaIntermediaria);
		this.poligno3D.desenha(this.pistaIntermediaria);
		this.poligno3D.geraPontosInferiores(this.pistaIntermediaria);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();	 
		 
		if (height == 0)
			height = 1;  
  
		gl.glViewport(0, 0, width, height);
  
		float aspect = (float)width / height;	 
  
		gl.glMatrixMode(GL.GL_PROJECTION);  
		gl.glLoadIdentity(); 
  
		glu.gluPerspective(angle, aspect, near, far); 	     
  
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();	     	
  
		glu.gluLookAt(xEye, yEye, zEye, xAt, yAt, zAt, xUp, yUp, zUp);	      
	}

	public void keyPressed(KeyEvent e) {		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				System.out.println(xPosition);
				System.out.println(yPosition);
				run();
				break;
			
			case KeyEvent.VK_RIGHT:	
				this.view_rotz += 1.0f;
				this.mudouDirecao = true;
				run();
				break;
				
			case KeyEvent.VK_LEFT:
				this.view_rotz -= 1.0f;
				this.mudouDirecao = true;
				run();
				break;
				
			case KeyEvent.VK_UP:
				run();
				break;
				
			case KeyEvent.VK_DOWN:
				run();
				break;
				
			case KeyEvent.VK_U:	
				animator.stop();	
				break;	
				
			case KeyEvent.VK_I:	
				animator.start();	
				break;		
				
			case KeyEvent.VK_P:
				this.zEye += 1.0;
				break;
				
			case KeyEvent.VK_O:
				this.zEye -= 1.0;
				break;				
		}		
	}	
	
	public void mousePressed(MouseEvent e) {
		prevMouseX = e.getX();
	    prevMouseY = e.getY();		
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
	    int y = e.getY();
	    Dimension size = e.getComponent().getSize();

	    float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    
	    prevMouseX = x;
	    prevMouseY = y;

	    view_rotx += thetaX;
	    view_roty += thetaY;
	}

	public void initLookAt() {
		this.xEye =  xPosition;
		this.yEye =  yPosition;
		this.zEye =  25.0;

		this.xAt = xPosition;
		this.yAt = yPosition;
		this.zAt = 1.0;

		this.xUp = 0.0;
		this.yUp = 1.0;
		this.zUp = 0.0;		
	}
	
	public void cameraPrimeiraPessoa() {		
		this.xEye = xCamera;
		this.yEye = yCamera;		

		this.xAt = xCamera;
		this.yAt = yCamera;			
	}
	
	public void initPerspective() {
		this.width = glDrawable.getWidth();
		this.height = glDrawable.getHeight();
		this.near = 1.0;
		this.far = 300.0;
		this.angle = 60.0;
	}

	public void displaySRU() {
		gl.glBegin(GL.GL_LINES);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex3d(0.0, 0.0, 0.0);
			gl.glVertex3d(10.0, 0.0, 0.0);

			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex3d(0.0, 0.0, 0.0);
			gl.glVertex3d(0.0, 10.0, 0.0);
	
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex3d(0.0, 0.0, 0.0);
			gl.glVertex3d(0.0, 0.0, 10.0);
		gl.glEnd();
	}
	
	public void run() {
		if(this.view_rotz == 360 || this.view_rotz == -360)
			this.view_rotz = 0;

		this.raio = 1.0f;
		if (this.mudouDirecao)
			this.raio = 0.0f;
			
		this.yPosition += this.retornarX(view_rotz, this.raio);
		this.xPosition += this.retornarY(view_rotz, this.raio);
		this.yCamera += this.retornarX(view_rotz, this.raio);
		this.xCamera += this.retornarY(view_rotz, this.raio);
	}
	
	private float retornarX(double angulo, double raio) {
		return (float) (raio * Math.cos(Math.PI * angulo / 180));
	}
	
	private float retornarY(double angulo, double raio) {
		return (float) (raio * Math.sin(Math.PI * angulo / 180));
	}
	
	public void setAnimator(FPSAnimator animator) {		
		this.animator = animator;		
	}
	
	public void loadImage(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(fileName));
			img_width = image.getWidth();
		    img_height = image.getHeight();
		    td = new TextureData(0, 0, false, image);
		    buffer = (ByteBuffer) td.getBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}
}
