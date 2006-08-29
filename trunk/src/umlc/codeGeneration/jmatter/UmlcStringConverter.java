/*
 * UmlStringConverter.java
 *
 * Created on August 26, 2006, 10:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.jmatter;

import org.apache.commons.beanutils.Converter;

/**
 *
 * @author ryan
 */
public class UmlcStringConverter implements Converter {
    
    /** Creates a new instance of UmlStringConverter */
    public UmlcStringConverter() {
    }

    public Object convert(Class aClass, Object object) {
        if (object instanceof java.awt.Color) {
            java.awt.Color c = (java.awt.Color)object;
            char[] buf = new char[8];
            buf[0] = '0';
            buf[1] = 'x';
            String s = Integer.toHexString(c.getRed());
            if (s.length() == 1) {
                    buf[2] = '0';
                    buf[3] = s.charAt(0);
            }
            else {
                    buf[2] = s.charAt(0);
                    buf[3] = s.charAt(1);
            }
            s = Integer.toHexString(c.getGreen());
            if (s.length() == 1) {
                    buf[4] = '0';
                    buf[5] = s.charAt(0);
            }
            else {
                    buf[4] = s.charAt(0);
                    buf[5] = s.charAt(1);
            }
            s = Integer.toHexString(c.getBlue());
            if (s.length() == 1) {
                    buf[6] = '0';
                    buf[7] = s.charAt(0);
            }
            else {
                    buf[6] = s.charAt(0);
                    buf[7] = s.charAt(1);
            }
            return String.valueOf(buf);
        }
        else return object.toString();
    }
    
}
