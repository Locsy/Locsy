package com.lovbomobile.locsy.renderer;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.SecretRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/10/13
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BootstrapSecretRenderer extends SecretRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.INPUTSECRET);


    @Override
    protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String redisplay = String.valueOf(component.getAttributes().get("redisplay"));
        if (redisplay == null || !redisplay.equals("true")) {
            currentValue = "";
        }

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "password", "type");
        writer.writeAttribute("name", component.getClientId(context),
                "clientId");

        String autoComplete = (String)
                component.getAttributes().get("autocomplete");
        if (autoComplete != null) {
            // only output the autocomplete attribute if the value
            // is 'off' since its lack of presence will be interpreted
            // as 'on' by the browser
            if ("off".equals(autoComplete)) {
                writer.writeAttribute("autocomplete",
                        "off",
                        "autocomplete");
            }
        }

        // render default text specified
        if (currentValue != null) {
            writer.writeAttribute("value", currentValue, "value");
        }

        RenderKitUtils.renderPassThruAttributes(context,
                writer,
                component,
                ATTRIBUTES,
                getNonOnChangeBehaviors(component));
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        RenderKitUtils.renderOnchange(context, component, false);

        String styleClass;
        if (null != (styleClass = (String)
                component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        Object bootstrapRequiredAttribute = component.getAttributes().get("bootstrapRequired");
        if(bootstrapRequiredAttribute != null) {
            writer.writeAttribute("required","true","required");
        }

        writer.endElement("input");

    }
}
