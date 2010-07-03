package org.ssg.android.game.herogame.util;

import org.ssg.android.game.herogame.Role;

public class BattleUtil {

	public static void attack(Role attacker, Role defencer) {
		// 命中
		int damage;
		boolean hit_result = (Math.random() * 100 < attacker.hit);
		if (hit_result) {

			// 攻击力
			int atk = Math.max(attacker.getAttack() - defencer.getDefence(), 0);
			damage = atk * (20 + attacker.getStrength()) / 20;

			// 属性修正
			damage *= elements_correct(attacker.element_set);
			damage /= 100;

			// 会心一击修正
			if (damage > 0) {
				if (Math.random() * 100 < 4 * attacker.dex / defencer.agi) {
					damage *= 2;
					attacker.critical = true;
				}
			}

			// 防御修正
			if (defencer.guarding) {
				damage /= 2;
			}

			// 分散
			if (Math.abs(damage) > 0) {
				int amp = Math.max(Math.abs(damage) * 15 / 100, 1);
				damage += Math.random() * (amp + 1) + Math.random() * (amp + 1)
						- amp;
			}

			defencer.hp -= damage;
		} else {
			damage = -1;
		}
	}

	public static int elements_correct(int element_set) {
		return 100;
	}
}
