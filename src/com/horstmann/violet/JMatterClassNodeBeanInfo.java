/*
Violet - A program for editing UML diagrams.

Copyright (C) 2002 Cay S. Horstmann (http://horstmann.com)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.horstmann.violet;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
   The bean info for the ClassNode type.
*/
public class JMatterClassNodeBeanInfo extends SimpleBeanInfo
{
   public PropertyDescriptor[] getPropertyDescriptors()
   {
      try
      {
          
          PropertyDescriptor abstractClass 
            = new PropertyDescriptor("abstractClass", JMatterClassNode.class);
         abstractClass.setValue("priority", new Integer(1));
                  
         PropertyDescriptor nameDescriptor 
            = new PropertyDescriptor("name", JMatterClassNode.class);
         nameDescriptor.setValue("priority", new Integer(2));
         
         PropertyDescriptor annotations 
            = new PropertyDescriptor("annotations", JMatterClassNode.class);
         annotations.setValue("priority", new Integer(3));         
         
         
         PropertyDescriptor attributesDescriptor
            = new PropertyDescriptor("attributes", JMatterClassNode.class);
         attributesDescriptor.setValue("priority", new Integer(4));
//         PropertyDescriptor methodsDescriptor
//            = new PropertyDescriptor("methods", JMatterClassNode.class);
//         methodsDescriptor.setValue("priority", new Integer(5));
         return new PropertyDescriptor[]
            {
               abstractClass,
               nameDescriptor,
               annotations,
               attributesDescriptor
              // methodsDescriptor
            };
      }
      catch (IntrospectionException exception)
      {
         return null;
      }
   }
}

