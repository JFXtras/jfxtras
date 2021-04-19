/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
module jfxtras.controls {
    exports jfxtras.scene.control;
    exports jfxtras.scene.control.builders;
    exports jfxtras.internal.scene.control.skin to jfxtras.agenda; // what does this bring?

    requires transitive jfxtras.common;
    requires transitive jfxtras.fxml;
    uses jfxtras.fxml.BuilderService;
    provides jfxtras.fxml.BuilderService with jfxtras.scene.control.builders.CalendarPickerBuilder
            , jfxtras.scene.control.builders.CalendarTextFieldBuilder
            , jfxtras.scene.control.builders.CalendarTimePickerBuilder
            , jfxtras.scene.control.builders.CalendarTimeTextFieldBuilder
            , jfxtras.scene.control.builders.LocalDatePickerBuilder
            , jfxtras.scene.control.builders.LocalDateTextFieldBuilder
            , jfxtras.scene.control.builders.LocalDateTimePickerBuilder
            , jfxtras.scene.control.builders.LocalDateTimeTextFieldBuilder
            , jfxtras.scene.control.builders.LocalTimePickerBuilder
            , jfxtras.scene.control.builders.LocalTimeTextFieldBuilder;

	requires transitive javafx.base;
	requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires transitive java.logging;
}