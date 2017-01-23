/**
 * AgendaRenderDaysFromDisplayedDateTest.java
 *
 * Copyright (c) 2011-2016, JFXtras
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

package jfxtras.scene.control.agenda.test;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.Slider;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaysFromDisplayedSkin;
import jfxtras.test.TestUtil;

/**
 * 
 */
public class AgendaRenderDaysFromDisplayedDateTest extends AbstractAgendaTestBase {

	/**
	 * 
	 */
	public Parent getRootNode()
	{
		Parent parent = super.getRootNode();
		agenda.setSkin(new AgendaDaysFromDisplayedSkin(agenda));
		return parent;
	}

	/**
	 * 
	 */
	@Test
	public void renderVanilla()
	{
		// GIVEN a default agenda
		
		// THEN the days before slider should be setup with its default values
		Slider daysBeforeSlider = (Slider)find("#daysBeforeSlider");
		Assert.assertEquals(-9.0, daysBeforeSlider.getMin(), 0.0001);
		Assert.assertEquals(0.0, daysBeforeSlider.getMax(), 0.0001);
		Assert.assertEquals(-1.0, daysBeforeSlider.getValue(), 0.0001);
		
		// AND the days after slider should be setup with its default values
		Slider daysAfterSlider = (Slider)find("#daysAfterSlider");
		Assert.assertEquals(0.0, daysAfterSlider.getMin(), 0.0001);
		Assert.assertEquals(9.0, daysAfterSlider.getMax(), 0.0001);
		Assert.assertEquals(6.0, daysAfterSlider.getValue(), 0.0001);
		//TestUtil.sleep(3000);
	}

	/**
	 * 
	 */
	@Test
	public void renderAfterCSS()
	{
		// GIVEN a default agenda
		
		// WHEN the days before and after slider extremes are set through CSS
		TestUtil.runThenWaitForPaintPulse( () -> {
			agenda.setStyle("-fxx-days-before-furthest:-5; -fxx-days-after-furthest:20;");
		});
			
		// THEN the days before slider should only have changed its extreme 
		Slider daysBeforeSlider = (Slider)find("#daysBeforeSlider");
		Assert.assertEquals(-5.0, daysBeforeSlider.getMin(), 0.0001);
		Assert.assertEquals(0.0, daysBeforeSlider.getMax(), 0.0001);
		Assert.assertEquals(-1.0, daysBeforeSlider.getValue(), 0.0001);
		
		// AND the days after slider should only have changed its extreme 
		Slider daysAfterSlider = (Slider)find("#daysAfterSlider");
		Assert.assertEquals(0.0, daysAfterSlider.getMin(), 0.0001);
		Assert.assertEquals(20.0, daysAfterSlider.getMax(), 0.0001);
		Assert.assertEquals(6.0, daysAfterSlider.getValue(), 0.0001);
	}
}
