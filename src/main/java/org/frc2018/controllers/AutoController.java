package org.frc2018.controllers;

public class AutoController extends Controller {

    private AutoController() {

    }

    @Override
    public void init() {

    }

    @Override
    public void handle() {

    }

    private static AutoController _instance = new AutoController();

    public static AutoController getInstance() {
        return _instance;
    }
}