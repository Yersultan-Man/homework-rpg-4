package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            throw new IllegalArgumentException("Teams and skills cannot be null");
        }

        RaidResult result = new RaidResult();
        int round = 0;
        final int MAX_ROUNDS = 100;

        result.addLine("Raid started: " + teamA.getName() + " vs " + teamB.getName());

        while (teamA.isAlive() && teamB.isAlive() && round < MAX_ROUNDS) {
            round++;
            result.addLine("\nRound " + round + " ---------------");

            // Ход команды A
            if (teamA.isAlive()) {
                result.addLine(teamA.getName() + " uses " + teamASkill.getSkillName() + " (" + teamASkill.getEffectName() + ")");
                int powerBefore = teamB.getHealth();
                teamASkill.cast(teamB);
                int damageDealt = powerBefore - teamB.getHealth();
                result.addLine("→ Dealt " + damageDealt + " damage to " + teamB.getName() + " (HP left: " + teamB.getHealth() + ")");
            }

            // Ход команды B (если ещё жива)
            if (teamB.isAlive()) {
                result.addLine(teamB.getName() + " uses " + teamBSkill.getSkillName() + " (" + teamBSkill.getEffectName() + ")");
                int powerBefore = teamA.getHealth();
                teamBSkill.cast(teamA);
                int damageDealt = powerBefore - teamA.getHealth();
                result.addLine("→ Dealt " + damageDealt + " damage to " + teamA.getName() + " (HP left: " + teamA.getHealth() + ")");
            }
        }

        result.setRounds(round);

        if (teamA.isAlive()) {
            result.setWinner(teamA.getName());
            result.addLine("\nVICTORY: " + teamA.getName() + " wins!");
        } else if (teamB.isAlive()) {
            result.setWinner(teamB.getName());
            result.addLine("\nVICTORY: " + teamB.getName() + " wins!");
        } else {
            result.setWinner("Draw");
            result.addLine("\nDRAW after " + round + " rounds");
        }

        return result;
    }
}