package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/**
 * Clase que construye objetos DropItem paso a paso.
 * Implementa el patron Builder (GM-9).
 */
public class DropBuilder {

    private Texture texture;
    private float velocidadY;
    private MovementStrategy strategy;
    private Sound sonido;
    private int puntos;
    private int daño;
    private float duracionEscudo;
    private String tipo;

    public DropBuilder() {
        this.velocidadY = 300f;
        this.strategy = new NormalFall();
        this.puntos = 10;
        this.daño = 1;
        this.duracionEscudo = 3f;
        this.tipo = "recurso";
    }

    public DropBuilder conTextura(Texture texture) {
        this.texture = texture;
        return this;
    }

    public DropBuilder conVelocidad(float velocidadY) {
        this.velocidadY = velocidadY;
        return this;
    }

    public DropBuilder conEstrategia(MovementStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public DropBuilder conSonido(Sound sonido) {
        this.sonido = sonido;
        return this;
    }

    public DropBuilder conPuntos(int puntos) {
        this.puntos = puntos;
        return this;
    }

    public DropBuilder conDaño(int daño) {
        this.daño = daño;
        return this;
    }

    public DropBuilder conDuracionEscudo(float duracion) {
        this.duracionEscudo = duracion;
        return this;
    }

    public DropBuilder comoPeligro() {
        this.tipo = "peligro";
        return this;
    }

    public DropBuilder comoRecurso() {
        this.tipo = "recurso";
        return this;
    }

    public DropBuilder comoVida() {
        this.tipo = "vida";
        return this;
    }

    public DropBuilder comoEscudo() {
        this.tipo = "escudo";
        return this;
    }

    public DropItem build() {
        DropItem drop;
        switch (tipo) {
            case "peligro":
                drop = new DangerDrop(texture, velocidadY, daño);
                break;
            case "vida":
                drop = new HealDrop(texture, velocidadY, sonido);
                break;
            case "escudo":
                drop = new ShieldDrop(texture, velocidadY, sonido, duracionEscudo);
                break;
            default:
                drop = new ResourceDrop(texture, velocidadY, sonido, puntos);
                break;
        }
        drop.setMovementStrategy(strategy);
        return drop;
    }

    // ==================== Metodos de fabrica rapidos ====================

    public static DropItem crearRecurso(Texture texture, float velocidad, Sound sonido) {
        return new DropBuilder()
                .conTextura(texture)
                .conVelocidad(velocidad)
                .conSonido(sonido)
                .conPuntos(10)
                .comoRecurso()
                .build();
    }

    public static DropItem crearPeligro(Texture texture, float velocidad) {
        MovementStrategy strategy;
        int random = MathUtils.random(1, 3);
        if (random == 1) {
            strategy = new ZigZagFall();
        } else if (random == 2) {
            strategy = new FastFall();
        } else {
            strategy = new NormalFall();
        }

        return new DropBuilder()
                .conTextura(texture)
                .conVelocidad(velocidad)
                .comoPeligro()
                .conDaño(1)
                .conEstrategia(strategy)
                .build();
    }

    public static DropItem crearVida(Texture texture, float velocidad, Sound sonido) {
        return new DropBuilder()
                .conTextura(texture)
                .conVelocidad(velocidad)
                .conSonido(sonido)
                .comoVida()
                .build();
    }

    public static DropItem crearEscudo(Texture texture, float velocidad, Sound sonido) {
        return new DropBuilder()
                .conTextura(texture)
                .conVelocidad(velocidad)
                .conSonido(sonido)
                .comoEscudo()
                .conDuracionEscudo(3f)
                .build();
    }
}