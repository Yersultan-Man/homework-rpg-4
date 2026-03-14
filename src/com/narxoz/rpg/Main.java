package com.narxoz.rpg;

import com.narxoz.rpg.battle.RaidEngine;
import com.narxoz.rpg.battle.RaidResult;
import com.narxoz.rpg.bridge.*;
import com.narxoz.rpg.composite.CombatNode;
import com.narxoz.rpg.composite.EnemyUnit;
import com.narxoz.rpg.composite.HeroUnit;
import com.narxoz.rpg.composite.PartyComposite;
import com.narxoz.rpg.composite.RaidGroup;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 4 Demo: Bridge + Composite ===\n");


        HeroUnit warrior = new HeroUnit("Arthas", 140, 30);
        HeroUnit mage    = new HeroUnit("Jaina",  90,  40);
        HeroUnit healer  = new HeroUnit("Uther",  80,  15);


        PartyComposite heroes = new PartyComposite("Heroes Party");
        heroes.add(warrior);
        heroes.add(mage);
        heroes.add(healer);

        EnemyUnit goblin1 = new EnemyUnit("Goblin Scout",  60, 18);
        EnemyUnit goblin2 = new EnemyUnit("Goblin Archer", 50, 22);
        EnemyUnit orc     = new EnemyUnit("Orc Warrior",  120, 28);
        EnemyUnit boss    = new EnemyUnit("Orc Chieftain", 200, 35);


        PartyComposite frontline = new PartyComposite("Enemy Frontline");
        frontline.add(goblin1);
        frontline.add(goblin2);
        frontline.add(orc);

        RaidGroup enemies = new RaidGroup("Enemy Raid");
        enemies.add(frontline);
        enemies.add(boss);


        System.out.println("--- Team Structures ---");
        heroes.printTree("");
        System.out.println();
        enemies.printTree("");
        System.out.println();

        Skill warriorSlash   = new SingleTargetSkill("Mortal Strike", 35, new PhysicalEffect());
        Skill mageFireball   = new SingleTargetSkill("Fireball",      50, new FireEffect());
        Skill jainaBlizzard  = new AreaSkill(     "Blizzard",        28, new IceEffect());
        Skill shadowNova     = new AreaSkill(     "Shadow Nova",     40, new ShadowEffect());

        System.out.println("--- Available Skills (Bridge combinations) ---");
        System.out.println("• " + warriorSlash.getSkillName()   + " (" + warriorSlash.getEffectName()   + ")");
        System.out.println("• " + mageFireball.getSkillName()   + " (" + mageFireball.getEffectName()   + ")");
        System.out.println("• " + jainaBlizzard.getSkillName()  + " (" + jainaBlizzard.getEffectName()  + ")");
        System.out.println("• " + shadowNova.getSkillName()     + " (" + shadowNova.getEffectName()     + ")");
        System.out.println();

        // Запускаем бой
        System.out.println("=== Starting Raid Simulation ===\n");

        RaidEngine engine = new RaidEngine().setRandomSeed(42L);

        // Вариант 1: герои используют AOE + single-target
        RaidResult result1 = engine.runRaid(heroes, enemies, jainaBlizzard, shadowNova);

        System.out.println("\n--- Raid Result (Heroes use Blizzard vs Enemies use Shadow Nova) ---");
        System.out.println("Winner: " + result1.getWinner());
        System.out.println("Rounds: " + result1.getRounds());
        System.out.println("Log:");
        for (String line : result1.getLog()) {
            System.out.println("  " + line);
        }
        System.out.println("\n=== Demo Complete ===");
    }
}