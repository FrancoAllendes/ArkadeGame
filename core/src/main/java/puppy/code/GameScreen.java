package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Pantalla principal donde ocurre el juego.
 * Extiende BaseScreen aplicando Template Method.
 */
public class GameScreen extends BaseScreen {

    private Survivor survivor;
    private Array<DropItem> drops;
    private long lastDropTime;

    // dino aliado
    private DinoAliado dinoAliado;
    private Texture dinoAliadoTexture;
    private int siguienteDinoEnPuntos;

    // tek cave (zona secreta)
    private boolean enTekCave;
    private float tiempoTekCave;
    private int supplyDropsRecolectados;
    private Texture fondoTekTexture;
    private Texture elementoTexture;
    private Texture enemigoTekTexture;
    private Sound sonidoTek;

    // texturas normales
    private Texture survivorTexture;
    private Texture recursoTexture;
    private Texture carneTexture;
    private Texture metalTexture;
    private Texture peligroTexture;
    private Texture vidaTexture;
    private Texture escudoTexture;
    private Texture supplyDropTexture;
    private Texture fondoTexture;

    // sonidos
    private Sound sonidoRecolectar;
    private Sound sonidoDanio;
    private Sound sonidoVida;
    private Sound sonidoEscudo;
    private Music musicaFondo;

    // pausa y volumen
    private boolean pausado;
    private float volumen;
    private GlyphLayout layout;

    public GameScreen(GameLluvia game) {
        super(game);
    }

    @Override
    public void show() {
        // texturas normales
        fondoTexture = new Texture(Gdx.files.internal("fondo.png"));
        survivorTexture = new Texture(Gdx.files.internal("survivor.png"));
        recursoTexture = new Texture(Gdx.files.internal("recurso.png"));
        carneTexture = new Texture(Gdx.files.internal("carne.png"));
        metalTexture = new Texture(Gdx.files.internal("metal.png"));
        peligroTexture = new Texture(Gdx.files.internal("peligro.png"));
        vidaTexture = new Texture(Gdx.files.internal("vida.png"));
        escudoTexture = new Texture(Gdx.files.internal("escudo.png"));
        supplyDropTexture = new Texture(Gdx.files.internal("supply_drop.png"));
        dinoAliadoTexture = new Texture(Gdx.files.internal("dino_aliado.png"));

        // texturas tek cave
        fondoTekTexture = new Texture(Gdx.files.internal("fondo_tek.png"));
        elementoTexture = new Texture(Gdx.files.internal("elemento.png"));
        enemigoTekTexture = new Texture(Gdx.files.internal("enemigo_tek.png"));

        // sonidos
        sonidoRecolectar = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        sonidoDanio = Gdx.audio.newSound(Gdx.files.internal("danio.mp3"));
        sonidoVida = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        sonidoEscudo = Gdx.audio.newSound(Gdx.files.internal("recolectar.mp3"));
        sonidoTek = Gdx.audio.newSound(Gdx.files.internal("sonido_tek.mp3"));
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

        volumen = 0.5f;
        musicaFondo.setVolume(volumen);
        musicaFondo.setLooping(true);
        musicaFondo.play();

        pausado = false;
        layout = new GlyphLayout();

        // tek cave
        enTekCave = false;
        tiempoTekCave = 0;
        supplyDropsRecolectados = 0;

        // crear jugador
        survivor = new Survivor(survivorTexture, sonidoDanio);
        drops = new Array<>();
        dinoAliado = null;
        siguienteDinoEnPuntos = 200;
        spawnDrop();
    }

    private void spawnDrop() {
        GameManager gm = GameManager.getInstance();
        DropItem drop;

        if (enTekCave) {
            // en tek cave: 55% elemento, 25% enemigo tek, 10% vida, 10% escudo
            int random = MathUtils.random(1, 100);
            if (random <= 25) {
                drop = DropBuilder.crearEnemigoTek(enemigoTekTexture, gm.getVelocidadBase());
            } else if (random <= 35) {
                drop = DropBuilder.crearVida(vidaTexture, gm.getVelocidadBase(), sonidoVida);
            } else if (random <= 45) {
                drop = DropBuilder.crearEscudo(escudoTexture, gm.getVelocidadBase(), sonidoEscudo);
            } else {
                drop = DropBuilder.crearElemento(elementoTexture, gm.getVelocidadBase(), sonidoRecolectar);
            }
        } else {
            // isla normal
            int random = MathUtils.random(1, 100);
            if (random <= 3) {
                // 3% supply drop
                drop = DropBuilder.crearSupplyDrop(supplyDropTexture, gm.getVelocidadBase(), sonidoRecolectar);
            } else if (random <= 6) {
                // 3% escudo
                drop = DropBuilder.crearEscudo(escudoTexture, gm.getVelocidadBase(), sonidoEscudo);
            } else if (random <= 14) {
                // 8% vida
                drop = DropBuilder.crearVida(vidaTexture, gm.getVelocidadBase(), sonidoVida);
            } else if (random <= 38) {
                // 24% peligro
                drop = DropBuilder.crearPeligro(peligroTexture, gm.getVelocidadBase());
            } else {
                // 62% recurso normal
                Texture[] recursos = {recursoTexture, carneTexture, metalTexture};
                Texture recursoAleatorio = recursos[MathUtils.random(0, 2)];
                drop = DropBuilder.crearRecurso(recursoAleatorio, gm.getVelocidadBase(), sonidoRecolectar);
            }
        }

        drops.add(drop);
        lastDropTime = TimeUtils.nanoTime();
    }

    /**
     * Activa la zona secreta Tek Cave.
     * Limpia la pantalla y cambia el modo de juego por 30 segundos.
     */
    private void activarTekCave() {
        enTekCave = true;
        tiempoTekCave = 15f;
        supplyDropsRecolectados = 0;

        // limpiar todos los drops actuales
        drops.clear();

        // sonido de entrada
        sonidoTek.play();
    }

    /**
     * Desactiva la zona Tek Cave y vuelve a la isla normal.
     */
    private void desactivarTekCave() {
        enTekCave = false;
        drops.clear();
    }

    private void verificarDinoAliado() {
        GameManager gm = GameManager.getInstance();
        if (gm.getPuntaje() >= siguienteDinoEnPuntos && dinoAliado == null) {
            dinoAliado = new DinoAliado(dinoAliadoTexture, survivor, 12f);
            siguienteDinoEnPuntos += 200;
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pausado = !pausado;
            if (pausado) {
                musicaFondo.pause();
            } else {
                musicaFondo.play();
            }
        }

        if (pausado) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                volumen = Math.min(volumen + 0.1f, 1.0f);
                musicaFondo.setVolume(volumen);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                volumen = Math.max(volumen - 0.1f, 0.0f);
                musicaFondo.setVolume(volumen);
            }
        }
    }

    @Override
    protected void update(float delta) {
        if (pausado) return;

        GameManager gm = GameManager.getInstance();
        survivor.update();

        // verificar dino aliado
        verificarDinoAliado();
        if (dinoAliado != null) {
            if (dinoAliado.estaActivo()) {
                dinoAliado.update();
            } else {
                dinoAliado = null;
            }
        }

        // actualizar timer de tek cave
        if (enTekCave) {
            tiempoTekCave -= delta;
            if (tiempoTekCave <= 0) {
                desactivarTekCave();
            }
        }

        // generar drops
        if (TimeUtils.nanoTime() - lastDropTime > gm.getIntervaloSpawn()) {
            spawnDrop();
        }

        // actualizar drops y colisiones
        for (int i = 0; i < drops.size; i++) {
            DropItem drop = drops.get(i);
            drop.update();

            // efecto iman del dino aliado: atrae recursos cercanos
            if (dinoAliado != null && dinoAliado.estaActivo()
                    && (drop instanceof ResourceDrop || drop instanceof SupplyDrop)
                    && dinoAliado.enRadioIman(drop.getX(), drop.getY())) {
                float dx = (dinoAliado.getX() + 32) - (drop.getX() + 32);
                float dy = (dinoAliado.getY() + 32) - (drop.getY() + 32);
                drop.setX(drop.getX() + dx * 3f * delta);
                drop.setY(drop.getY() + dy * 3f * delta);
            }

            if (drop.fueraDePantalla()) {
                drops.removeIndex(i);
                i--;
                continue;
            }

            // colision con survivor
            if (drop.checkCollision(survivor)) {
                // contar supply drops
                if (drop instanceof SupplyDrop && !enTekCave) {
                    supplyDropsRecolectados++;
                }

                drop.onPlayerCollision(survivor);
                gm.setPuntaje(survivor.getPuntos());
                gm.setVidas(survivor.getVidas());
                drops.removeIndex(i);
                i--;

                // verificar si se activa tek cave
                if (supplyDropsRecolectados >= 3 && !enTekCave) {
                    activarTekCave();
                    break;
                }
                continue;
            }

            // colision del dino aliado con recursos
            if (dinoAliado != null && dinoAliado.estaActivo()
                    && (drop instanceof ResourceDrop || drop instanceof SupplyDrop)
                    && drop.checkCollision(dinoAliado)) {
                if (drop instanceof SupplyDrop && !enTekCave) {
                    supplyDropsRecolectados++;
                }
                drop.onPlayerCollision(survivor);
                gm.setPuntaje(survivor.getPuntos());
                drops.removeIndex(i);
                i--;

                if (supplyDropsRecolectados >= 3 && !enTekCave) {
                    activarTekCave();
                    break;
                }
            }
        }

        if (!survivor.estaVivo()) {
            musicaFondo.stop();
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // dibujar fondo segun zona
        if (enTekCave) {
            batch.draw(fondoTekTexture, 0, 0, 800, 480);
        } else {
            batch.draw(fondoTexture, 0, 0, 800, 480);
        }

        survivor.draw(batch);

        if (dinoAliado != null && dinoAliado.estaActivo()) {
            dinoAliado.draw(batch);
        }

        for (DropItem drop : drops) {
            drop.draw(batch);
        }
    }

    @Override
    protected void drawUI(SpriteBatch batch) {
        GameManager gm = GameManager.getInstance();
        font.draw(batch, "Puntos: " + gm.getPuntaje(), 10, 470);
        font.draw(batch, "Vidas: " + gm.getVidas(), 710, 470);
        font.draw(batch, "Nivel: " + gm.getNivel(), 370, 470);

        // supply drops debajo de puntos
        if (!enTekCave) {
            font.draw(batch, "Supply Drops: " + supplyDropsRecolectados + "/3", 10, 450);
        }

        // escudo activo centrado abajo
        if (survivor.estaInvencible()) {
            String escudo = "ESCUDO ACTIVO!";
            layout.setText(font, escudo);
            font.draw(batch, escudo, (800 - layout.width) / 2, 30);
        }

        // dino aliado centrado abajo
        if (dinoAliado != null && dinoAliado.estaActivo()) {
            String dinoTexto = "DINO ALIADO! " + (int) dinoAliado.getTiempoRestante() + "s";
            layout.setText(font, dinoTexto);
            font.draw(batch, dinoTexto, (800 - layout.width) / 2, 50);
        }

        // tek cave debajo de vidas (derecha)
        if (enTekCave) {
            font.draw(batch, "TEK CAVE! " + (int) tiempoTekCave + "s", 710, 450);
        }

        // menu de pausa
        if (pausado) {
            String pausa = "PAUSA";
            layout.setText(font, pausa);
            font.draw(batch, pausa, (800 - layout.width) / 2, 300);

            int porcentaje = Math.round(volumen * 100);
            String vol = "Volumen: " + porcentaje + "%";
            layout.setText(font, vol);
            font.draw(batch, vol, (800 - layout.width) / 2, 260);

            String barra = "[";
            int bloques = Math.round(volumen * 10);
            for (int i = 0; i < 10; i++) {
                barra += (i < bloques) ? "=" : " ";
            }
            barra += "]";
            layout.setText(font, barra);
            font.draw(batch, barra, (800 - layout.width) / 2, 235);

            String controles = "Flechas UP/DOWN: volumen";
            layout.setText(font, controles);
            font.draw(batch, controles, (800 - layout.width) / 2, 200);

            String reanudar = "Presiona ESC para continuar";
            layout.setText(font, reanudar);
            font.draw(batch, reanudar, (800 - layout.width) / 2, 160);
        }
    }

    @Override
    public void dispose() {
        musicaFondo.dispose();
        sonidoRecolectar.dispose();
        sonidoDanio.dispose();
        sonidoVida.dispose();
        sonidoEscudo.dispose();
        sonidoTek.dispose();
        survivorTexture.dispose();
        recursoTexture.dispose();
        carneTexture.dispose();
        metalTexture.dispose();
        peligroTexture.dispose();
        vidaTexture.dispose();
        escudoTexture.dispose();
        supplyDropTexture.dispose();
        fondoTexture.dispose();
        fondoTekTexture.dispose();
        elementoTexture.dispose();
        enemigoTekTexture.dispose();
        dinoAliadoTexture.dispose();
    }
}