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
        
            int count = 0;
            while(true){
                yield.accept(count++);
            }
        });
        while(true){
            System.out.println(testRoutine.next());
            Thread.sleep(800);
        }
    }
}
