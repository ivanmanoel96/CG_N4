package n4;

public final class Ponto4D {
	private float x;
	private float y;
	private float z;
	private float w;

	public Ponto4D() {
		this(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public Ponto4D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0f;
	}
	
	public Ponto4D(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	// inverte o sinal das coordenadas do ponto
	public void inverterSinal() {
		this.atribuirX(this.obterX()*-1);
		this.atribuirY(this.obterY()*-1);
		this.atribuirZ(this.obterZ()*-1);
	}
	
	public float obterX() {
		return x;
	}
	
	public float obterY() {
		return y;
	}
	
	public float obterZ() {
		return z;
	}
	
	public float obterW() {
		return w;
	}

	public void atribuirX(float x) {
		this.x = x;
	}
	
	public void atribuirY(float y) {
		this.y = y;
	}
	
	public void atribuirZ(float z) {
		this.z = z;
	}
}