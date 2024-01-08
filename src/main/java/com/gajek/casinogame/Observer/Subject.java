package com.gajek.casinogame.Observer;

public abstract class Subject {
    public abstract void attach(IObserver IObserver);
    public abstract void detach(IObserver IObserver);
    public abstract void notifyObservers();
}
