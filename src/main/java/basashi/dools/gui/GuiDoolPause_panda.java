package basashi.dools.gui;

import basashi.dools.entity.EntityDool;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.PandaEntity;

public class GuiDoolPause_panda extends GuiDoolPause {
	public PandaEntity entity;
	private int variant = 0;

	public GuiDoolPause_panda(EntityDool entityfigure) {
		super(entityfigure);
		entity = (PandaEntity)entityfigure.renderEntity;
	}

	@Override
	public void init() {
		super.init();
		PandaEntity.Type main = entity.getMainGene();
		PandaEntity.Type hide = entity.getHiddenGene();
		this.addButton(new Button(width / 2 - 140, height / 6 + 0 + 12, 80, 20, "Main "+ main.getName(), (bt)->{actionPerformed(101, bt);}));
		this.addButton(new Button(width / 2 - 140, height / 6 + 24 + 12, 80, 20, "Hide "+ hide.getName(), (bt)->{actionPerformed(102, bt);}));
	}

	@Override
	protected void actionPerformed(int id, Button button) {
		switch (id) {
		case 101:
			PandaEntity.Type main = entity.getMainGene();
			entity.setMainGene(PandaEntity.Type.byIndex(main.getIndex() + 1));
			button.setMessage("Main "+ entity.getMainGene().getName());
			break;
		case 102:
			PandaEntity.Type hide = entity.getHiddenGene();
			entity.setHiddenGene(PandaEntity.Type.byIndex(hide.getIndex() + 1));
			button.setMessage("Hide "+ entity.getHiddenGene().getName());
			break;
		}
		super.actionPerformed(id, button);
	}
}
