package n4;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class Poligno {
	private FloatBuffer matriz;
	private Color color;
	private GL gl;

	public Poligno(GL gl, Color color) {
		this.gl = gl;
		this.color = color;
		this.matriz = FloatBuffer.wrap(new float[16]);
	}
	
	public void desenha(float[] translacao, float[] rotacao, float[] escala) {
		this.gl.glPushMatrix();
		this.gl.glLoadIdentity();
		this.gl.glTranslated(translacao[0], translacao[1], translacao[2]);
		this.gl.glRotated(rotacao[0], rotacao[1], rotacao[2], 1.0f);
		this.gl.glScaled(escala[0], escala[1], escala[2]);
		this.gl.glGetFloatv(GL.GL_MODELVIEW_MATRIX, matriz);
		this.gl.glPopMatrix();

		this.gl.glPushMatrix();
		this.gl.glColor3f(0.0f, 0.0f, 1.0f);
		this.gl.glMultMatrixf(matriz);
			this.geraCubo();
		this.gl.glPopMatrix();
	}

	private void geraCubo() {
		// Face frontal
		Ponto4D[] cubo = new Ponto4D[24];
		cubo[0] = new Ponto4D(-1.0f, -1.0f, 1.0f);
		cubo[1] = new Ponto4D(1.0f, -1.0f, 1.0f);
		cubo[2] = new Ponto4D(1.0f, 1.0f, 1.0f);
		cubo[3] = new Ponto4D(-1.0f, 1.0f, 1.0f);
		// Face posterior
		cubo[4] = new Ponto4D(-1.0f, -1.0f, -1.0f);
		cubo[5] = new Ponto4D(-1.0f, 1.0f, -1.0f);
		cubo[6] = new Ponto4D(1.0f, 1.0f, -1.0f);
		cubo[7] = new Ponto4D(1.0f, -1.0f, -1.0f);
		// Face superior
		cubo[8] = new Ponto4D(-1.0f, 1.0f, -1.0f);
		cubo[9] = new Ponto4D(-1.0f, 1.0f, 1.0f);
		cubo[10] = new Ponto4D(1.0f, 1.0f, 1.0f);
		cubo[11] = new Ponto4D(1.0f, 1.0f, -1.0f);
		// Face inferior
		cubo[12] = new Ponto4D(-1.0f, -1.0f, -1.0f);
		cubo[13] = new Ponto4D(1.0f, -1.0f, -1.0f);
		cubo[14] = new Ponto4D(1.0f, -1.0f, 1.0f);
		cubo[15] = new Ponto4D(-1.0f, -1.0f, 1.0f);
		// Face lateral direita
		cubo[16] = new Ponto4D(1.0f, -1.0f, -1.0f);
		cubo[17] = new Ponto4D(1.0f, 1.0f, -1.0f);
		cubo[18] = new Ponto4D(1.0f, 1.0f, 1.0f);
		cubo[19] = new Ponto4D(1.0f, -1.0f, 1.0f);
		// Face lateral esquerda
		cubo[20] = new Ponto4D(-1.0f, -1.0f, -1.0f);
		cubo[21] = new Ponto4D(-1.0f, -1.0f, 1.0f);
		cubo[22] = new Ponto4D(-1.0f, 1.0f, 1.0f);
		cubo[23] = new Ponto4D(-1.0f, 1.0f, -1.0f);
		this.desenha(cubo);
	}

	public void desenha(Ponto4D[] poligno) {
		this.gl.glColor3f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255);
		this.gl.glBegin(GL.GL_QUADS);
			// Face frontal
			this.gl.glNormal3f(0.0f, 0.0f, -1.0f);
			this.gl.glTexCoord2f(0.0f, 1.0f);
			this.gl.glVertex3f(poligno[0].obterX(), poligno[0].obterY(), poligno[0].obterZ());
			this.gl.glTexCoord2f(1.0f, 1.0f);
			this.gl.glVertex3f(poligno[1].obterX(), poligno[1].obterY(), poligno[1].obterZ());
			this.gl.glTexCoord2f(1.0f, 0.0f);
			this.gl.glVertex3f(poligno[2].obterX(), poligno[2].obterY(), poligno[2].obterZ());
			this.gl.glTexCoord2f(0.0f, 0.0f);
			this.gl.glVertex3f(poligno[3].obterX(), poligno[3].obterY(), poligno[3].obterZ());
			// Face posterior
			this.gl.glNormal3f(0.0f, 0.0f, 1.0f);
			this.gl.glTexCoord2f(1.0f, 0.0f);
			this.gl.glVertex3f(poligno[4].obterX(), poligno[4].obterY(), poligno[4].obterZ());
			this.gl.glTexCoord2f(1.0f, 1.0f);
			this.gl.glVertex3f(poligno[5].obterX(), poligno[5].obterY(), poligno[5].obterZ());
			this.gl.glTexCoord2f(0.0f, 1.0f);
			this.gl.glVertex3f(poligno[6].obterX(), poligno[6].obterY(), poligno[6].obterZ());
			this.gl.glTexCoord2f(0.0f, 0.0f);
			this.gl.glVertex3f(poligno[7].obterX(), poligno[7].obterY(), poligno[7].obterZ());
			// Face superior
			this.gl.glNormal3f(0.0f, 1.0f, 0.0f);
			this.gl.glTexCoord2f(0.0f, 1.0f);
			this.gl.glVertex3f(poligno[8].obterX(), poligno[8].obterY(), poligno[8].obterZ());
			this.gl.glTexCoord2f(0.0f, 0.0f);
			this.gl.glVertex3f(poligno[9].obterX(), poligno[9].obterY(), poligno[9].obterZ());
			this.gl.glTexCoord2f(1.0f, 0.0f);
			this.gl.glVertex3f(poligno[10].obterX(), poligno[10].obterY(), poligno[10].obterZ());
			this.gl.glTexCoord2f(1.0f, 1.0f);
			this.gl.glVertex3f(poligno[11].obterX(), poligno[11].obterY(), poligno[11].obterZ());
			// Face inferior
			this.gl.glNormal3f(0.0f, -1.0f, 0.0f);
			this.gl.glTexCoord2f(1.0f, 1.0f);
			this.gl.glVertex3f(poligno[12].obterX(), poligno[12].obterY(), poligno[12].obterZ());
			this.gl.glTexCoord2f(0.0f, 1.0f);
			this.gl.glVertex3f(poligno[13].obterX(), poligno[13].obterY(), poligno[13].obterZ());
			this.gl.glTexCoord2f(0.0f, 0.0f);
			this.gl.glVertex3f(poligno[14].obterX(), poligno[14].obterY(), poligno[14].obterZ());
			this.gl.glTexCoord2f(1.0f, 0.0f);
			this.gl.glVertex3f(poligno[15].obterX(), poligno[15].obterY(), poligno[15].obterZ());
			// Face lateral direita
			this.gl.glNormal3f(1.0f, 0.0f, 0.0f);
			this.gl.glTexCoord2f(1.0f, 0.0f);
			this.gl.glVertex3f(poligno[16].obterX(), poligno[16].obterY(), poligno[16].obterZ());
			this.gl.glTexCoord2f(1.0f, 1.0f);
			this.gl.glVertex3f(poligno[17].obterX(), poligno[17].obterY(), poligno[17].obterZ());
			this.gl.glTexCoord2f(0.0f, 1.0f);
			this.gl.glVertex3f(poligno[18].obterX(), poligno[18].obterY(), poligno[18].obterZ());
			this.gl.glTexCoord2f(0.0f, 0.0f);
			this.gl.glVertex3f(poligno[19].obterX(), poligno[19].obterY(), poligno[19].obterZ());
			// Face lateral esquerda
			this.gl.glNormal3f(-1.0f, 0.0f, 0.0f);
			this.gl.glTexCoord2f(0.0f, 0.0f);
			this.gl.glVertex3f(poligno[20].obterX(), poligno[20].obterY(), poligno[20].obterZ());
			this.gl.glTexCoord2f(1.0f, 0.0f);
			this.gl.glVertex3f(poligno[21].obterX(), poligno[21].obterY(), poligno[21].obterZ());
			this.gl.glTexCoord2f(1.0f, 1.0f);
			this.gl.glVertex3f(poligno[22].obterX(), poligno[22].obterY(), poligno[22].obterZ());
			this.gl.glTexCoord2f(0.0f, 1.0f);
			this.gl.glVertex3f(poligno[23].obterX(), poligno[23].obterY(), poligno[23].obterZ());
		this.gl.glEnd();
	}

	public boolean colidiu(Ponto4D[] face2D, float x, float y) {
		if (this.analisaBBox(face2D, x, y)) {
			short interseccoes = 0;

			if (this.percenceReta(x, y, face2D[0], face2D[face2D.length - 1]))
				return true;
			
			if (this.existeIntersecao(x, y, face2D[0], face2D[face2D.length - 1]))
				interseccoes++;
			
			for (int j = 1; j < face2D.length; j++) {
				if (this.percenceReta(x, y, face2D[j - 1], face2D[j]))
					return true;
					
				if (this.existeIntersecao(x, y, face2D[j - 1], face2D[j]))
					interseccoes++;
			}

			return interseccoes % 2 > 0;
		}
		return false;
	}
	
	private boolean percenceReta(float x, float y, Ponto4D origem, Ponto4D destino) {
		if ((origem.obterX()) == (destino.obterX()) && (origem.obterY()) == (destino.obterY()))
			return (x) == (origem.obterX()) && y == (origem.obterY());

		float result = x * (origem.obterY() - destino.obterY()) + y * (destino.obterX() - origem.obterX())
				+ (origem.obterX() * destino.obterY() - destino.obterX() * origem.obterY());

		return result == 0;
	}

	private boolean existeIntersecao(float x, float y, Ponto4D origem, Ponto4D destino) {
		float equacao = (y - origem.obterY()) / (destino.obterY() - origem.obterY());
		float interseccaoX = origem.obterX() + (destino.obterX() - origem.obterX()) * equacao;
		return interseccaoX >= x ? Math.abs(equacao - 0.5) <= 0.5 : false;
	}

	private boolean analisaBBox(Ponto4D[] cubo, float x, float y) {
		float menorX = cubo[0].obterX();
		float menorY = cubo[0].obterY();
		float maiorX = cubo[0].obterX();
		float maiorY = cubo[0].obterY();

		for (int i = 0; i < cubo.length; i++) {
			if (cubo[i].obterX() < menorX)
				menorX = cubo[i].obterX();

			if (cubo[i].obterX() > maiorX)
				maiorX = cubo[i].obterX();

			if (cubo[i].obterY() < menorY)
				menorY = cubo[i].obterY();

			if (cubo[i].obterY() > maiorY)
				maiorY = cubo[i].obterY();
		}

		return x >= menorX && x <= maiorX && y >= menorY && y <= maiorY;
	}

	public Ponto4D[] obtemFace2D() {
		Ponto4D[] face2D = new Ponto4D[4];
		face2D[0] = this.transformaPonto(-1.0f, 1.0f, 1.0f);
		face2D[1] = this.transformaPonto(1.0f, 1.0f, 1.0f);
		face2D[2] = this.transformaPonto(1.0f, -1.0f, 1.0f);
		face2D[3] = this.transformaPonto(-1.0f, -1.0f, 1.0f);
		return face2D;
	}
	
	public Ponto4D[] obtemFace2D(Ponto4D[] cubo) {
		Ponto4D[] face2D = new Ponto4D[4];
		face2D[0] = cubo[0];
		face2D[1] = cubo[1];
		face2D[2] = cubo[2];
		face2D[3] = cubo[3];
		return face2D;
	}

	public void geraPontosInferiores(Ponto4D[] pistaIntermediaria) {
		// Face frontal
		pistaIntermediaria[0] = this.transformaPonto(-1.0f, 1.0f, 1.0f);
		pistaIntermediaria[1] = this.transformaPonto(1.0f, 1.0f, 1.0f);
		// Face posterior
		pistaIntermediaria[4] = this.transformaPonto(-1.0f, 1.0f, -1.0f);
		pistaIntermediaria[7] = this.transformaPonto(1.0f, 1.0f, -1.0f);
		// Face inferior
		pistaIntermediaria[12] = this.transformaPonto(-1.0f, 1.0f, -1.0f);
		pistaIntermediaria[13] = this.transformaPonto(1.0f, 1.0f, -1.0f);
		pistaIntermediaria[14] = this.transformaPonto(1.0f, 1.0f, 1.0f);
		pistaIntermediaria[15] = this.transformaPonto(-1.0f, 1.0f, 1.0f);
		// Face lateral direita
		pistaIntermediaria[16] = this.transformaPonto(1.0f, 1.0f, -1.0f);
		pistaIntermediaria[19] = this.transformaPonto(1.0f, 1.0f, 1.0f);
		// Face lateral esquerda
		pistaIntermediaria[20] = this.transformaPonto(-1.0f, 1.0f, -1.0f);
		pistaIntermediaria[21] = this.transformaPonto(-1.0f, 1.0f, 1.0f);
	}

	public void geraPontosSuperiores(Ponto4D[] cuboIntermediario) {
		// Face frontal
		cuboIntermediario[2] = this.transformaPonto(1.0f, -1.0f, 1.0f);
		cuboIntermediario[3] = this.transformaPonto(-1.0f, -1.0f, 1.0f);
		// Face posterior
		cuboIntermediario[5] = this.transformaPonto(-1.0f, -1.0f, -1.0f);
		cuboIntermediario[6] = this.transformaPonto(1.0f, -1.0f, -1.0f);
		// Face superior
		cuboIntermediario[8] = this.transformaPonto(-1.0f, -1.0f, -1.0f);
		cuboIntermediario[9] = this.transformaPonto(-1.0f, -1.0f, 1.0f);
		cuboIntermediario[10] = this.transformaPonto(1.0f, -1.0f, 1.0f);
		cuboIntermediario[11] = this.transformaPonto(1.0f, -1.0f, -1.0f);
		// Face lateral direita
		cuboIntermediario[17] = this.transformaPonto(1.0f, -1.0f, -1.0f);
		cuboIntermediario[18] = this.transformaPonto(1.0f, -1.0f, 1.0f);
		// Face lateral esquerda
		cuboIntermediario[22] = this.transformaPonto(-1.0f, -1.0f, 1.0f);
		cuboIntermediario[23] = this.transformaPonto(-1.0f, -1.0f, -1.0f);
	}

	private Ponto4D transformaPonto(float x, float y, float z) {
		return this.transformaPonto(new Ponto4D(x, y, z));
	}
	
	public Ponto4D transformaPonto(Ponto4D ponto) {
		return new Ponto4D (this.matriz.get(0) * ponto.obterX() + this.matriz.get(4) * ponto.obterY() + this.matriz.get(8) * ponto.obterZ() + this.matriz.get(12) * ponto.obterW(),
							this.matriz.get(1) * ponto.obterX() + this.matriz.get(5) * ponto.obterY() + this.matriz.get(9) * ponto.obterZ() + this.matriz.get(13) * ponto.obterW(),
							this.matriz.get(2) * ponto.obterX() + this.matriz.get(6) * ponto.obterY() + this.matriz.get(10) * ponto.obterZ() + this.matriz.get(14) * ponto.obterW(),
							this.matriz.get(3) * ponto.obterX() + this.matriz.get(7) * ponto.obterY() + this.matriz.get(11) * ponto.obterZ() + this.matriz.get(15) * ponto.obterW());
	}
}