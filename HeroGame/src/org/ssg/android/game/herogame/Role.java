package org.ssg.android.game.herogame;

import org.ssg.android.game.herogame.control.BackGroundMap;

public abstract class Role extends Sprite {


    public int hp; // 生命力
    public int attack; // 攻击力
    public int defence;// 防御力
    public int maxHP;
    
    public int hit;
	public int strength;
	public int element_set;
	public boolean critical;
	public int dex;
	public int agi;
	public boolean guarding;
	
    public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}
    
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0)
            this.hp = 0;
        else
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

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
    
    public Role(String fileName, int x, int y, int w, int h, BackGroundMap map, int hp,
            int attack, int defence) {
        super(fileName, x, y, w, h, map);
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        maxHP = hp;
    }

}
