package main;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jesse
 */
public class Koroutine<T> {
    private Thread thread = null;
    private Consumer<Consumer<T>> routine;
    private T result = null;
    private Lock lock = new ReentrantLock();
    private boolean ready = true;
    
    public Koroutine(Consumer<Consumer<T>> routine){
        if (routine == null){
            throw new NullPointerException();
        }
        this.routine = routine;
        lock.lock();
    }
    
    private synchronized void Yield(T result){
        this.result = result;
        ready = true;
        while(ready){
            try{
                notify();
                wait();
            }
            catch(InterruptedException e){}
        }
        
    }
    
    public synchronized T next()throws InterruptedException{
        if (thread == null){
            thread = new Thread(() -> {
                while(ready){
                    try{
                        notify();
                    wait();
                    } catch(InterruptedException e){}
                }
                routine.accept((r) -> {Yield(r);});});
            thread.start();
        }
        ready = false;
        while(!ready){
        notify();
            wait();
        }
        return result;
    }
}
