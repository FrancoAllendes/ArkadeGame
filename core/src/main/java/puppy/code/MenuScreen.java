package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen extends BaseScreen {

    private Texture fondoTexture;
    private GlyphLayout layout;

    public MenuScreen(GameLluvia game) {
        super(game);
    }

    @Override
    public void show() {
        fondoTexture = new Texture(Gdx.files.internal("fondo.png"));
        layout = new GlyphLayout();
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.justTouched()) {
            GameManager.getInstance().resetear();
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    protected void update(float delta) {
    }

    @Override
    protected void draw(SpriteBatch batch) {
        batch.draw(fondoTexture, 0, 0, 800, 480);
    }

    @Override
    protected void drawUI(SpriteBatch batch) {
        String titulo = "ARK ARCADE";
        layout.setText(font, titulo);
        font.draw(batch, titulo, (800 - layout.width) / 2, 300);

        String sub1 = "Sobrevive recolectando recursos";
        layout.setText(font, sub1);
        font.draw(batch, sub1, (800 - layout.width) / 2, 250);

        String sub2 = "y esquivando dinosaurios!";
        layout.setText(font, sub2);
        font.draw(batch, sub2, (800 - layout.width) / 2, 225);

        String instruccion = "Presiona ENTER para jugar";
        layout.setText(font, instruccion);
        font.draw(batch, instruccion, (800 - layout.width) / 2, 150);
    }

    @Override
    public void dispose() {
        if (fondoTexture != null) fondoTexture.dispose();
    }
}