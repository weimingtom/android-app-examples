package org.ssg.android.game.herogame;

public abstract class Role extends Sprite {

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    private int hp; // 生命力
    private int attack; // 攻击力
    private int defence;// 防御力

    public Role(String fileName, int x, int y, int w, int h, BackGroundMap map, int hp,
            int attack, int defence) {
        super(fileName, x, y, w, h, map);
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
    }

}
