/*
 * generated by Xtext
 */
package eu.artist.postmigration.nfrvt.lang.gel.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import eu.artist.postmigration.nfrvt.lang.gel.ui.internal.GELActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class GELExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return GELActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return GELActivator.getInstance().getInjector(GELActivator.EU_ARTIST_POSTMIGRATION_NFRVT_LANG_GEL_GEL);
	}
	
}
