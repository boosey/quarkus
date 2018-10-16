package org.jboss.shamrock.deployment;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.shamrock.runtime.RegisterForReflection;

public class RegisterForReflectionProcessor implements ResourceProcessor {
    @Override
    public void process(ArchiveContext archiveContext, ProcessorContext processorContext) throws Exception {
        for (AnnotationInstance i : archiveContext.getCombinedIndex().getAnnotations(DotName.createSimple(RegisterForReflection.class.getName()))) {
            ClassInfo target = i.target().asClass();
            boolean methods = i.value("methods") == null || i.value("methods").asBoolean();
            boolean fields = i.value("fields") == null || i.value("fields").asBoolean();
            processorContext.addReflectiveClass(methods, fields, target.name().toString());
        }

        //TODO: where should stuff like this go?
        //this will hold a heap of memory when it is initialized, and is rarely used (but it generally on the analysis path)
        processorContext.addRuntimeInitializedClasses("com.sun.org.apache.xml.internal.serializer.ToHTMLStream");
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
