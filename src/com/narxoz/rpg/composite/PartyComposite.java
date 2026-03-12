package com.narxoz.rpg.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PartyComposite implements CombatNode {
    private final String name;
    private final List<CombatNode> children = new ArrayList<>();

    public PartyComposite(String name) {
        this.name = name;
    }

    public void add(CombatNode node) {
        children.add(node);
    }

    public void remove(CombatNode node) {
        children.remove(node);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return children.stream()
                .mapToInt(CombatNode::getHealth)
                .sum();
    }

    @Override
    public int getAttackPower() {
        return getAliveChildren().stream()
                .mapToInt(CombatNode::getAttackPower)
                .sum();
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0 || !isAlive()) {
            return;
        }

        List<CombatNode> alive = getAliveChildren();
        if (alive.isEmpty()) {
            return;
        }

        // Простое равномерное распределение урона
        int damagePerUnit = amount / alive.size();
        int remainder = amount % alive.size();

        for (int i = 0; i < alive.size(); i++) {
            int dmg = damagePerUnit + (i < remainder ? 1 : 0);
            alive.get(i).takeDamage(dmg);
        }
    }

    @Override
    public boolean isAlive() {
        return children.stream().anyMatch(CombatNode::isAlive);
    }

    @Override
    public List<CombatNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void printTree(String indent) {
        int totalHP = getHealth();
        int totalATK = getAttackPower();
        System.out.println(indent + "+ " + name + " [HP=" + totalHP + ", ATK=" + totalATK + "]");

        String childIndent = indent + "  ";
        for (CombatNode child : children) {
            child.printTree(childIndent);
        }
    }

    private List<CombatNode> getAliveChildren() {
        return children.stream()
                .filter(CombatNode::isAlive)
                .collect(Collectors.toList());
    }
}