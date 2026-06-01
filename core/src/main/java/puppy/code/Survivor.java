package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Clase que representa al superviviente en el juego.
 * Extiende GameObject (GM-4) y aplica encapsulamiento (GM-5).
 */
public class Survivor extends GameObject {

    private Sound sonidoHerido;
    private int vidas;
    private int puntos;
    private int velocidadX;
    private boolean herido;
    private int tiempoHeridoMax;
    private int tiempoHerido;

    // sistema de invencibilidad
    private boolean invencible;
    private float tiempoInvencible;

    public Survivor(Texture texture, Sound sonidoHerido) {
        super(texture, 800 / 2 - 64 / 2, 20, 64, 64);
        this.sonidoHerido = sonidoHerido;
        this.vidas = 3;
        this.puntos = 0;
        this.velocidadX = 400;
        this.herido = false;
        this.tiempoHeridoMax = 50;
        this.tiempoHerido = 0;
        this.invencible = false;
        this.tiempoInvencible = 0;
    }

    /**
     * Aplica danio al superviviente. No permite vidas negativas.
     * Si esta invencible, no recibe danio.
     */
    public void dañar() {
        if (invencible) return; // no recibe danio si tiene escudo
        if (vidas > 0) {
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
        }
    }

    /**
     * Cura al superviviente restaurando una vida.
     * Maximo 5 vidas.
     */
    public void curar() {
        if (vidas < 5) {
            vidas++;
        }
    }

    /**
     * Activa el escudo de invencibilidad por un tiempo determinado.
     * @param duracion duracion en segundos
     */
    public void activarEscudo(float duracion) {
        invencible = true;
        tiempoInvencible = duracion;
    }

    public void sumarPuntos(int cantidad) {
        puntos += cantidad;
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    public boolean estaHerido() {
        return herido;
    }

    public boolean estaInvencible() {
        return invencible;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (invencible) {
            // efecto dorado parpadeante cuando esta invencible
            batch.setColor(1, 1, 0, 0.8f);
            batch.draw(getTexture(), getX(), getY());
            batch.setColor(Color.WHITE);
        } else if (!herido) {
            batch.draw(getTexture(), getX(), getY());
        } else {
            batch.draw(getTexture(), getX(), getY() + MathUtils.random(-5, 5));
            tiempoHerido--;
            if (tiempoHerido <= 0) {
                herido = false;
            }
        }
    }

    @Override
    public void update() {
        // movimiento desde teclado
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            setX(getX() - velocidadX * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            setX(getX() + velocidadX * Gdx.graphics.getDeltaTime());
        }

        // limites de pantalla
        if (getX() < 0) setX(0);
        if (getX() > 800 - getWidth()) setX(800 - getWidth());

        // actualizar invencibilidad
        if (invencible) {
            tiempoInvencible -= Gdx.graphics.getDeltaTime();
            if (tiempoInvencible <= 0) {
                invencible = false;
            }
        }
    }

    @Override
    public void dispose() {
        getTexture().dispose();
    }

    // ==================== Getters y Setters (GM-5) ====================

    public int getVidas() { return vidas; }
    public void setVidas(int vidas) { this.vidas = vidas; }
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public int getVelocidadX() { return velocidadX; }
    public void setVelocidadX(int velocidadX) { this.velocidadX = velocidadX; }
}