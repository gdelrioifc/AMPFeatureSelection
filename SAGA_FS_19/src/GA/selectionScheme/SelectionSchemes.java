/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA.selectionScheme;

import GA.Population;




/**
 *
 * @author beltran
 */
public abstract class SelectionSchemes {
  private String name;

    public SelectionSchemes(String name) {
        this.name = name;
    }
  
  
  public abstract Population select(Population p); 

  public String getName(){return this.name;}
}
