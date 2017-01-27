package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static mcjty.immcraft.books.BookParser.SECTION_MARGIN;

public class BookRenderHelper {

    public static void renderPage(List<BookPage> pages, int index, double x, double y, double z, float scale) {
        GlStateManager.rotate(-33, 1, 0, 0);
        GlStateManager.scale(.6, .6, .6);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        GlStateManager.translate(-0.5F, 0.5F, 0.07F);
        float f3 = 0.0075F/1.5f;
        GlStateManager.scale(f3 * scale, -f3 * scale, f3);
        GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);

        renderText(pages.get(index), scale);

        ClientProxy.font.drawString(700.0f, -650.0f, (index+1) + "/" + pages.size(), 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f);


        Minecraft.getMinecraft().entityRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void renderText(BookPage page, float scale) {
        int cury = 0;
        for (RenderSection section : page.getSections()) {
            for (RenderElement element : section.getElements()) {
                element.render(cury);
            }
            cury += section.getHeight() + SECTION_MARGIN;
        }
    }
}