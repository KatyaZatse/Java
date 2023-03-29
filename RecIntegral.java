/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.numberaddition;

import javax.swing.JOptionPane;


/**
 *
 * @author zakat
 */
public class RecIntegral {
    
     public double down;
     public double up;
     public double step;
     public double res;

    public double getDown() {
        return down;
    }

    public void setDown(double down) throws MyException {
        if(!prov(down)){
            throw new MyException("Нижняя граница выходит из диапазона");  
       }
        
        this.down = down;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) throws MyException {
        if(!prov(up)){
            throw new MyException("Верхняя граница выходит из диапазона");
        }
        if(this.getDown()>=up){
            throw new MyException("верхняя граница меньше чем нижняя");
        }
        this.up = up;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) throws MyException {
        if(!prov(step)){
            throw new MyException("Шаг выходит из диапазона");
        }
        if(step>(this.getUp()-this.getDown())){
            throw new MyException("Шаг перевашает");
        }
        this.step = step;
    }

    public double getRes() {
        return res;
    }

    public void setRes(double res) throws MyException {
        if(!prov(res)){
            throw new MyException("Res is incorrect");
        }
        this.res = res;
    }
    public boolean prov(double a){
        return a>0.000001 && a<1000000;
    } 
     
     public RecIntegral(Object down,Object up,Object step,Object res) throws MyException{
        try {
            this.setDown(Double.parseDouble(down.toString()));
            this.setUp(Double.parseDouble(up.toString()));
            this.setStep(Double.parseDouble(step.toString()));
            this.setRes(Double.parseDouble(res.toString()));
        } catch (NumberFormatException e) {
            throw new MyException("ошибка встречен символ или ,", e);
        }catch (MyException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"err",JOptionPane.ERROR_MESSAGE);
        }
         
     }
      public RecIntegral(Object down,Object up,Object step) throws MyException{
        try {
            this.setDown(Double.parseDouble(down.toString()));
            this.setUp(Double.parseDouble(up.toString()));
            this.setStep(Double.parseDouble(step.toString()));
        } catch (NumberFormatException e) {
            throw new MyException("ошибка встречен символ или ,", e);
        }catch (MyException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"err",JOptionPane.ERROR_MESSAGE);

        }
         
         
     }
}


    

