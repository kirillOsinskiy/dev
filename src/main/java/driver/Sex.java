/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

/**
 *
 * @author Kirill
 */
public enum Sex {
    MALE {
        @Override
        public String toString() {
            return "М";
        }
    },
    FEMALE {
        @Override
        public String toString() {
            return "Ж";
        }
    };    
}
