package StartMenu;

import game.Controller;
import game.GameWindow;
import game.Main;
import game.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static StartMenu.MainMenuTest.wow;

import static game.World.controller;

public class ButtonListener implements ActionListener {

private int number;




    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("The game has begun");
        wow.dispose();

        new World();
    }

    public int getNumber() {
        return number;
    }
}
