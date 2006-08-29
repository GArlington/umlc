/*
 * ColorConverter.java
 *
 * Created on August 26, 2006, 11:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package umlc.codeGeneration.jmatter;

import java.awt.Color;
import java.awt.SystemColor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.beanutils.Converter;

/**
 *
 * @author ryan
 */
public class ColorConverter  implements Converter {
    
    /** Creates a new instance of ColorConverter */
    public ColorConverter() {
    }
    
   public Object convert(Class aClass, Object value) {
        if (value != null) {
              String s = value.toString();
              if (s.length() <= 1) {
                  throw new IllegalArgumentException("");
              }
              if (s.charAt(0) == '0' && s.charAt(1) == 'x') {
                  if (s.length() != 8) {
                      throw new IllegalArgumentException("");
                  }
                  int colorValue = 0;
                  try {
                      colorValue = Integer.parseInt(s.substring(2), 16);
                      return new Color(colorValue);
                  }
                  catch (NumberFormatException ex) {
                      throw new IllegalArgumentException(
                          "Can't parse \""
                              + s
                              + "\" as an hexadecimal number: "
                              + ex);
                  }
              }
              else {
                  // a color name
                  try {
                      // could it be this is already somewhere: get the value of  a static final by string
                      Field f = SystemColor.class.getField(s);
                      if (f == null
                          || !Modifier.isStatic(f.getModifiers())
                          || !Modifier.isFinal(f.getModifiers())
                          || !Modifier.isPublic(f.getModifiers())
                          || !Color.class.isAssignableFrom(f.getType())) {
                            throw new IllegalArgumentException("");
                      }
                      return (Color) f.get(SystemColor.class);
                  }
                  catch (Exception ex) {
                      throw new IllegalArgumentException(
                          "Can't parse \"" + s + "\" as a color-name: " + ex);
                  }
              }
          }
          return null;
   }
}
