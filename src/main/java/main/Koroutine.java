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
 * Kinda of a - Coroutine. Uses a separate thread which acts like a coroutine.
 * @author jesse
 */
public class Koroutine<T> {
    private Thread thread = null;
    private Consumer<Consumer<T>> routine;
    private T result = null;
    private boolean pauseRoutine = true;
    private boolean complete = false;
    
    private synchronized void raiseComplete(){
        complete = true;
        notify();
    }
    
    public Koroutine(Consumer<Consumer<T>> routine){
        if (routine == null){
            throw new NullPointerException();
        }
        this.routine = routine;
    }
    
    private synchronized void Yield(T result){
        this.result = result;
        pauseRoutine = true;
        notify();
        while(pauseRoutine){
            try{
                wait();
            }
            catch(InterruptedException e){}
        }
        
    }
    
    public boolean notComplete(){
        return !complete;
    }
    public boolean isComplete(){
        return complete;
    }
    
    public boolean started(){
        return thread != null;
    }
    
    public synchronized void start() throws InterruptedException{
        if (thread == null){
            thread = new Thread(() -> {
                while(pauseRoutine){
                    try{
                        wait();
                    } catch(InterruptedException e){}
                }
                
                routine.accept(r -> Yield(r));
                
                raiseComplete();
            });
            
            thread.start();
            pauseRoutine = false;
            notify();
            while(!pauseRoutine && !complete){
                wait();
            }
        }
    }
    
    public static class KoroutineNotStartedException extends RuntimeException{
        public KoroutineNotStartedException(){
            super("Koroutine was not started yet.");
        }
    }
    
    public synchronized T await() throws InterruptedException{
        if (!started()){
            throw new KoroutineNotStartedException();
        }
        T result = this.result;
        
        pauseRoutine = false;
        notify();
        while(!pauseRoutine && !complete){
            wait();
        }
        
        return result;
    }
}
