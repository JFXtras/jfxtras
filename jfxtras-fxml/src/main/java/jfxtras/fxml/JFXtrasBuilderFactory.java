/**
 * JFXtrasBuilderFactory.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.fxml;

//import java.util.ServiceLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import jfxtras.fxml.BuilderService;

/**
 * An extended BuilderFactory that uses ServiceLoader to detect any builders on the classpath.
 * This means builders are discovered semi-automatically by this factory.
 * If none of the discovered builders match, the builder forwards the request to JavaFXBuilderFactory, so  this class can be used instead of.
 * 
 * In order to make automatically discovered builders:
 * 1. Implement the jfxtras.fxml.BuilderService interface instead of the javafx.util.Builder interface on all builder implementations.
 * 2. Create a file in your project / jar called "META-INF/services/jfxtras.fxml.BuilderService"
 * 3. In that file specify the full class name of all builders from 1 that you want to make auto-discoverable, each name on a name line.
 * 4. Use this builder instead of the default, for example like so: FXMLLoader.load(url, null, new JFXtrasBuilderFactory());
 * 
 * @author Tom Eugelink
 *
 */
public class JFXtrasBuilderFactory implements BuilderFactory {
	
	// support Java META-INF services
	private static ServiceLoader<BuilderService> builderServiceLoader = ServiceLoader.load(BuilderService.class);

	/**
	 * 
	 */
    public JFXtrasBuilderFactory() {
    	javaFXBuilderFactory = new JavaFXBuilderFactory();
    }
    final private BuilderFactory javaFXBuilderFactory;

    /**
     * 
     */
    @Override
    public Builder<?> getBuilder(Class<?> clazz) {
    	
    	// try the ServiceLoader configured classes
    	for (BuilderService builderService : builderServiceLoader) {
    		if (builderService.isBuilderFor(clazz)) {
    			try
				{
					return builderService.getClass().getConstructor(new Class<?>[]{}).newInstance(new Object[]{});
				}
				catch (NoSuchMethodException e) { throw new RuntimeException(e); }
				catch (SecurityException e) { throw new RuntimeException(e); }
				catch (InstantiationException e) { throw new RuntimeException(e); }
				catch (IllegalAccessException e) { throw new RuntimeException(e); }
				catch (IllegalArgumentException e) { throw new RuntimeException(e); }
				catch (InvocationTargetException e) { throw new RuntimeException(e); }
    		}
        }
    	
    	// fall back to default factory
        return javaFXBuilderFactory.getBuilder(clazz);
    }
}