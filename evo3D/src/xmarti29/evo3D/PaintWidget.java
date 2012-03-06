package xmarti29.evo3D;

import com.trolltech.qt.opengl.*;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

/**
 * This class draws the 3D object using OpenGL and provides means
 * to scale or rotate it.
 */
public class PaintWidget extends QGLWidget
{
	GLContext glContext;
	GL gl;
	GLU glu;
	GLUT glut;
	
	Evo3D evo3D = null;
	
	int thumbIndex = -1;
	
	GA ga;
	
	Workspace workspace = null;
	
	float []mat_ambient={0.1f, 0.1f, 0.1f, 1.0f};
	float []mat_diffuse={1.0f, 0.0f, 0.0f, 1.0f};
	float []lt_ambient= {1.0f, 1.0f, 1.0f, 1.0f};
	float []lt_diffuse= {1.0f, 1.0f, 1.0f, 1.0f};
	float []lt_specular={1.0f, 1.0f, 1.0f, 1.0f};
	float []lt_position={0.0f, 200.0f, 100.0f, 0.0f};
	
	int transform = 0;
	
	float tmpRot = 0.0f;
	float yRot = 20.0f;
	float scale = 1.0f;
	float tmpScale = 0.0f;
	
	public PaintWidget(Evo3D _evo3D, int index, GA _ga)
	{
		evo3D = _evo3D;
		thumbIndex = index;
		ga = _ga;
	}
	
	protected void initializeGL()
	{
		glContext = GLDrawableFactory.getFactory().createExternalGLContext();
		glContext.makeCurrent();
		gl = glContext.getGL();
		glu = new GLU();
		glut = new GLUT();
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
				
		gl.glPointSize(5.0f);
		gl.glEnable(GL.GL_POINT_SMOOTH);
		gl.glLineWidth(1.0f);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		    
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lt_ambient, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, lt_diffuse, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, lt_specular, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lt_position, 0);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, 0);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, 1);

		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		    
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mat_ambient, 0);

	}
	
	protected void resizeGL(int w, int h)
	{		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, w, h);
		glu.gluPerspective(45.0f, (float)w/(float)h, 1.0, 100.0);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();	
		glu.gluLookAt(0.0f, 10.0f, -30.0f, 0.0f, 5.0f, 0.0f, 0.0f, 1.0f, 0.0f);
	}
	
	protected void paintGL()
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glPushMatrix();
		
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		gl.glScalef(scale, scale, scale);
		
		if (workspace != null)
			workspace.render(gl, glut);
		
		gl.glPopMatrix();
	}
	
	public void setWorkspace(Workspace work)
	{
		workspace = work;
	}
	
	protected void mousePressEvent(QMouseEvent event)
	{
		if (event.button() == Qt.MouseButton.LeftButton)
		{
			tmpRot = event.x();
			transform = 1;
		}
		else if (event.button() == Qt.MouseButton.RightButton)
		{
			tmpScale = event.y();
			transform = 2;
		}
	}
	
	protected void mouseReleaseEvent(QMouseEvent event)
	{
		transform = 0;
		
		if (event.button() == Qt.MouseButton.MidButton && thumbIndex != -1)
		{
			evo3D.updateWorkspace(thumbIndex);
		}
	}
	
	protected void mouseMoveEvent(QMouseEvent event)
	{
		switch(transform)
		{
		case 1:
			yRot -= (tmpRot-event.x())/3.0f;
			tmpRot = event.x();
			
			update();
			break;
			
		case 2:
			scale += (tmpScale-event.y())/200.0f;
			if (scale < 0.1f)
				scale = 0.1f;
			tmpScale = event.y();
			
			update();
			break;
		}
	}
}
