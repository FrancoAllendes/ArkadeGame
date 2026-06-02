package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Clase que representa un dinosaurio aliado que sigue al Survivor
 * y atrae recursos como un iman.
 * Extiende GameObject. Funcionalidad propia del juego.
 */
public class DinoAliado extends GameObject {

    private Survivor survivor;
    private float tiempoRestante;
    private boolean activo;
    private float velocidadSeguimiento;
    private float radioIman;

    /**
     * Constructor del DinoAliado.
     * @param texture textura del dinosaurio
     * @param survivor el superviviente al que sigue
     * @param duracion tiempo de vida en segundos
     */
    public DinoAliado(Texture texture, Survivor survivor, float duracion) {
        super(texture, survivor.getX() + 130, survivor.getY(), 64, 64);
        this.survivor = survivor;
        this.tiempoRestante = duracion;
        this.activo = true;
        this.velocidadSeguimiento = 350f;
        this.radioIman = 150f;
    }

    /**
     * Verifica si un drop esta dentro del radio de iman.
     * @param dropX posicion X del drop
     * @param dropY posicion Y del drop
     * @return true si esta dentro del radio
     */
    public boolean enRadioIman(float dropX, float dropY) {
        float dx = (getX() + getWidth() / 2) - (dropX + 32);
        float dy = (getY() + getHeight() / 2) - (dropY + 32);
        float distancia = (float) Math.sqrt(dx * dx + dy * dy);
        return distancia < radioIman;
    }

    public boolean estaActivo() {
        return activo;
    }

    public float getTiempoRestante() {
        return tiempoRestante;
    }

    public float getRadioIman() {
        return radioIman;
    }

    public void setRadioIman(float radioIman) {
        this.radioIman = radioIman;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (activo) {
            batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void update() {
        if (!activo) return;

        float delta = Gdx.graphics.getDeltaTime();

        tiempoRestante -= delta;
        if (tiempoRestante <= 0) {
            activo = false;
            return;
        }

        float objetivoX = survivor.getX() + 130;
        float objetivoY = survivor.getY();

        float diffX = objetivoX - getX();
        float diffY = objetivoY - getY();

        if (Math.abs(diffX) > 2) {
            setX(getX() + Math.signum(diffX) * velocidadSeguimiento * delta);
        }
        if (Math.abs(diffY) > 2) {
            setY(getY() + Math.signum(diffY) * velocidadSeguimiento * delta);
        }

        if (getX() < 0) setX(0);
        if (getX() > 800 - getWidth()) setX(800 - getWidth());
    }

    @Override
    public void dispose() {
    }
}