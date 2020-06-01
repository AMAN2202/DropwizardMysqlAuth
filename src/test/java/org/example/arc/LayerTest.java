package org.example.arc;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "org.example.empApi")
public class LayerTest {

    static Config config = new Config();
    static Architectures.LayeredArchitecture layeredArchitecture;


    static {

        layeredArchitecture = layeredArchitecture();



        /*
        Define layer for architecture
         */
        Iterator<Map.Entry<String, List<String>>> itr = config.getLayers().entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, List<String>> entry = itr.next();

            layeredArchitecture.layer(entry.getKey()).definedBy(entry.getValue().toArray(new String[entry.getValue().size()]));
        }






        /*
        Define acess rules for architecture
         */
        itr = config.getAcessRules().entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, List<String>> entry = itr.next();

            if (entry.getValue().isEmpty())
                layeredArchitecture.whereLayer(entry.getKey()).mayNotBeAccessedByAnyLayer();
            else
                layeredArchitecture.whereLayer(entry.getKey()).mayOnlyBeAccessedByLayers(entry.getValue().toArray(new String[entry.getValue().size()]));

        }


        /*
        Add exceptions for certain classes
         */

        Iterator<Map.Entry<String, String>> itr2 = config.getIgnoreClasses().entrySet().iterator();
        while (itr2.hasNext()) {
            Map.Entry<String, String> entry = itr2.next();

            Class classUsed=getClassForName(entry.getKey());
            Class voilatingClass=getClassForName(entry.getValue());
            if(classUsed!=null && voilatingClass!=null)
            {
                System.out.println(classUsed.getSimpleName()+" "+voilatingClass.getSimpleName());
                layeredArchitecture.ignoreDependency(classUsed,voilatingClass);
            }
        }



    }



    /*
   Return objrct of type class for a given string or null if unable to create class object
    */
    private static Class getClassForName(String className) {
        Class classForAnnotation = null;
        try {
            classForAnnotation = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return classForAnnotation;
        }
    }

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture;


}