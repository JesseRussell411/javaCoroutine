/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author jesse
 */
public class Driver {
    public static void main(String[] args) throws InterruptedException{
        Koroutine<Integer> testRoutine = new Koroutine<Integer>((yield) -> {
            for(int i = 0; i < 4; ++i){
                yield.accept(i);
            }
        });
        testRoutine.start();
        while(testRoutine.notComplete()){
            System.out.println(testRoutine.await());
            Thread.sleep(800);
        }
    }
}
