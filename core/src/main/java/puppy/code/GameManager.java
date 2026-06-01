package puppy.code;

/**
 * Clase que gestiona el estado global del juego.
 * Implementa el patron Singleton (GM-6).
 */
public class GameManager {

    private static GameManager instance;

    private int puntaje;
    private int vidas;
    private int nivel;
    private float velocidadBase;
    private float intervaloSpawn;
    private boolean juegoActivo;

    // limites de dificultad
    private static final float VELOCIDAD_MAXIMA = 500f;
    private static final float INTERVALO_MINIMO = 150000000f;
    private static final int NIVEL_SOLO_SPAWN = 20;

    private GameManager() {
        resetear();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void resetear() {
        this.puntaje = 0;
        this.vidas = 3;
        this.nivel = 1;
        this.velocidadBase = 200f;
        this.intervaloSpawn = 500000000f;
        this.juegoActivo = true;
    }

    public void sumarPuntaje(int puntos) {
        this.puntaje += puntos;
        verificarNivel();
    }

    public void perderVida() {
        if (vidas > 0) vidas--;
        if (vidas <= 0) juegoActivo = false;
    }

    /**
     * Verifica si el jugador sube de nivel.
     * Cada 60 puntos sube un nivel.
     */
    private void verificarNivel() {
        int nuevoNivel = (puntaje / 60) + 1;
        if (nuevoNivel > nivel) {
            nivel = nuevoNivel;
            aumentarDificultad();
        }
    }

    /**
     * Aumenta la dificultad al subir de nivel.
     * Antes del nivel 20: aumenta velocidad y frecuencia de spawn.
     * Desde el nivel 20: solo aumenta frecuencia de spawn (mas enemigos).
     * La velocidad tiene un tope maximo para que sea jugable.
     */
    private void aumentarDificultad() {
        if (nivel < NIVEL_SOLO_SPAWN) {
            // aumentar velocidad solo si no supera el maximo
            if (velocidadBase < VELOCIDAD_MAXIMA) {
                velocidadBase = Math.min(velocidadBase + 30f, VELOCIDAD_MAXIMA);
            }
        }
        // siempre aumentar frecuencia de spawn
        intervaloSpawn = Math.max(intervaloSpawn * 0.85f, INTERVALO_MINIMO);
    }

    // ==================== Getters y Setters ====================

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
        verificarNivel();
    }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas) { this.vidas = vidas; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public float getVelocidadBase() { return velocidadBase; }
    public void setVelocidadBase(float velocidadBase) { this.velocidadBase = velocidadBase; }
    public float getIntervaloSpawn() { return intervaloSpawn; }
    public void setIntervaloSpawn(float intervaloSpawn) { this.intervaloSpawn = intervaloSpawn; }
    public boolean isJuegoActivo() { return juegoActivo; }
    public void setJuegoActivo(boolean juegoActivo) { this.juegoActivo = juegoActivo; }
}